//package com.zhz.selenium.controller;
//
//import cn.hutool.log.Log;
//import cn.hutool.log.LogFactory;
//import com.zhz.selenium.service.AmazonFileService;
//import com.zhz.selenium.util.StringUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@Component
//@EnableScheduling
//public class AmazonFileController {
//    Log log = LogFactory.get();
//
//    @Autowired
//    private AmazonFileService amazonFileService;
//
//    @Value("${grant_type}")
//    private String grantType;
//
//    @Value("${client_id}")
//    private String clientId;
//
//    @Value("${refresh_token}")
//    private String refreshToken;
//
//    @Value("${client_secret}")
//    private String clientSecret;
//
//    @Value("${Content-Type}")
//    private String contentType;
//
//    @Value("${Amazon-Advertising-API-Scope}")
//    private String amazonAdvertisingAPIScope;
//
//    @Value("${Amazon-Advertising-API-ClientId}")
//    private String amazonAdvertisingAPIClientId;
//
//    @Value("${searchFields}")
//    private String searchFields;
//
//    @Value("${amzFields}")
//    private String amzFields;
//
//    @RequestMapping(value = "/importSearchTermFile")
//    @Scheduled(cron="0 0,30 21 * * ?")
//    public void importSearchTermFile() throws InterruptedException {
//        Map<Object, Object> map = new HashMap<>();
//        amazonFileService.getAccessToken(grantType,clientId,refreshToken,clientSecret,map);
//        doTimeout(map);
//        String reportId = amazonFileService.getSearchTermReportId(contentType, amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
//                , "Bearer " + map.get("access_token"), searchFields);
//        doTimeout(map);
//        log.info("report id = "+reportId);
//        if (!StringUtil.isEmpty(reportId)){
//            amazonFileService.getSearchTermReport(contentType,amazonAdvertisingAPIScope,amazonAdvertisingAPIClientId
//                    , "Bearer " + map.get("access_token"),reportId);
//        }
//
//    }
//
//    @RequestMapping(value = "/importAmzFile")
//    @Scheduled(cron="0 0 21 * * ?")
//    public void importAmzFile() throws InterruptedException {
//        Map<Object, Object> map = new HashMap<>();
//        amazonFileService.getAccessToken(grantType,clientId,refreshToken,clientSecret,map);
//        doTimeout(map);
//        String reportId = amazonFileService.getAmzReportId(contentType, amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
//                , "Bearer " + map.get("access_token"), amzFields);
//        doTimeout(map);
//        log.info("report id = "+reportId);
//        if (!StringUtil.isEmpty(reportId)){
//            amazonFileService.getAmzReport(contentType,amazonAdvertisingAPIScope,amazonAdvertisingAPIClientId
//                    , "Bearer " + map.get("access_token"),reportId);
//        }
//    }
//
//    @RequestMapping(value = "/importCampaignFile")
//    @Scheduled(cron="0 0 22 * * ?")
//    public void importCampaignFile() throws InterruptedException {
//        Map<Object, Object> map = new HashMap<>();
//        amazonFileService.getAccessToken(grantType,clientId,refreshToken,clientSecret,map);
//        doTimeout(map);
//        amazonFileService.getCampaignReport(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
//                , "Bearer " + map.get("access_token"));
//    }
//
//    @RequestMapping(value = "/importAdgroupFile")
//    @Scheduled(cron="0 0,30 22 * * ?")
//    public void importAdgroupFile() throws InterruptedException {
//        Map<Object, Object> map = new HashMap<>();
//        amazonFileService.getAccessToken(grantType,clientId,refreshToken,clientSecret,map);
//        doTimeout(map);
//        amazonFileService.getAdgroupReport(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
//                , "Bearer " + map.get("access_token"));
//    }
//
//    @RequestMapping(value = "/importNegativeKeyWordFile")
//    @Scheduled(cron="0 0,30 23 * * ?")
//    public void importNegativeKeyWordFile() throws InterruptedException {
//        Map<Object, Object> map = new HashMap<>();
//        amazonFileService.getAccessToken(grantType,clientId,refreshToken,clientSecret,map);
//        doTimeout(map);
//        amazonFileService.getNegativeKeyWordReport(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
//                , "Bearer " + map.get("access_token"));
//    }
//
//    @RequestMapping(value = "/importPortfoliosFile")
//    @Scheduled(cron="0 0 23 * * ?")
//    public void importPortfoliosFile() {
//        Map<Object, Object> map = new HashMap<>();
//        amazonFileService.getAccessToken(grantType,clientId,refreshToken,clientSecret,map);
//        doTimeout(map);
//        amazonFileService.getImportPortfoliosReport(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
//                , "Bearer " + map.get("access_token"));
//    }
//
//
//    private void doTimeout(Map<Object, Object> map){
//        if ((Long)map.get("endTime")-new Date().getTime()<=0){
//            amazonFileService.getAccessToken(grantType,clientId,refreshToken,clientSecret,map);
//        }
//    }
//
//}
