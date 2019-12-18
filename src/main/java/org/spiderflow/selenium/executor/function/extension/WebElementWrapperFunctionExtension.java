package org.spiderflow.selenium.executor.function.extension;

import org.spiderflow.executor.FunctionExtension;
import org.springframework.stereotype.Component;

@Component
public class WebElementWrapperFunctionExtension implements FunctionExtension {

    @Override
    public Class<?> support() {
        return WebElementWrapper.class;
    }

    public static WebElementWrapper clickAndHold(WebElementWrapper element) {
        element.action().clickAndHold(element.element());
        return element;
    }

    public static WebElementWrapper release(WebElementWrapper element) {
        element.action().release(element.element());
        return element;
    }

    public static WebElementWrapper move(WebElementWrapper element, int x, int y) {
        element.action().moveToElement(element.element(),x, y);
        return element;
    }

    public static WebElementWrapper move(WebElementWrapper element) {
        element.action().moveToElement(element.element());
        return element;
    }

    public static WebElementWrapper doubleClick(WebElementWrapper element) {
        element.action().doubleClick(element.element());
        return element;
    }

    public static WebElementWrapper pause(WebElementWrapper element,int pause) {
        element.action().pause(pause);
        return element;
    }

    public static WebElementWrapper perform(WebElementWrapper element){
        element.action().perform();
        element.clear();
        return element;
    }
}
