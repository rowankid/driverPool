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

	private String browserDriverPath;
	private int globalTimeout;
	private int windowStyle;
	private boolean clearCookies;
	
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
	public String getBrowserDriverPath() {
		return browserDriverPath;
	}
	public void setBrowserDriverPath(String browserDriverPath) {
		this.browserDriverPath = browserDriverPath;
	}
	public int getGlobalTimeout() {
		return globalTimeout;
	}
	public void setGlobalTimeout(int globalTimeout) {
		this.globalTimeout = globalTimeout;
	}
	public int getWindowStyle() {
		return windowStyle;
	}
	public void setWindowStyle(int windowStyle) {
		this.windowStyle = windowStyle;
	}
	public boolean isClearCookies() {
		return clearCookies;
	}
	public void setClearCookies(boolean clearCookies) {
		this.clearCookies = clearCookies;
	}
}
