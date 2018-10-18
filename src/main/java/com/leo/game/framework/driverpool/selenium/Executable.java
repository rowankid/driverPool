package com.leo.game.framework.driverpool.selenium;

import java.util.Optional;

import org.openqa.selenium.WebDriver;

@FunctionalInterface
public interface Executable {

	public Optional<?> execute(WebDriver driver, String command);
}
