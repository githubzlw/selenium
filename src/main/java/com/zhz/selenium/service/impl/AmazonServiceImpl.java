package com.zhz.selenium.service.impl;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.zhz.selenium.mapper.AmazonMapper;
import com.zhz.selenium.pojo.*;
import com.zhz.selenium.service.AmazonService;
import com.zhz.selenium.util.CfgPrefixPlatIds;
import com.zhz.selenium.util.WebDriverUtil;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AmazonServiceImpl implements AmazonService {

    @Autowired
    private CfgPrefixPlatIds cfgPrefixPlatIds;

    @Value("${search.page}")
    private Integer pageNum;

    @Autowired
    private AmazonMapper amazonMapper;

    @Autowired
    private WebDriverUtil webDriverUtil;

    @Override
    public void getResultBySearch(String date,List<KeyWord> keyWords,String userName,String passWord,List<MyAsin> keyWordProducts) throws InterruptedException {
        Log log = LogFactory.get();
        // 获取配置文件标签
        List<Map<String, String>> platIds = cfgPrefixPlatIds.getPlatIds();

        // 准备工作
        WebDriver webDriver = webDriverUtil.prepare();

        // 随便进一个搜索页面
        webDriverUtil.openWebDriver("https://www.amazon.com/s?k=cat+litter+box+self+cleaning&page=2&language=zh&crid=1D36IRPZPUTFU&qid=1661223937&sprefix=cat+litter+box+self+cleaning%2Caps%2C488&ref=sr_pg_2",webDriver);
//        webDriverUtil.openWebDriver("https://www.amazon.com/",webDriver);
//        webDriverUtil.openWebDriver("https://www.amazon.com/s?k=cat&crid=GBXXWB7HDQIV&sprefix=cat+%2Caps%2C628&ref=nb_sb_noss_2",webDriver);
        Thread.sleep(90000);
        // 登录 a-column a-span6 a-span-last a-text-right
        try {
            login(webDriver, userName,passWord);
        } catch (InterruptedException e) {
            log.info(e.getMessage());
        }

        for (KeyWord keyWord:keyWords){
            List<Integer> count = new ArrayList<>();
            List<Integer> normalCount = new ArrayList<>();
            List<Integer> notNormalCount = new ArrayList<>();
            Thread.sleep(60000);

            //插入搜索的主表内容
            Search search = new Search(keyWord.getId(), date, 0);
            amazonMapper.insertSearch(search);

            // 设置爬取多少页
            for (int i = 1; i <= pageNum; i++) {
                try {
                    // 搜索结果的集合
                    List<SearchResult> searchResult = new ArrayList<>();

                    // 分析结果的集合
                    List<Product> productResult = new ArrayList<>();

                    // 设置路径和浏览器路径
                    String url = "https://www.amazon.com/s?k="+keyWord.getKeyWord().replaceAll(" ","+")+"&page="+i+"&crid=1A15FZ4DH112&qid=1655432069&sprefix=phone%2Caps%2C428&ref=sr_pg_2";

                    // 开启链接
                    webDriverUtil.openWebDriver(url,webDriver);

                    for (Map entry : platIds) {
                        Thread.sleep(60000);
                        getSearchResult(webDriver,searchResult,search.getId(),keyWord.getId(),i
                                , (String) entry.get("promoteClass"), (String) entry.get("normalClass")
                                , (String) entry.get("productNameClass"), (String) entry.get("productUrlClass")
                                , (String) entry.get("productPriceClass"), (String) entry.get("productAsinClass")
                                , keyWordProducts, productResult, (String) entry.get("productPhotoClass")
                                , (String) entry.get("productStarArowClass"), (String) entry.get("productStarArowAttributeClass")
                                /*, (String) entry.get("productReviewNumClass")*/
                                ,count,normalCount,notNormalCount,keyWord.getKeyWord(),date);
                    }

                    if(searchResult.size()>0){
                        // 插入搜素的结果
                        amazonMapper.insertAmazonResult(searchResult);
                    }

                    if (productResult.size()>0){
                        // 插入分析的结果
                        amazonMapper.insertProduct(productResult);
                    }


                } catch (Exception e) {
                    log.info(e.getMessage());
                }
                keyWord.setLastSearchDate(date);
                keyWord.setSearchCount(keyWord.getSearchCount()+1);
            }
        }
        // 关闭链接
        webDriverUtil.closeWebDriver(webDriver);
        // 更新关键词
        amazonMapper.updateKeyWord(keyWords);
    }


    @Override
    public void login(WebDriver webDriver, String userName, String passWord) throws InterruptedException {
        Thread.sleep(60000);
        try {
            WebElement check = webDriver.findElement(By.className("a-text-right"));
            check.click();
        } catch (Exception e) {
        }
        Thread.sleep(60000);
        try {
            WebElement check = webDriver.findElement(By.id("g"));
            WebElement img = check.findElement(By.tagName("img"));
            img.click();
        } catch (Exception e) {
        }
        Thread.sleep(60000);
        WebElement element = webDriver.findElement(By.id("nav-link-accountList"));
        element.click();
        Thread.sleep(60000);
        WebElement userNameEle = webDriver.findElement(By.id("ap_email"));
        userNameEle.sendKeys(userName);
        Thread.sleep(60000);
        WebElement btnContinue = webDriver.findElement(By.id("continue"));
        btnContinue.click();
        Thread.sleep(60000);
        WebElement passWordEle = webDriver.findElement(By.id("ap_password"));
        passWordEle.sendKeys(passWord);
        Thread.sleep(60000);
        WebElement signInSubmit = webDriver.findElement(By.id("signInSubmit"));
        signInSubmit.click();
    }

    @Override
    public void click(WebDriver webDriver, String url) throws InterruptedException {
        Thread.sleep(30000);
        String js = "window.open('"+url+"');";
        ((JavascriptExecutor) webDriver).executeScript(js);
    }

    @Override
    public void insertKeyWord(String startTime,String endTime) {
        amazonMapper.insertKeyWord(startTime,endTime);
    }


    /**
     * 搜索结果
     */
    private void getSearchResult(WebDriver webDriver,List<SearchResult> result,Integer searchId,Integer keyWordId,Integer page
            ,String promoteClass,String normalClass,String productNameClass,String productUrlClass,String productPriceClass,String productAsinClass
            ,List<MyAsin> keyWordProducts,List<Product> productResult, String productPhotoClass, String productStarArowClass
            , String productStarArowAttributeClass/*, String productReviewNumClass*/, List<Integer> count, List<Integer> normalCount, List<Integer> notNormalCount
            ,String keyWord,String date) throws InterruptedException {
        Thread.sleep(25000);
        //总体搜索DIV
        WebElement element = webDriver.findElement(By.className("s-main-slot"));

        List<WebElement> elements = element.findElements(By.tagName("div"));

        for (WebElement webElement : elements) {
            String className = webElement.getAttribute("class");
            //过滤找到商品的className
            if (!promoteClass.equalsIgnoreCase(className)
                    && !normalClass.equalsIgnoreCase(className)){
                continue;
            }

            SearchResult searchResult = new SearchResult();
            searchResult.setAmzProductDel(1);
            count.add(1);

            // 是否推广
            if (promoteClass.equalsIgnoreCase(className)){
                searchResult.setAmzProductStyle(1);
                notNormalCount.add(1);
                searchResult.setNotNormalCount(notNormalCount.size());

            }else {
                searchResult.setAmzProductStyle(0);
                normalCount.add(1);
                searchResult.setNormalCount(normalCount.size());
            }

            try {
                // 图片
                WebElement photoEle = webElement.findElement(By.className(productPhotoClass));
                String photoSrc = photoEle.getAttribute("src");
                searchResult.setAmzProductImg(photoSrc);
            } catch (Exception e) {
            }

            try {
                // 星数
                WebElement aRowEle = webElement.findElement(By.className(productStarArowClass));
                List<WebElement> list = aRowEle.findElements(By.tagName("span"));
                if (list.size()>0){
                    String attribute = list.get(0).getAttribute(productStarArowAttributeClass);
                    searchResult.setAmzProductStar(attribute);
                }
                if (searchResult.getAmzProductStar() != null){
                    if (list.size()>1){
                        String reviewNum = list.get(3).getAttribute(productStarArowAttributeClass);
                        searchResult.setAmzProductReviewNum(reviewNum);
                    }
                }
            } catch (Exception e) {
            }

            try {
                // 商品名
                WebElement productNameEle = webElement.findElement(By.className(productNameClass));
                String productName = productNameEle.getText();
                searchResult.setAmzProductName(productName);
            } catch (Exception e) {
            }

            try {
                // 商品url
                WebElement productUrlEle = webElement.findElement(By.className(productUrlClass));
                String productUrl = productUrlEle.getAttribute("href");
//                productDetail(webDriver,productUrl);
                searchResult.setAmzUrl(productUrl);
            } catch (Exception e) {
            }

            try {
                // 商品价格
                WebElement productPriceEle = webElement.findElement(By.className(productPriceClass));
                String productPrice = productPriceEle.getText().replace("\n",".");
                searchResult.setAmzProductPrice(productPrice);
                try {
                    searchResult.setAmzDoublePrice(productPriceEle.getText().replace("\n",".").replace("$","").replace("US","").replace(",",""));
                } catch (Exception e) {
                }
            } catch (Exception e) {
            }
            String dataAsin = null;

            try {
                // 商品asin
                dataAsin = webElement.getAttribute(productAsinClass);
                searchResult.setAmzAsin(dataAsin);
            } catch (Exception e) {
            }

            searchResult.setSearchId(searchId);
            searchResult.setKeyWordId(keyWordId);
            searchResult.setAmzProductPage(page);
            searchResult.setAmzProductIndex(result.size()+1);
            searchResult.setCount(count.size());
            searchResult.setSearchDate(date);


            // 设置结果分析表的内容，如果asin相同则记录位置并插入
            for (MyAsin keyWordProduct:keyWordProducts) {
                if (keyWordProduct.getKeyWord().equals(keyWord) && StringUtils.isNotEmpty(dataAsin) && dataAsin.equals(keyWordProduct.getAsin())){
                    Product product = new Product();
                    product.setKeyWordId(keyWordId);
                    product.setProductAsin(dataAsin);
                    product.setProductPage(page);
                    product.setProductIndex(result.size()+1);
                    product.setSearchId(searchId);
                    product.setProductState(searchResult.getAmzProductStyle());
                    product.setCount(count.size());
                    product.setNormalCount(searchResult.getNormalCount());
                    product.setNotNormalCount(searchResult.getNotNormalCount());
                    product.setKeyWord(keyWord);
                    product.setSearchDate(date);
                    product.setProductImg(searchResult.getAmzProductImg());
                    productResult.add(product);
                }
            }
            result.add(searchResult);
        }
    }

    private void productDetail(WebDriver webDriver,String productUrl) throws InterruptedException {
        click(webDriver,productUrl);
    }
}
