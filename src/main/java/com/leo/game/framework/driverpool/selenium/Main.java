package com.leo.game.framework.driverpool.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Main {

	public void action(Executable executor, WebDriver driver, String command) {
		executor.execute(driver, command);
	}
	
	public static void main(String[] args) {

		WebDriver driver = new FirefoxDriver();
		String command = "";
		
		Main testAJAX = new Main();
		WebDriverExecutor e = new WebDriverExecutor();
		//testAJAX.action(e::executeAJAX, driver, command);
		testAJAX.action(e::executeJS, driver, command);
	}
}
