package com.leo.game.framework.driverpool.selenium;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class ExpectedConditionBing<T> implements ExpectedCondition<T> {

	private String resultName;

	public ExpectedConditionBing(String resultName) {
		super();
		if (resultName != null) {
			this.resultName = resultName;
		} else {
			this.resultName = "ajaxResultName";
		}
	}
	
	@Override
	public T apply(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return (T) js.executeScript(
				new StringBuilder("return window.")
				.append(resultName)
				.append(" != 'failed';")
				.toString(), "");
	}
}
