package com.zhz.selenium.util;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.http.ClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
public class WebDriverUtil {

    @Value("${url}")
    private String url;

    public WebDriver prepare(){
//        String browserUrl = "C:\\Program Files\\Google\\Chrome\\Application";
        String browserUrl = url;
        System.getProperties().setProperty("webdriver.chrome.driver",browserUrl+"\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NONE);
        WebDriver webDriver = new ChromeDriver(options);
        webDriver.manage().window().maximize();
//        webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.MINUTES);
        return webDriver;
    }

    public void openWebDriver(String url,WebDriver webDriver){
        //开启webDriver进程
        webDriver.get(url);
    }

    public void closeWebDriver(WebDriver webDriver){
        webDriver.close();
        webDriver.quit();
    }

}
