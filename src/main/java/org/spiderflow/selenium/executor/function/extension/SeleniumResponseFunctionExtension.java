package org.spiderflow.selenium.executor.function.extension;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.spiderflow.annotation.Comment;
import org.spiderflow.annotation.Example;
import org.spiderflow.executor.FunctionExtension;
import org.spiderflow.selenium.io.SeleniumResponse;
import org.spiderflow.selenium.model.WebElements;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SeleniumResponseFunctionExtension implements FunctionExtension {

    @Override
    public Class<?> support() {
        return SeleniumResponse.class;
    }

    @Comment("根据css选择器提取请求结果")
    @Example("${resp.selector('div > a')}")
    public static WebElementWrapper selector(SeleniumResponse response, String css) {
        try {
            return new WebElementWrapper(response, response.getDriver().findElement(By.cssSelector(css)));
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Comment("根据css选择器提取请求结果")
    @Example("${resp.selector('div > a')}")
    public static WebElements selectors(SeleniumResponse response, String css) {
        try {
            return new WebElements(response, response.getDriver().findElements(By.cssSelector(css)));
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Comment("根据xpath在请求结果中查找")
    @Example("${resp.xpaths('//a')}")
    public static WebElements xpaths(SeleniumResponse response, String xpath) {
        try {
            return new WebElements(response, response.getDriver().findElements(By.xpath(xpath)));
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Comment("根据xpath在请求结果中查找")
    @Example("${resp.xpath('//a')}")
    public static WebElement xpath(SeleniumResponse response, String xpath) {
        try {
            return new WebElementWrapper(response, response.getDriver().findElement(By.xpath(xpath)));
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Comment("执行js")
    @Example("${resp.executeScript(\"document.write('hello spider-flow !')\")}")
    public static Object executeScript(SeleniumResponse response, String script) {
        return executeScript(response, script, null);
    }

    @Comment("执行js")
    @Example("${resp.executeScript(\"document.write('hello '+arguments[0]+' !')\",\"spider-flow\")}")
    public static Object executeScript(SeleniumResponse response, String script, List<Object> arguments) {
        JavascriptExecutor executor = null;
        try {
            executor = (JavascriptExecutor) response.getDriver();
        } catch (Throwable e) {
            throw new RuntimeException("该驱动不支持执行js");
        }
        return executor.executeScript(script, arguments);
    }

}
