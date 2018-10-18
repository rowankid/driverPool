/**   
 * Copyright © 2018 CMBC Info. Tech Ltd. All rights reserved.
 * 
 * @Package: com.umbraViv.methods 
 * @author: Viv. 
 * @creatDate: 2018年5月16日 下午8:12:40 
 */
package com.leo.game.framework.driverpool.driver;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

@Component
public class BrowserInitUtil {
	
	/** 
	 * timeout setting for browser
	 * 
	 * @param driver
	 * @param seconds void
	 */ 
	public void timeWait(WebDriver driver, int seconds) {
		//driver.manage().timeouts().pageLoadTimeout(seconds, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(seconds, TimeUnit.SECONDS);

	}

	/** 
	 * window size
	 * 
	 * @param driver
	 * @param flag
	 */
	public void windowSet(WebDriver driver, int flag) {
		switch(flag) {
		case 0 : driver.manage().window().fullscreen();
		case 1 : driver.manage().window().maximize();
		default : ;
		}
	}

	/** 
	 * clean cookie or not
	 * 
	 * @param driver
	 */
	public void clearCookies(WebDriver driver) {
		driver.manage().deleteAllCookies();
	}

}
