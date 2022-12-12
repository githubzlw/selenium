package com.zhz.selenium.test;

import com.zhz.selenium.pojo.SearchResult;
import com.zhz.selenium.util.WebDriverUtil;
import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class test2 {
    public static void main(String[] args) throws InterruptedException {
        WebDriverUtil webDriverUtil = new WebDriverUtil();
//        ChromeOptions options = new ChromeOptions();
//        // 设置代理ip
//        String ip = "ip:port";
//        options.addArguments("--proxy-server=http://" + ip);


        System.getProperties().setProperty("webdriver.chrome.driver","C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");

        //开启webDriver进程
//        WebDriver webDriver = new ChromeDriver(options);
//        WebDriver webDriver = new ChromeDriver();
        ChromeOptions options = new ChromeOptions();
//        options.setPageLoadStrategy(PageLoadStrategy.NONE);
        WebDriver webDriver = new ChromeDriver(options);
//        WebDriver webDriver = webDriverUtil.prepare();
//        webDriver.manage().window().maximize();
//        webDriver.manage().timeouts().implicitlyWait(60, TimeUnit.MINUTES);

        String url = "https://www.amazon.com/";
//        String url = "https://www.amazon.com/s?k=gaming+keyboard&language=zh&pd_rd_r=7bd53818-9baa-4cc0-88e6-8d62a28febd2&pd_rd_w=NGETF&pd_rd_wg=pidUp&pf_rd_p=971294fa-7a1b-4a02-89ed-49f0f15a6df4&pf_rd_r=NGRWG6WR6AKJ5VBSWEPS&ref=pd_gw_unk";
//        webDriver.get("https://www.amazon.com/-/zh/Sliding-Unfinished-Environmental-Installation-K-Frame/product-reviews/B07QFPZRN6/ref=cm_cr_arp_d_viewopt_srt?ie=UTF8&reviewerType=all_reviews&sortBy=recent&pageNumber=1");
        Thread.sleep(30000);
        webDriverUtil.openWebDriver(url,webDriver);
//        webDriver.get(url);

//        List<Map<String,Object>> result = new ArrayList<>();
//        getSourceByEvaluation(webDriver,result);
//        List<SearchResult> result = new ArrayList<>();
//        getSourceBySearch(webDriver,result,1,1,1);
        login(webDriver);
//        click(webDriver);


//        System.out.println(result);
        //关闭webDriver进程
//        webDriver.close();
//        webDriver.quit();
    }

    /**
     * 商品评价页面
     * @param webDriver
     * @param result
     */
    private static void getSourceByEvaluation(WebDriver webDriver,List<Map<String,Object>> result) {
        String source = webDriver.getPageSource();
        //总体评论DIV
        WebElement element = webDriver.findElement(By.id("cm_cr-review_list"));
        List<WebElement> elements = element.findElements(By.tagName("div"));
        for (WebElement webElement : elements) {
            String className = webElement.getAttribute("class");
            //过滤找到对应评论列表
            if (!"a-section review aok-relative".equalsIgnoreCase(className)){
                continue;
            }
            Map<String, Object> map = new HashMap<>();
            //对应评论信息
            WebElement userNameEle = webElement.findElement(By.className("a-profile-name"));
            map.put("用户",userNameEle.getText());

            WebElement aRowEle = webElement.findElement(By.className("a-row"));
            WebElement starNumEle = aRowEle.findElement(By.className("a-link-normal"));
            map.put("星数",starNumEle.getAttribute("title"));

            List<WebElement> list = aRowEle.findElements(By.tagName("a"));
            WebElement starLevelEle = list.stream().filter(ele -> ele.getAttribute("class").equalsIgnoreCase("a-size-base a-link-normal review-title a-color-base review-title-content a-text-bold")).collect(Collectors.toList()).get(0);
            map.put("星级",starLevelEle.getText());

            WebElement creatMessEle = webElement.findElements(By.tagName("span")).stream().filter(ele->ele.getAttribute("class").equalsIgnoreCase("a-size-base a-color-secondary review-date")).collect(Collectors.toList()).get(0);
            map.put("评论时间",creatMessEle.getText());

            WebElement contentEle = webElement.findElements(By.tagName("div")).stream().filter(ele->ele.getAttribute("class").equalsIgnoreCase("a-row a-spacing-small review-data")).collect(Collectors.toList()).get(0);
            map.put("评论",contentEle.getText());
            result.add(map);
            System.out.println("--------------------------------------");
        }
    }

    /**
     * 搜索页面
     */
    private static void getSourceBySearch(WebDriver webDriver,List<SearchResult> result,Integer searchId,Integer keyWordId,Integer page) {
        String source = webDriver.getPageSource();
        //总体搜索DIV
        WebElement element = webDriver.findElement(By.className("s-main-slot"));

        List<WebElement> elements = element.findElements(By.tagName("div"));

        for (WebElement webElement : elements) {
            String className = webElement.getAttribute("class");
            //过滤找到商品的className
            if (!"s-result-item s-asin sg-col-0-of-12 sg-col-16-of-20 AdHolder sg-col s-widget-spacing-small sg-col-12-of-16".equalsIgnoreCase(className)
                    && !"s-result-item s-asin sg-col-0-of-12 sg-col-16-of-20 sg-col s-widget-spacing-small sg-col-12-of-16".equalsIgnoreCase(className)){
                continue;
            }

            SearchResult searchResult = new SearchResult();

            // 是否推广
            if ("s-result-item s-asin sg-col-0-of-12 sg-col-16-of-20 AdHolder sg-col s-widget-spacing-small sg-col-12-of-16".equalsIgnoreCase(className)){
                searchResult.setAmzProductStyle(1);
            }

            // 图片
            WebElement photoEle = webElement.findElement(By.className("s-image"));
            String photoSrc = photoEle.getAttribute("src");

            // 星数
            WebElement aRowEle = webElement.findElement(By.className("a-size-small"));
            List<WebElement> list = aRowEle.findElements(By.tagName("span"));
            if (list.size()>0){
                String attribute = list.get(0).getAttribute("aria-label");
            }

            // review数量
            WebElement numEle = webElement.findElement(By.className("a-size-base"));
            String reviewNum = numEle.getText();

            // 商品名
            WebElement productNameEle = webElement.findElement(By.className("a-size-medium"));
            String productName = productNameEle.getText();
            searchResult.setAmzProductName(productName);

            // 商品url
            WebElement productUrlEle = webElement.findElement(By.className("a-link-normal"));
            String productUrl = productUrlEle.getAttribute("href");
            searchResult.setAmzUrl(productUrl);

            try {
                // 商品价格
                WebElement productPriceEle = webElement.findElement(By.className("a-price"));
                String productPrice = productPriceEle.getText().replace("\n",".");
                searchResult.setAmzProductPrice(productPrice);
            } catch (Exception e) {
            }

            // 商品asin
            String dataAsin = webElement.getAttribute("data-asin");
            searchResult.setAmzAsin(dataAsin);

            searchResult.setSearchId(searchId);
            searchResult.setKeyWordId(keyWordId);
            searchResult.setAmzProductPage(page);
            searchResult.setAmzProductIndex(result.size()+1);
            result.add(searchResult);
        }
    }


    private static void login(WebDriver webDriver) throws InterruptedException {
        Thread.sleep(30000);
        WebElement element = webDriver.findElement(By.id("nav-link-accountList"));
        element.click();
        Thread.sleep(20000);
        WebElement userNameEle = webDriver.findElement(By.id("ap_email"));
        userNameEle.sendKeys("1975868046@qq.com");
        WebElement checkBox = webDriver.findElement(By.id("ap_legal_agreement_check_box"));
        checkBox.click();
        Thread.sleep(20000);
        WebElement btnContinue = webDriver.findElement(By.id("continue"));
        btnContinue.click();
        Thread.sleep(20000);
        WebElement passWordEle = webDriver.findElement(By.id("ap_password"));
        passWordEle.sendKeys("bbq135..");
        Thread.sleep(20000);
        WebElement signInSubmit = webDriver.findElement(By.id("signInSubmit"));
        signInSubmit.click();

    }

    // 如果asin和某个asin相同则点击
    private static void click(WebDriver webDriver) throws InterruptedException {
        Thread.sleep(30000);
        String url = "https://www.amazon.com/-/zh/dp/B09V87YTC9/ref=sr_1_3?__mk_zh_CN=%E4%BA%9A%E9%A9%AC%E9%80%8A%E7%BD%91%E7%AB%99&crid=2DBV8646QH65P&keywords=phone&qid=1655430253&sprefix=phone%2Caps%2C356&sr=8-3";
        String js = "window.open('"+url+"');";
        ((JavascriptExecutor) webDriver).executeScript(js);
    }

}
