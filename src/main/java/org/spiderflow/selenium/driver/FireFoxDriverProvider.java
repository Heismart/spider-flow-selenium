package org.spiderflow.selenium.driver;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FireFoxDriverProvider implements DriverProvider {

    @Value("${selenium.driver.firefox:null}")
    private String firefoxDriverPath;

    @Override
    public String support() {
        return firefoxDriverPath != null ? "firefox" : null;
    }

    @Override
    public WebDriver getWebDriver(List<String> arguments, String proxyStr) {
        System.setProperty("webdriver.gecko.driver", firefoxDriverPath);
        FirefoxOptions options = new FirefoxOptions();
        if (arguments != null) {
            options.addArguments(arguments);
        }
        if (StringUtils.isNotBlank(proxyStr)) {
            Proxy proxy = new Proxy();
            proxy.setHttpProxy(proxyStr);
            options.setProxy(proxy);
        }
        return new FirefoxDriver(options);
    }
}
