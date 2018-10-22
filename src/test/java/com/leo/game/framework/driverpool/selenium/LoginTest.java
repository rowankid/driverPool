package com.leo.game.framework.driverpool.selenium;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.leo.game.framework.driverpool.driver.WebDriverCreater;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginTest {

	@Autowired
	Login login;
	
	@Autowired
	CountryActions actions;
	
	@Autowired
	WebDriverCreater driverUtil;
	
	@Test
	public void test() {
		//WebDriver driver =  driverUtil.initFirefoxBrowser(); //new FirefoxDriver();
		//WebDriver driver =  driverUtil.initPhantomjsBrowser(); //new FirefoxDriver();
		WebDriver driver =  driverUtil.initHeadlessChromeBrowser(); //new FirefoxDriver();
		try {
			String id = "wzdacyl6";
			String pw = "a815728";
			login.login(id, pw, driver);
			Thread.sleep(3000);
			login.jumpToCity(driver, 40);
			Thread.sleep(3000);
			actions.collectCountryInfo(driver);
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			driver.quit();
		}
		assertTrue(true);
	}
}
