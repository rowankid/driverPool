package com.leo.game.framework.driverpool.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.leo.game.framework.driverpool.PoolConfiguration;

@Component
public class DriverCreater {

    @Autowired
	PoolConfiguration poolConfig;

    @Autowired
    PageSettings setting;
	
	/**
	 * @Description firefox gecko driver v0.21.0
	 * @return boolean
	 */
	public boolean addFireFoxGeoDriverV021(String driverpath) {
		try {
			System.setProperty("webdriver.gecko.driver", driverpath);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * init settings includes, driver path, timeout, window size, cookie
	 * 
	 * @param driver
	 * @param timeout
	 * @param windowSet
	 * @param clearCookies
	 * @return WebDriver
	 */
	public WebDriver initFirefoxBrowser(String driverpath, int timeout, int windowSet, boolean clearCookies) {
		addFireFoxGeoDriverV021(driverpath); //set system variable for firefox gec drvier
		WebDriver driver = new FirefoxDriver();
		setting.timeWait(driver, timeout); // timeout
		setting.windowSet(driver, windowSet); //maximum broswer
		//clean cookies
		if (clearCookies) {
			setting.clearCookies(driver);
		}
		return driver;
	}
	
	public WebDriver initFirefoxBrowser() {
		WebDriver driver = initFirefoxBrowser(poolConfig.getBrowserDriverPath(), 
				poolConfig.getGlobalTimeout(), poolConfig.getWindowStyle(), poolConfig.isClearCookies());
		return driver;
	}
}
