package com.leo.game.framework.driverpool;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.openqa.selenium.WebDriver;

import com.leo.game.framework.driverpool.driver.DriverCreater;

public class PooledDriver {

	private String driverName;
	
	private DriverInterceptor handler;
	
    /**
     * The properties for the connection pool
     */
    protected PoolConfiguration poolProperties;
    
    /**
     * The underlying web driver
     */
    private volatile WebDriver driver;
    
    /**
     * The parent
     */
    protected DriverPool parent;
    
    /**
     * timestamp to keep track of validation intervals
     */
    private volatile long initTime;

    /**
     * Timestamp the driver was last 'touched' by the pool
     */
    private volatile long timestamp;

    /**
     * Set to true if this driver has been discarded by the pool
     */
    private volatile boolean discarded;
    
    /**
     * Lock for this driver only
     */
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(false);
    
    private AtomicBoolean released = new AtomicBoolean(false);
    
    public PooledDriver(String driverName, DriverPool parent, PoolConfiguration poolProperties){
    	super();
    	this.setDriverName(driverName);
    	this.setParent(parent);
    	this.setPoolProperties(poolProperties);
    	this.setDiscarded(false);
    	this.initTime = System.currentTimeMillis();
    }
    
    public String toString() {
    	return "Driver name: " + this.driverName
    			+ "; inited at: " + new Date(this.initTime)
    			+ "; last_updated  at: " + new Date(this.timestamp);
    }
    
    /**
     * initiate a driver which is valid for work
     * 
     * @return driver;
     */
	public PooledDriver initDriver(DriverCreater creater) {
		this.driver = creater.initFirefoxBrowser();
		this.driver.get("http://www.baidu.com");
		return this;
	}
	
	public PooledDriver validDriver() {
		try {
			System.out.println("start to valid a new driver " + this.getDriverName() + " at " + new Date());
			Thread.sleep(1000);
			System.out.println("finish to valid a new driver " + this.getDriverName() + " at " + new Date());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public PooledDriver regenerateDriver() {
		try {
			System.out.println("started to regenerate this driver " + this.getDriverName() + " at " + new Date());
			Thread.sleep(1000);
			System.out.println("finish to regenerate this driver " + this.getDriverName() + " at " + new Date());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public void startToWork() throws InterruptedException {
		try {
			System.out.println("started to work this driver " + this.getDriverName() + " at " + new Date());
			Thread.sleep(1000);
			System.out.println("finish to work this driver " + this.getDriverName() + " at " + new Date());
		} catch (InterruptedException e) {
			this.parent.getBusy().remove(this);
			this.parent.getIdle().put(this);
		} finally {
			
		}
	}
	
	public boolean release() throws InterruptedException {
		try {
			System.out.println("started to release this driver " + this.getDriverName() + " at " + new Date());
			Thread.sleep(1000);
			System.out.println("finish to release this driver " + this.getDriverName() + " at " + new Date());
			return true;
		} catch (InterruptedException e) {
			
		} finally {
			
		}
		return false;
	}
	
    /**
     * Locks the connection only if either {@link PoolConfiguration#isPoolSweeperEnabled()} or
     * {@link PoolConfiguration#getUseLock()} return true. The per connection lock ensures thread safety is
     * multiple threads are performing operations on the connection.
     * Otherwise this is a noop for performance
     */
    public void lock() {
        if (poolProperties.isUseLock() || this.poolProperties.isPoolSweeperEnabled()) {
            //optimized, only use a lock when there is concurrency
            lock.writeLock().lock();
        }
    }
    
    /**
     * Unlocks the connection only if the sweeper is enabled
     * Otherwise this is a noop for performance
     */
    public void unlock() {
        if (poolProperties.isUseLock() || this.poolProperties.isPoolSweeperEnabled()) {
          //optimized, only use a lock when there is concurrency
            lock.writeLock().unlock();
        }
    }
    
	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public DriverInterceptor getHandler() {
		return handler;
	}

	public void setHandler(DriverInterceptor handler) {
		this.handler = handler;
	}

	public PoolConfiguration getPoolProperties() {
		return poolProperties;
	}

	public void setPoolProperties(PoolConfiguration poolProperties) {
		this.poolProperties = poolProperties;
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public DriverPool getParent() {
		return parent;
	}

	public void setParent(DriverPool parent) {
		this.parent = parent;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isDiscarded() {
		return discarded;
	}

	public void setDiscarded(boolean discarded) {
		this.discarded = discarded;
	}

	public AtomicBoolean getReleased() {
		return released;
	}

	public void setReleased(AtomicBoolean released) {
		this.released = released;
	}

	public ReentrantReadWriteLock getLock() {
		return lock;
	}

	public long getInitTime() {
		return initTime;
	}
}
