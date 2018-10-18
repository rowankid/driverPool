package com.leo.game.framework.driverpool.selenium;

import java.time.Duration;
import java.util.Optional;
import java.util.Random;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;

@Component
public class WebDriverExecutor {

	public Optional<?> executeJS(WebDriver driver, String command) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(command);
		return Optional.empty();
	}

	public Optional<String> executeAJAXOld(WebDriver driver, String command) {
		String ajaxResultName = "info_dipanAjax" + new Random().nextInt(1000);
		String jsCommand = new StringBuilder("window.")
				.append(ajaxResultName)
				.append("='failed';var RemoteData=new Dipan.SanGuo.Common.RemoteData();RemoteData.onComplete=function(resp){window.")
				.append(ajaxResultName)
				.append("=resp;};")
				.append(command)
				.toString();

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(jsCommand);
		new WebDriverWait((org.openqa.selenium.WebDriver) driver, 10)
			.until (new ExpectedConditionBing<Boolean>(ajaxResultName));
		
		return Optional.of((String) js.executeScript("return window." + ajaxResultName + ";", ""));
	}
	
	public JsonObject executeAJAX20181013(WebDriver driver, String command){
		Object response = ((JavascriptExecutor) driver).executeAsyncScript(
				          "var callback = arguments[arguments.length - 1];" +
				          "var xhr = new XMLHttpRequest();" +
				          "xhr.open('POST', 'http://sg40.dipan.com/GateWay/City.ashx?id=23', true);" +
				          "xhr.onreadystatechange = function() {" +
				          "  if (xhr.readyState == 4) {" +
				          "    callback(xhr.responseText);" +
				          "  }" +
				          "};" +
				          "xhr.send();");
		return (JsonObject) new JsonParser().parse((String) response);
	}
	
	public JsonObject executeAJAX(WebDriver driver, String command){
		Object response = ((JavascriptExecutor) driver).executeAsyncScript(
				          "var callback = arguments[arguments.length - 1];" +
				          "var xhr = new XMLHttpRequest();" +
				          "xhr.open('POST', '" + command +"', true);" +
				          "xhr.onreadystatechange = function() {" +
				          "  if (xhr.readyState == 4) {" +
				          "    callback(xhr.responseText);" +
				          "  }" +
				          "};" +
				          "xhr.send();");
		try {
			return (JsonObject) new JsonParser().parse((String) response);
		} catch(ClassCastException | JsonSyntaxException ex) {
			System.out.println(response);
			return new JsonObject();
		}
	}
	public Optional<String> executeAJAX_old1(WebDriver driver, String command) {
		String ajaxResultName = "info_dipanAjax" + new Random().nextInt(1000);
		String jsCommand = new StringBuilder("window.")
				.append(ajaxResultName)
				.append("='failed';var RemoteData=new Dipan.SanGuo.Common.RemoteData();RemoteData.onComplete=function(resp){window.")
				.append(ajaxResultName)
				.append("=resp;};")
				.append(command)
				.toString();

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(jsCommand);

		try {

			new FluentWait<WebDriver>(driver)
					//.withTimeout(Duration.ofNanos(1))
					//.pollingEvery(Duration.ofNanos(1))
					.withTimeout(Duration.ofSeconds(10))
					.pollingEvery(Duration.ofSeconds(2))
					.withMessage("TTTTTTTTTTTTTTTTT")
					.until (driver1 -> {
						JavascriptExecutor jss = (JavascriptExecutor) driver1;
						return (Boolean) jss.executeScript("return window." + ajaxResultName + " != 'failed';", "");
					});
		} catch(TimeoutException ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			return Optional.empty();
		}

		return Optional.of((String) js.executeScript("return window." + ajaxResultName + ";", ""));
		
		/*Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(10, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS);
		
		wait.until (driver1 -> {
			JavascriptExecutor jss = (JavascriptExecutor) driver1;
			return (Boolean) jss.executeScript("return window." + ajaxResultName + " != 'failed';", "");
		});*/

	}
	
}
