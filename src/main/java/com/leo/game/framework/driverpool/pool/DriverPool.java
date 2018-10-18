package com.leo.game.framework.driverpool.pool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;

import org.openqa.selenium.WebDriverException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.leo.game.framework.driverpool.driver.PooledDriver;
import com.leo.game.framework.driverpool.driver.WebDriverCreater;
import com.leo.game.framework.driverpool.exception.PoolException;
import com.leo.game.framework.driverpool.util.Constants;
import com.leo.game.framework.driverpool.util.PoolConfiguration;

@Component
public class DriverPool {

    /**
     * All the information about the driver pool
     * These are the properties the pool got instantiated with
     */
    @Autowired
    private PoolConfiguration poolProperties;

    @Autowired
    private WebDriverCreater creater;
    
    /**
     * Carries the size of the pool, instead of relying on a queue implementation
     * that usually iterates over to get an exact count
     */
    private AtomicInteger size = new AtomicInteger(0);
    
    /**
     * Carries the suffix of all drivers
     */
    private AtomicInteger suffix = new AtomicInteger(0);
    
    /**
     * Contains all the drivers that are in use
     * 
     * TODO - this shouldn't be a blocking queue, simply a list to hold our objects
     */
    private BlockingQueue<PooledDriver> busy;
    
    /**
     * Contains all the idle drivers
     */
    private BlockingQueue<PooledDriver> idle;
    
    /**
     * The thread that is responsible for checking abandoned and idle threads
     */
    private volatile PoolCleaner poolCleaner;
    
    /**
     * counter to track how many threads are waiting for a connection
     */
    private AtomicInteger waitcount = new AtomicInteger(0);

	private boolean closed;
	
    /**
     * The counters for statistics of the pool.
     */
    private final AtomicLong borrowedCount = new AtomicLong(0);
    private final AtomicLong returnedCount = new AtomicLong(0);
    private final AtomicLong createdCount = new AtomicLong(0);
    private final AtomicLong releasedCount = new AtomicLong(0);
    private final AtomicLong reconnectedCount = new AtomicLong(0);
    private final AtomicLong removeAbandonedCount = new AtomicLong(0);
    private final AtomicLong releasedIdleCount = new AtomicLong(0);

    public String toString() {
    	return "pool_size :" + this.size.get()
    			+ "; busy_size :" + this.busy.size()
    			+ "; idle_size :" + this.idle.size()
    			+ "; borrow_count:" + this.borrowedCount
    			+ "; return_count:" + this.returnedCount
    			+ "; reconnected_count:" + this.reconnectedCount
    			+ "; releasedIdleCount:" + this.releasedIdleCount
    			+ "; releasedCount:" + this.releasedCount;
    }
    
    @PostConstruct
    public void init() throws PoolException, InterruptedException {
        
        //make space for 10 extra in case we flow over a bit
        busy = new LinkedBlockingQueue<>();
        idle = new LinkedBlockingQueue<>();
        
        //initializePoolCleaner(properties);
        PooledDriver[] initialPool = new PooledDriver[poolProperties.getInitialSize()];
        try{
        	for(int i = 0 ; i < initialPool.length ; i++) {
        		initialPool[i] = this.createDriver();
            	this.idle.put(initialPool[i]);
            	this.size.incrementAndGet();
        	}
        } catch(WebDriverException ex) {
        	ex.printStackTrace();
        } finally {
        	
        }
        setClosed(false);
    }
    
    /**
     * 1. to get driver from idle list;
     * 2. if not null, check driver validation;
     * 3. if null, create a new driver add it into busy list;
     * 
     * @param newDriver if true add a new driver, otherwise try to borrow from idle list
     * @param driverType
     * @return
     * @throws PoolException
     * @throws InterruptedException 
     */
    public PooledDriver borrowDriver(boolean newDriver) throws PoolException, InterruptedException {
    	if(this.isClosed()) {
    		throw new PoolException("the pool has been closed");
    	}
        PooledDriver driver = null;
    	if(!newDriver) {
    		driver = idle.poll();
    	}
    	//got driver from idle, check validation
    	if(driver != null) {
    		driver = validDriver(driver);
        	driver.setTimestamp(System.currentTimeMillis());
    		this.borrowedCount.incrementAndGet();
    		this.busy.put(driver);
        	return driver;
    	}
    	//if driver is null, the driver need to be created
    	else {
            if (this.size.get() < poolProperties.getMaxSize()) {
                if (size.addAndGet(1) > getPoolProperties().getMaxSize()) {
                    //if we got here, two threads passed through the first if
                    size.decrementAndGet();
                } else {
                	driver = createDriver();
                	System.out.println("create driver " + driver.getDriverName() + "size : " + this.size.get() + ", max size : " + poolProperties.getMaxSize());
                	try {
                		driver.lock();
                    	driver.setTimestamp(System.currentTimeMillis());
                		this.borrowedCount.incrementAndGet();
                		this.busy.put(driver);
                    	return driver;
                	}finally {
                		driver.unlock();
                	}
                }
            }
    	}
    	return null;
    }
    
    public void returnDriver(PooledDriver driver) throws PoolException {
    	if(this.isClosed()) {
    		//TODO: release this driver;
    		throw new PoolException("the pool has been closed");
    	}
    	if(driver != null) {
            try {
                driver.lock();
                if(busy.remove(driver)) {
                	driver.setTimestamp(System.currentTimeMillis());
                    if(!driver.isDiscarded()) {
                    	release(driver);
                    }
                    else {
                        releasedIdleCount.incrementAndGet();
                        //size.addAndGet(-1);
                        size.decrementAndGet();
                    }
                }
            } catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
            	driver.unlock();
            }
    	}
    }
    
    private void release(PooledDriver driver) throws PoolException, InterruptedException {
        if (driver == null) return;
        try {
        	driver.lock();
            if (driver.release()) {
                this.idle.put(driver);
                returnedCount.incrementAndGet();
            }
            else {
            	discardDriver(driver);
            }
        } finally {
        	driver.unlock();
        }
    }
    
    public void discardDriver(PooledDriver driver) throws PoolException, InterruptedException {
    	driver.discard();
    	this.returnDriver(driver);
    	driver.setInterceptor(null);
    	driver.setDriver(null);
    	driver = null;
    }
    
	protected PooledDriver createDriver() throws PoolException, InterruptedException {
		PooledDriver driver = new PooledDriver(getDriverName(), this, this.getPoolProperties());
		driver.initDriver(creater);
    	return driver;
    }
	
    private PooledDriver validDriver(PooledDriver driver) throws InterruptedException {
        return driver.validDriver();
	}
    
    private String getDriverName() {
    	return Constants.DRIVERNAME_PREDIX + suffix.incrementAndGet();
    }

	public AtomicInteger getSize() {
		return size;
	}

	public void setSize(AtomicInteger size) {
		this.size = size;
	}

	public AtomicInteger getSuffix() {
		return suffix;
	}

	public void setSuffix(AtomicInteger suffix) {
		this.suffix = suffix;
	}

	public PoolConfiguration getPoolProperties() {
		return poolProperties;
	}

	public void setPoolProperties(PoolConfiguration poolProperties) {
		this.poolProperties = poolProperties;
	}

	public BlockingQueue<PooledDriver> getBusy() {
		return busy;
	}

	public void setBusy(BlockingQueue<PooledDriver> busy) {
		this.busy = busy;
	}

	public BlockingQueue<PooledDriver> getIdle() {
		return idle;
	}

	public void setIdle(BlockingQueue<PooledDriver> idle) {
		this.idle = idle;
	}

	public PoolCleaner getPoolCleaner() {
		return poolCleaner;
	}

	public void setPoolCleaner(PoolCleaner poolCleaner) {
		this.poolCleaner = poolCleaner;
	}

	public AtomicInteger getWaitcount() {
		return waitcount;
	}

	public void setWaitcount(AtomicInteger waitcount) {
		this.waitcount = waitcount;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public AtomicLong getBorrowedCount() {
		return borrowedCount;
	}

	public AtomicLong getReturnedCount() {
		return returnedCount;
	}

	public AtomicLong getCreatedCount() {
		return createdCount;
	}

	public AtomicLong getReleasedCount() {
		return releasedCount;
	}

	public AtomicLong getReconnectedCount() {
		return reconnectedCount;
	}

	public AtomicLong getRemoveAbandonedCount() {
		return removeAbandonedCount;
	}

	public AtomicLong getReleasedIdleCount() {
		return releasedIdleCount;
	}
}
