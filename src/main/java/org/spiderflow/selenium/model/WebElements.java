package org.spiderflow.selenium.model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class WebElements extends ArrayList<WebElement> {

    public WebElements() {
    }

    public WebElements(List<WebElement> elements) {
        super(elements == null ? Collections.emptyList() : elements);
    }

    public List<String> html(){
        return attr("innerHTML");
    }

    public List<String> attr(String attr){
        return this.stream().map(element->element.getAttribute(attr)).collect(Collectors.toList());
    }

    public List<String> text(){
        return attr("text");
    }

    public WebElements selectors(String css){
        WebElements elements = new WebElements();
        this.stream().map(element->element.findElements(By.cssSelector(css))).forEach(elements::addAll);
        return elements;
    }

    public WebElement selector(String css){
        for (WebElement element : this) {
            WebElement ele = element.findElement(By.cssSelector(css));
            if(ele != null){
                return ele;
            }
        }
        return null;
    }

    public WebElements xpaths(String xpath){
        WebElements elements = new WebElements();
        this.stream().map(element->element.findElements(By.xpath(xpath))).forEach(elements::addAll);
        return elements;
    }

    public WebElement xpath(String xpath){
        for (WebElement element : this) {
            WebElement ele = element.findElement(By.xpath(xpath));
            if(ele != null){
                return ele;
            }
        }
        return null;
    }
}
