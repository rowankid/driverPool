package com.leo.game.framework.driverpool;

import com.leo.game.framework.driverpool.driver.PooledDriver;
import com.leo.game.framework.driverpool.pool.DriverPool;

public class PoolBorrownDiscardReleaseThread implements Runnable {

	private DriverPool pool;
	
	public PoolBorrownDiscardReleaseThread(DriverPool pool) {
		super();
		this.setPool(pool);
	}
	
	@Override
	public void run() {
		try {
			//System.out.println(this + "before borrow " + " pool busy " + pool.getBusy().size() + "; idle " + pool.getIdle().size());
			PooledDriver driver = pool.borrowDriver(false);
			//System.out.println(this + "after borrow " + "pool busy " + pool.getBusy().size() + "; idle " + pool.getIdle().size());
			System.out.println(driver.getDriverName() + " start to work~!");
			//driver.getDriver().get(getUrl());
			long waitMS = (long)(Math.random() * 1000);
			Thread.sleep(waitMS);
			
			switch((int)(Math.random() * 10) % 2) {
				case 0: {
					//System.out.println(this + "before release " + "pool busy " + pool.getBusy().size() + "; idle " + pool.getIdle().size());
					pool.returnDriver(driver);
					System.out.println("release driver " + driver.getDriverName() + ", executor " + this);
				};return;
				case 1: {
					//System.out.println(this + "before discard " + "pool busy " + pool.getBusy().size() + "; idle " + pool.getIdle().size());
					pool.discardDriver(driver);
					System.out.println("discard driver " + driver.getDriverName() + ", executor " + this);
					//System.out.println(this + "after discard " + "pool busy " + pool.getBusy().size() + "; idle " + pool.getIdle().size());
				};return;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getUrl() {
		int i = (int)((Math.random()*10)%3);
		switch(i) {
		case 0: return "http://image.baidu.com/";
		case 1: return "http://news.baidu.com/";
		case 2: return "https://tieba.baidu.com/index.html";
		default: return "http://www.baidu.com/";
		}
	}

	public DriverPool getPool() {
		return pool;
	}

	public void setPool(DriverPool pool) {
		this.pool = pool;
	}

}
