package org.spiderflow.selenium.io;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.spiderflow.io.SpiderResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SeleniumResponse implements SpiderResponse{
	
	private WebDriver driver;
	
	public SeleniumResponse(WebDriver driver){
		this.driver = driver;
	}
	
	@Override
	public int getStatusCode() {
		return 0;
	}

	@Override
	public String getTitle() {
		return driver.getTitle();
	}

	@Override
	public byte[] getBytes() {
		return null;
	}
	
	@Override
	public String getHtml() {
		return driver.getPageSource();
	}
	
	@Override
	public Map<String, String> getCookies() {
		Set<Cookie> cookies = driver.manage().getCookies();
		Map<String,String> cookieMap = new HashMap<>();
		if(cookies != null){
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie.getValue());
			}
		}
		return cookieMap;
	}
	
	@Override
	public Map<String, String> getHeaders() {
		return null;
	}

	@Override
	public String getContentType() {
		return null;
	}

	@Override
	public String getUrl() {
		return driver.getCurrentUrl();
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void quit(){
		try {
			driver.quit();
		} catch (Exception ignored) {
		}
	}
}
