package com.leo.game.framework.driverpool;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SeleniumTest {

	@Test
	public void openWebBrowser() {
		WebDriver driver = new FirefoxDriver();//new ChromeDriver();//
		driver.get("www.baidu.com");
		driver.quit();
		assertNotNull(driver);
	}
}
