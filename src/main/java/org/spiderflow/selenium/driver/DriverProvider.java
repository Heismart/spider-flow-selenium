package org.spiderflow.selenium.driver;

import org.openqa.selenium.WebDriver;

import java.util.List;

public interface DriverProvider {

    public String support();

    public WebDriver getWebDriver(List<String> arguments, String proxyStr);
}
