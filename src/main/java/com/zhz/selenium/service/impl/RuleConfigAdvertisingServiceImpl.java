package com.zhz.selenium.service.impl;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.zhz.selenium.mapper.RuleConfigAdvertisingMapper;
import com.zhz.selenium.service.AmazonAdAutoService;
import com.zhz.selenium.service.AmazonFileService;
import com.zhz.selenium.service.RuleConfigAdvertisingService;
import com.zhz.selenium.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RuleConfigAdvertisingServiceImpl implements RuleConfigAdvertisingService {
    Log log = LogFactory.get();

    @Autowired
    private RuleConfigAdvertisingMapper ruleConfigAdvertisingMapper;

    @Autowired
    private AmazonAdAutoService amazonAdAutoService;

    @Autowired
    private AmazonFileService amazonFileService;

    @Value("${grant_type}")
    private String grantType;

    @Value("${client_id}")
    private String clientId;

    @Value("${refresh_token}")
    private String refreshToken;

    @Value("${client_secret}")
    private String clientSecret;

    @Value("${Amazon-Advertising-API-Scope}")
    private String amazonAdvertisingAPIScope;

    @Value("${Amazon-Advertising-API-ClientId}")
    private String amazonAdvertisingAPIClientId;


    @Override
    public List<Map<String, Object>> selectKeyWordAndNoOrder(String click,String startTime,String endTime,String ruleName) {
        try {
            List<Map<String, Object>> list = ruleConfigAdvertisingMapper.selectKeyWordAndNoOrder(click,startTime,endTime);
            return list;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }


    @Override
    public List<Map<String, Object>> selectImpressionAndCtrAndNoOrder(String date, Integer impression, String ctr,String ruleName) {
        try {
            List<Map<String, Object>> maps = ruleConfigAdvertisingMapper.selectImpressionAndCtrAndNoOrder(date, impression, ctr);
            return maps;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectClickAndCtrAndNoOrder(String click, String ctr,String startTime,String endTime,String ruleName) {
        try {
            List<Map<String, Object>> maps = ruleConfigAdvertisingMapper.selectClickAndCtrAndNoOrder(click, ctr,startTime,endTime);
            return  maps;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectSearchTermByAcos(String acos,String startTime,String endTime,String ruleName) {
        try {
            List<Map<String, Object>> maps = ruleConfigAdvertisingMapper.selectSearchTermByAcos(Double.valueOf(acos)/100.0,startTime,endTime);
            return maps;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectSearchTermByCtrAndClick(Double ctr, String click, Double conversionRate,String startTime
            ,String endTime,String ruleName) {
        try {
            List<Map<String, Object>> maps = ruleConfigAdvertisingMapper.selectSearchTermByCtrAndClick(ctr, click, conversionRate,startTime,endTime);
            return  maps;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectSearchTermByClickAndConrate(String click, Double conversionRate
            ,String startTime,String endTime,String ruleName) {
        try {
            List<Map<String, Object>> maps = ruleConfigAdvertisingMapper.selectSearchTermByClickAndConrate(click, conversionRate,startTime,endTime);
            return maps;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectAsinByClick(String click,String startTime,String endTime,String ruleName) {
        try {
            List<Map<String, Object>> maps = ruleConfigAdvertisingMapper.selectAsinByClick(click,startTime,endTime);
            return maps;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectStopAdvice1(Double percent,String startTime,String endTime,String ruleName) {
        try {
            List<Map<String, Object>> maps = ruleConfigAdvertisingMapper.selectStopAdvice1(percent / 100);
            return maps;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectStopAdvice2(Double percent,String startTime,String endTime,String ruleName) {
        try {
            List<Map<String, Object>> maps = ruleConfigAdvertisingMapper.selectStopAdvice2(percent / 100);
            return maps;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectStopAdvice3(Double percent,String startTime,String endTime,String ruleName) {
        try {
            List<Map<String, Object>> maps = ruleConfigAdvertisingMapper.selectStopAdvice3(percent / 100);
            return maps;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectStopAdvice4(Double percent,String startTime,String endTime,String ruleName) {
        try {
            List<Map<String, Object>> maps = ruleConfigAdvertisingMapper.selectStopAdvice4(percent / 100);
            return maps;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectStopAdvice5(Double percent,String startTime,String endTime,String ruleName) {
        try {
            List<Map<String, Object>> maps = ruleConfigAdvertisingMapper.selectStopAdvice5(percent / 100);
            return maps;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectStopAdvice6(Double percent,String startTime,String endTime,String ruleName) {
        try {
            List<Map<String, Object>> maps = ruleConfigAdvertisingMapper.selectStopAdvice6(percent / 100);
            return maps;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }


    @Override
    public List<Map<String, Object>> selectSearchTermByLowAcos(String acos,String startTime,String endTime,String ruleName) {
        try {
            List<Map<String, Object>> maps = ruleConfigAdvertisingMapper.selectSearchTermByLowAcos(acos,startTime,endTime);
            return maps;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    private void createAutoAdvertising(List<Map<String, Object>> resultList,String flag,String ruleName){
        Map<Object, Object> map = new HashMap<>();
        Map<String, List<Map<String, Object>>> resultsList = amazonAdAutoService.dealNegativeKeywords(resultList);
        amazonFileService.getAccessToken(grantType,clientId,refreshToken,clientSecret,map);
        doTimeout(map);
        switch(flag){
            case "createNegativeAsin" :
                for (Map.Entry result :resultsList.entrySet()) {
                    List<Map<String, Object>> value = (List)result.getValue();
                    for (Map<String, Object> m: value) {
                        amazonAdAutoService.createNegativeAsin(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
                            , "Bearer " + map.get("access_token"), Long.parseLong(StringUtil.toString(m.get("campaign_id")))
                            ,Long.parseLong(StringUtil.toString(m.get("ad_group_id"))),StringUtil.toString(m.get("Customer_Search_Term"))
                            ,ruleName);
                    }
                }
                break;
            case "createNegativeKeywords" :
                for (Map.Entry result :resultsList.entrySet()) {
                    List<Map<String, Object>> value = (List)result.getValue();
                    for (Map<String, Object> m: value) {
                        amazonAdAutoService.createNegativeKeywords(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
                                , "Bearer " + map.get("access_token"), Long.parseLong(StringUtil.toString(m.get("campaign_id")))
                                ,Long.parseLong(StringUtil.toString(m.get("ad_group_id"))),StringUtil.toString(m.get("Customer_Search_Term"))
                                ,ruleName);
                    }
                }
                break;
            default :
                break;
        }

    }

    private void doTimeout(Map<Object, Object> map){
        if ((Long)map.get("endTime")-new Date().getTime()<=0){
            amazonFileService.getAccessToken(grantType,clientId,refreshToken,clientSecret,map);
        }
    }
}
