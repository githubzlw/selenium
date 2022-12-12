package com.zhz.selenium.service;

import com.zhz.selenium.pojo.KeyWord;
import com.zhz.selenium.pojo.MyAsin;
import org.openqa.selenium.WebDriver;

import java.util.List;

public interface AmazonService {
    void getResultBySearch(String date, List<KeyWord> keyWords, String userName, String passWord,List<MyAsin> keyWordProducts) throws InterruptedException;

    void login(WebDriver webDriver,String userName,String passWord) throws InterruptedException;

    void click(WebDriver webDriver,String url) throws InterruptedException;

    void insertKeyWord(String startTime,String endTime);

}
