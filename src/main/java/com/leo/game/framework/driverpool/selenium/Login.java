package com.leo.game.framework.driverpool.selenium;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

@Component
public class Login {

	private boolean untilVisibleLocator(WebDriverWait wait, By by, WebDriver driver) {
		boolean result;
		try{
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			result = true;
		}
		catch(TimeoutException ex1){
			result = false;
		}
		finally{
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		return result;
	}
	
	public boolean jumpToCity(WebDriver driver, int serverId) {
		String accessedURL = "http://sg" + serverId + ".dipan.com/city";
		String title = "";
		try{
			driver.get(accessedURL); //UnreachableBrowserException is able to be sent since browser has no response
			title = driver.getTitle();
		} catch(TimeoutException ex){
			title = driver.getTitle();
		} catch (WebDriverException ex){
			driver.get(accessedURL); //UnreachableBrowserException is able to be sent since browser has no response
		}
		boolean logined = false;
		int time = 3;
		while(!logined && time > 0){
			logined = title.contains("城池");
			try {
				Thread.sleep(1000);
				title = driver.getTitle();
			} catch (Exception ex){
				ex.printStackTrace();
			}
			time--;
		}
		return logined;
	}
	
	/**
	 * To do the login to sg.dipan.com action in page.
	 * 
	 * @throws LoginFailureException 
	 * @throws NoActionException 
	 */
	public void login(String id, String pw, WebDriver driver) {
		
		driver.get("http://pass.dipan.com/aspx/logindo.aspx?callback=http://sg.dipan.com");
		//driver.get("http://pass.dipan.com/aspx/logindo.aspx");
		driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS); 
		WebDriverWait wait =  new WebDriverWait(driver, 40);
		if(!untilVisibleLocator(wait, By.id("btn_Login"), driver)) {
			driver.get("http://pass.dipan.com/aspx/logindo.aspx?callback=http://sg.dipan.com");
			//driver.get("http://pass.dipan.com/aspx/logindo.aspx");
		}
		try{
			WebElement element1 = driver.findElement(By.id("UserName1"));
			element1.clear();
			element1.sendKeys(id);
			WebElement element2 = driver.findElement(By.id("Password1"));
			element2.clear();
			element2.sendKeys(pw);
			WebElement element3 = driver.findElement(By.id("btn_Login"));
			element3.click();
		}
		catch(WebDriverException ex){
			ex.printStackTrace();
		}
	}
	
}
