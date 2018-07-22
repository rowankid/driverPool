package com.leo.game.framework.driverpool;

import static org.junit.Assert.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runners.MethodSorters;

import com.leo.game.framework.driverpool.driver.PooledDriver;
import com.leo.game.framework.driverpool.pool.DriverPool;


@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DriverPoolTest {

    @Autowired
    DriverPool pool;

	@Test
	public void testBorrowNDiscardORelease() {
		int pooltime = 50;
		try {
			System.out.println("pool " + pool);
			ExecutorService executor = Executors.newFixedThreadPool(pool.getPoolProperties().getMaxSize());
			for(int i = 0 ; i < pooltime ; i++) {
				executor.submit(new PoolBorrownDiscardReleaseThread(pool));
			}
			//while(true) {
				Thread.sleep(60000);
				for(PooledDriver driver : pool.getIdle()) {
					System.out.println("name " + driver.getDriverName() + " object : " + driver);
				}
				System.out.println("pool " + pool);
			//}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue(pool.getBorrowedCount().get() <= pool.getPoolProperties().getMaxSize());
	}

	@Test
	public void testDiscard() {
		try {
			System.out.println("discard pool " + pool);
			ExecutorService executor = Executors.newFixedThreadPool(1);
			executor.submit(new PoolDiscardThread(pool));
			Thread.sleep(10000);
			for(PooledDriver driver : pool.getIdle()) {
				System.out.println("name " + driver.getDriverName() + " object : " + driver);
			}
			System.out.println("discard pool " + pool);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue(pool.getSize().get() < pool.getPoolProperties().getMaxSize());
	}
	
	@Test
	public void testBorrowNRelease() {
		int pooltime = 20;
		try {
			System.out.println("pool " + pool);
			ExecutorService executor = Executors.newFixedThreadPool(pool.getPoolProperties().getMaxSize());
			for(int i = 0 ; i < pooltime ; i++) {
				executor.submit(new PoolBorrownReleaseThread(pool));
			}
			Thread.sleep(30000);
			for(PooledDriver driver : pool.getIdle()) {
				System.out.println("name " + driver.getDriverName() + " object : " + driver);
			}
			System.out.println("pool " + pool);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue(pool.getBorrowedCount().get() <= pool.getPoolProperties().getMaxSize());
	}

}
