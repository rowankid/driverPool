package com.leo.game.framework.driverpool.selenium;


import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;

@Component
public class CountryActions {
	
	@Autowired
	WebDriverExecutor executor;
	
	public void collectCountryInfo(WebDriver driver)  {
		/*String jsCountrylInfo = "RemoteData.Post(\"/GateWay/City.ashx?id=23\");";
		Optional<String> result = executor.executeAJAX(driver, jsCountrylInfo);
		String jsonStr;
		if(result.isPresent()) {
			jsonStr = result.get();
			System.out.println(jsonStr);
		}*/
		JsonObject jo = executor.executeAJAX(driver, "http://sg40.dipan.com/GateWay/City.ashx?id=23");
		System.out.println(jo.toString());
		System.out.println(jo.get("name"));
		JsonObject jo1 = executor.executeAJAX(driver, "http://sg40.dipan.com/GateWay/City.ashx?id=1");
		System.out.println(jo1.toString());
		JsonObject jo2 = executor.executeAJAX(driver, "http://sg40.dipan.com/GateWay/City.ashx?id=2");
		System.out.println(jo2.toString());
		JsonObject jo3 = executor.executeAJAX(driver, "http://sg40.dipan.com/GateWay/City.ashx?id=3");
		System.out.println(jo3.toString());
		JsonObject jo4 = executor.executeAJAX(driver, "http://sg40.dipan.com/GateWay/City.ashx?id=4");
		System.out.println(jo4.toString());
		JsonObject jo23 = executor.executeAJAX(driver, "http://sg40.dipan.com/GateWay/City.ashx?id=23");
		System.out.println(jo23.toString());
		JsonObject jo5 = executor.executeAJAX(driver, "http://sg40.dipan.com/GateWay/City.ashx?id=5");
		System.out.println(jo5.toString());
		JsonObject jo6 = executor.executeAJAX(driver, "http://sg40.dipan.com/GateWay/City.ashx?id=6");
		System.out.println(jo6.toString());
		JsonObject jo7 = executor.executeAJAX(driver, "http://sg40.dipan.com/GateWay/City.ashx?id=7");
		System.out.println(jo7.toString());
		JsonObject jo8 = executor.executeAJAX(driver, "http://sg40.dipan.com/GateWay/City.ashx?id=8");
		System.out.println(jo8.toString());
	}
}
