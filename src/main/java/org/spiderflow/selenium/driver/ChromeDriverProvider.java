package org.spiderflow.selenium.driver;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChromeDriverProvider implements DriverProvider {

    @Value("${selenium.driver.chrome:null}")
    private String chromeDriverPath;

    @Override
    public String support() {
        return chromeDriverPath != null ? "chrome" : null;
    }

    @Override
    public WebDriver getWebDriver(List<String> arguments, String proxyStr) {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        ChromeOptions options = new ChromeOptions();
        if (arguments != null) {
            options.addArguments(arguments);
        }
        if (StringUtils.isNotBlank(proxyStr)) {
            Proxy proxy = new Proxy();
            proxy.setHttpProxy(proxyStr);
            options.setProxy(proxy);
        }
        return new ChromeDriver(options);
    }
}
