package com.leo.game.framework.driverpool.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.leo.game.framework.driverpool.util.PoolConfiguration;

@Component
public class WebDriverCreater {
	
    @Autowired
	PoolConfiguration poolConfig;
    
    @Autowired
    BrowserInitUtil setting;
	
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
	
	public WebDriver initHeadlessChromeBrowser() {
		System.out.println(System.currentTimeMillis() + " |  start");
	    System.setProperty("webdriver.chrome.driver", "D:\\server\\chromeDriver243\\chromedriver.exe");
		ChromeOptions chromeOptions = new ChromeOptions();
	    chromeOptions.addArguments("--headless");
	    chromeOptions.addArguments("--disable-gpu"); 
	    WebDriver driver = new ChromeDriver(chromeOptions);
	    return driver;
	}

	public WebDriver initPhantomjsBrowser() {
		System.out.println(System.currentTimeMillis() + " |  start");
        //设置必要参数
        DesiredCapabilities dcaps = new DesiredCapabilities();
        //ssl证书支持
        dcaps.setCapability("acceptSslCerts", true);
        //截屏支持
        dcaps.setCapability("takesScreenshot", true);
        //css搜索支持
        dcaps.setCapability("cssSelectorsEnabled", true);
        //js支持
        dcaps.setJavascriptEnabled(true);
        //驱动支持
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,"D:\\server\\phantomjs\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
        //创建无界面浏览器对象
        WebDriver driver = new PhantomJSDriver(dcaps);
	    return driver;
	}
}
