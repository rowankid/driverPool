package com.leo.game.framework.driverpool;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.leo.game.framework.driverpool.driver.WebDriverCreater;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SeleniumTest {
	
	@Autowired
	WebDriverCreater driverUtil;

	@Test
	public void openWebBrowser() {
		//System.setProperty("webdriver.gecko.driver", "D:\\tools\\selenium\\driver\\geckodriver-v0.21.0-win64\\geckodriver.exe");
		WebDriver driver =  driverUtil.initFirefoxBrowser(); //new FirefoxDriver();
		driver.get("http://www.baidu.com");
		driver.quit();
		assertNotNull(driver);
	}
}
