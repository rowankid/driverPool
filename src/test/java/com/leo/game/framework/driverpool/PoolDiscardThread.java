package com.leo.game.framework.driverpool;

import com.leo.game.framework.driverpool.driver.PooledDriver;
import com.leo.game.framework.driverpool.pool.DriverPool;

public class PoolDiscardThread implements Runnable {

	private DriverPool pool;
	
	public PoolDiscardThread(DriverPool pool) {
		super();
		this.setPool(pool);
	}
	
	@Override
	public void run() {
		try {
			System.out.println("before discard borrow " + " pool busy " + pool.getBusy().size() + "; idle " + pool.getIdle().size());
			PooledDriver driver = pool.borrowDriver(false);
			System.out.println("after discard borrow " + "pool busy " + pool.getBusy().size() + "; idle " + pool.getIdle().size());
			System.out.println(driver.getDriverName() + " start to work~!");
			//driver.getDriver().get("http://www.sina.com.cn/");
			Thread.sleep((long)(Math.random() * 5000));
			System.out.println("before discard " + "pool busy " + pool.getBusy().size() + "; idle " + pool.getIdle().size());
			pool.discardDriver(driver);
			System.out.println("after discard " + "pool busy " + pool.getBusy().size() + "; idle " + pool.getIdle().size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DriverPool getPool() {
		return pool;
	}

	public void setPool(DriverPool pool) {
		this.pool = pool;
	}

}
