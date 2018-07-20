package com.leo.game.framework.driverpool;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PoolConfigurationTest {

    @Autowired
	PoolConfiguration poolConfig;
	
	@Test
	public void test() {
		System.out.println(poolConfig.getInitialSize());
		System.out.println(poolConfig.getMaxSize());
		System.out.println(poolConfig.isUseLock());
		System.out.println(poolConfig.isPoolSweeperEnabled());
		assertTrue(poolConfig.getMaxSize() > 0);
	}

	@Test
	public void testUseLock() {
		assertTrue(poolConfig.isUseLock());
	}
}
