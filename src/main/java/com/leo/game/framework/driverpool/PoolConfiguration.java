package com.leo.game.framework.driverpool;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value={"classpath:pool.properties"}, 
			ignoreResourceNotFound=false, 
			encoding="UTF-8",
			name="pool.properties")
@ConfigurationProperties
public class PoolConfiguration {
	
	private boolean useLock;
	private boolean poolSweeperEnabled;
	private int initialSize;
	private int maxSize;
	private int driverType;
	
	public boolean isUseLock() {
		return useLock;
	}
	public void setUseLock(boolean useLock) {
		this.useLock = useLock;
	}
	public boolean isPoolSweeperEnabled() {
		return poolSweeperEnabled;
	}
	public void setPoolSweeperEnabled(boolean poolSweeperEnabled) {
		this.poolSweeperEnabled = poolSweeperEnabled;
	}
	public int getInitialSize() {
		return initialSize;
	}
	public void setInitialSize(int initialSize) {
		this.initialSize = initialSize;
	}
	public int getMaxSize() {
		return maxSize;
	}
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}
	public int getDriverType() {
		return driverType;
	}
	public void setDriverType(int driverType) {
		this.driverType = driverType;
	}
}
