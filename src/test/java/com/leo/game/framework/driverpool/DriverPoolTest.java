package com.leo.game.framework.driverpool;

import static org.junit.Assert.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.leo.game.framework.driverpool.exception.PoolException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DriverPoolTest {

    @Autowired
    DriverPool pool;
    
	@Test
	public void testInit() {
		try {
			//pool.init();
			System.out.println("pool " + pool);
			
			ExecutorService executor = Executors.newFixedThreadPool(pool.getPoolProperties().getMaxSize());
			for(int i = 0 ; i < 200 ; i++) {
				executor.submit(new DriverPoolTestThread(pool));
			}
			Thread.sleep(120000);
			System.out.println("pool " + pool);
			for(PooledDriver driver : pool.getIdle()) {
				System.out.println("name " + driver.getDriverName() + " object : " + driver);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue(pool.getSize().get() > 0);
	}

}
