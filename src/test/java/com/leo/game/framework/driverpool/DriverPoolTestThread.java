package com.leo.game.framework.driverpool;

public class DriverPoolTestThread implements Runnable {

	private DriverPool pool;
	
	public DriverPoolTestThread(DriverPool pool) {
		super();
		this.setPool(pool);
	}
	
	@Override
	public void run() {
		try {
			System.out.println(this + "before borrow " + " pool busy " + pool.getBusy().size() + "; idle " + pool.getIdle().size());
			PooledDriver driver = pool.borrowDriver(false);
			System.out.println(this + "after borrow " + "pool busy " + pool.getBusy().size() + "; idle " + pool.getIdle().size());
			System.out.println(driver.getDriverName() + " start to work~!");
			driver.getDriver().get(getUrl());
			Thread.sleep((long)(Math.random() * 1000));
			System.out.println(this + "before release " + "pool busy " + pool.getBusy().size() + "; idle " + pool.getIdle().size());
			pool.release(driver);
			System.out.println(this + "after release " + "pool busy " + pool.getBusy().size() + "; idle " + pool.getIdle().size());
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
