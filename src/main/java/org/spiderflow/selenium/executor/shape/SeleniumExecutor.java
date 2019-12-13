package org.spiderflow.selenium.executor.shape;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spiderflow.ExpressionEngine;
import org.spiderflow.context.SpiderContext;
import org.spiderflow.executor.ShapeExecutor;
import org.spiderflow.model.Shape;
import org.spiderflow.model.SpiderNode;
import org.spiderflow.selenium.driver.DriverProvider;
import org.spiderflow.selenium.io.SeleniumResponse;
import org.spiderflow.selenium.utils.SeleniumResponseHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class SeleniumExecutor implements ShapeExecutor {

    public static final String DRIVER_TYPE = "driverType";

    public static final String JAVASCRIPT_ENABLED = "javascriptEnabled";

    public static final String URL = "url";

    public static final String DRIVER_VAR_NAME = "$$$driver";

    public static final String PAGE_LOAD_TIMEOUT = "pageLoadTimeout";

    public static final String IMPLICITLY_WAIT_TIMEOUT = "implicitlyWaitTimeout";

    public static final String PROXY = "proxy";

    @Autowired
    private List<DriverProvider> driverProviders;

    private Map<String, DriverProvider> providerMap;

    @Autowired
    private ExpressionEngine engine;

    private static Logger logger = LoggerFactory.getLogger(SeleniumExecutor.class);

    @PostConstruct
    private void init() {
        providerMap = driverProviders.stream().filter(provider -> provider.support() != null).collect(Collectors.toMap(DriverProvider::support, value -> value));
    }

    @Override
    public Shape shape() {
        Shape shape = new Shape();
        shape.setImage("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAEnklEQVRYR7WXb0xbVRTAz7ktyLbyR9wGBMbKkDHjkE6gWwKBYrJlmk1xkcyone2iJiZzDjWL2RdZ4hejGZiQOOOHlpiZkGWkSCAL0fHaUGIIc3UDN3AITGpH9q9FdPxZ3zG3paOF1/ceTt7Hd88953fOPefccxFW+A3XmAyiCFUILA0B0ghBDwQeroYRCQXt3c6VqEQ1wtwoEXsfgGoAME3FHjtDsbnAIQhKsrIAV2tMeiS0IaBJSZHUOgEJhGR9yiGMxdsfF2B4/3MWYtSg0mMZPvKjiHVb2y/YpYQkAYZerLYD4pv/xWuZPfbCtgvWpevLAFbJeMTuMogYgHDYwfY/ex6jDkWwRh/HQ4BMm0XvdNzg5ZS6mgBct4hiXiQxowDMQs3INNZdCVSuNgCvjm1t3dXcTggg0/aGCZF1E1GgvcOHKfNiSjwITUaWN/2946Ns0+YixlgoWvRgKpDo77yScKslD1HMVuMAA9qBRsGzAHDIjgihrDdMzrgae29LRiHZ/JZLt2dfpc8P3vMD7PqlG5i0JgHmt2VR8JXS4I7kJExZM3rUzWbHy5UhqFljFCwLAGY/Ij48+yZhcnj7vfmt0Uoixm09zNU1CMUAi/JhOQrUltLAgRIqTxo/4dLcH1Q4SvJrjMLjGAl/jLG5oKe9w2eI/ONh3/jFV9mtF9F9tp/JerfnaXJZK8TKtVdf9iodB0Oqxkyb2YKIy0rvw5/v9O0fv2/kEOs/bXDPZOiL3mnWxs2NRQcocOqgOJ37oGUs8fZ38keBVIeZtkP1iPDJ0jNjIk04On0pPCEzmlunmnu1nq5BVFUhPAqWXf5i3XWzfEkTnYwLwIGq/vjHebL/blXWtw44cob13ZnGUESUvtx0cH9WGyxfd+0leVElAF6WZ7puTpd9fS57JQBP6Kiv6XXR+MgAHH/LvTl3h/V00UqP4LBxIn/t7+/K9wQegSyb+RggNsjF6oeqj11aTc6WD1pYCgAqJuKpg0Gv6iTcYHvNoEXtJTkAQ7p+qMX0UWHrRew5288q5GRrS0U37wWqypB3w3Arjm1EUgYajVbn8zklVbYe5uwaxKrlMjRVW0qXD5RQxZ+jn3cWzPa8oJCsAY2xm8+VHGCxFctt+nLnYWFv9rMmnx8mzg+wkWs+1HB53or3bhfzs9Ig5/vRrh9fnWkqje6s0jpjWrFFjyiOKpUXX8/VbZho2vX2WIEu8xnGeE4AiKI49dv0zctHfvpGfyL115F9qTMSEYrVzhIoD3cIYzHXMaJUaNVghWU2J8553U9OKt6GRNSm3SnU8D0xAwlA0KMcuvhAvfnevtzHRKVmFWAJZODexwAsJKPkvaAmBruT//7FtulusaIsUp2mTGiMyC0bStUm5FJDQ4UTw+s0FHOFS1RKaAaI/i85lq8U4uh6v/v4xr8UhpBw1i+Fivsw4R2SAOqVciKFBacGC70kIxcgoGNao6D+YRKh5JMyQNAuVx2ns28545cdNbMEqI8knFR+qHqchkHEev44jfY0TtkFAMjBEOxY9oiPUyni0N0BGhMBptlzJ5N262ZnQnIM/AzAo8ZotN5/AYJq57vIypCyAAAAAElFTkSuQmCC");
        shape.setName("selenium");
        shape.setTitle("Selenium");
        shape.setLabel("Selenium");
        return shape;
    }

    @Override
    public String supportShape() {
        return "selenium";
    }

    @Override
    public void execute(SpiderNode node, SpiderContext context, Map<String, Object> variables) {
        String proxy = node.getStringJsonValue(PROXY);
        String driverType = node.getStringJsonValue(DRIVER_TYPE);
        if (StringUtils.isBlank(driverType) || !providerMap.containsKey(driverType)) {
            context.error("找不到驱动：{}", driverType);
            return;
        }
        if (StringUtils.isNotBlank(proxy)) {
            try {
                proxy = engine.execute(proxy, variables).toString();
                context.info("设置代理：{}", proxy);
            } catch (Exception e) {
                context.error("设置代理出错，异常信息：{}", e);
            }
        }
        List<Map<String, String>> argumentMap = node.getListJsonValue("argument");
        List<String> arguments = argumentMap.stream().map(m -> m.get("argument")).collect(Collectors.toList());
        WebDriver driver = null;
        try {
            String url = engine.execute(node.getStringJsonValue(URL), variables).toString();
            context.info("设置请求url:{}", url);
            driver = providerMap.get(driverType).getWebDriver(arguments, proxy);
            driver.manage().timeouts().pageLoadTimeout(NumberUtils.toInt(node.getStringJsonValue(PAGE_LOAD_TIMEOUT), 60 * 1000), TimeUnit.MILLISECONDS);
            driver.manage().timeouts().implicitlyWait(NumberUtils.toInt(node.getStringJsonValue(IMPLICITLY_WAIT_TIMEOUT), 3 * 1000), TimeUnit.MILLISECONDS);
            driver.get(url);
            SeleniumResponse response = new SeleniumResponse(driver);
            SeleniumResponseHolder.add(context, response);
            Object oldResp = variables.get("resp");

            //TODO 会造成其他线程无法使用resp,但是也不能长期不释放...综合考虑在请求时，如果变量中有SeleniumResponse，就释放掉之前的driver
            if(oldResp instanceof SeleniumResponse){
                SeleniumResponse oldResponse = (SeleniumResponse) oldResp;
                oldResponse.quit();
            }
            variables.put("resp", response);
        } catch (Exception e) {
            context.error("请求出错，异常信息：{}", e);
            if (driver != null) {
                try {
                    driver.quit();
                } catch (Exception ignored) {
                }
            }
            ExceptionUtils.wrapAndThrow(e);
        }
    }

}
