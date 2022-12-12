package com.zhz.selenium.service;

import com.zhz.selenium.pojo.AutoCampaign;
import com.zhz.selenium.pojo.CreateCampaign;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface AmazonAdAutoService {
    void createNegativeKeywords(String amazonAdvertisingAPIScope,String amazonAdvertisingAPIClientId
            ,String token,Long campaignId,Long adGroupId,String keywordText,String rule);

    void createNegativeAsin(String amazonAdvertisingAPIScope,String amazonAdvertisingAPIClientId
            ,String token,Long campaignId,Long adGroupId,String asin,String rule);

    void updateCampaignStatus(String amazonAdvertisingAPIScope,String amazonAdvertisingAPIClientId
            ,String token,Long campaignId,String rule);

    void updateAdgroupStatus(String amazonAdvertisingAPIScope,String amazonAdvertisingAPIClientId
            ,String token,Long adGroupId,String rule);

    void updateKeywordBid(String amazonAdvertisingAPIScope,String amazonAdvertisingAPIClientId
            ,String token,Long keywordId,String rule, BigDecimal bid);

    void createKeyword(String amazonAdvertisingAPIScope,String amazonAdvertisingAPIClientId
            ,String token,Long campaignId,Long adGroupId,String rule, String matchType,String keyWord,BigDecimal bid);

    String createAdGroup(String amazonAdvertisingAPIScope,String amazonAdvertisingAPIClientId
            ,String token,Long campaignId,String rule,String name,Double bid,String state);

    String createCampaign(String amazonAdvertisingAPIScope, String amazonAdvertisingAPIClientId, String token
            , String rule,CreateCampaign createCampaign,String campaignName) throws ParseException;

    String createCampaign(String amazonAdvertisingAPIScope, String amazonAdvertisingAPIClientId, String token
            , String rule, AutoCampaign campaign, String campaignName) throws ParseException;

    void createProductAds(String amazonAdvertisingAPIScope, String amazonAdvertisingAPIClientId, String token, String rule
            , String sku,Long campaignId, Long adGroupId);

    void createTarget(String amazonAdvertisingAPIScope, String amazonAdvertisingAPIClientId, String token, String rule
            ,Long campaignId, Long adGroupId,String targetWord,BigDecimal bid);

    Map<String,List<Map<String,Object>>> dealNegativeKeywords(List<Map<String, Object>> results);

    List<Map<String, Object>> dealAsin(List<Map<String, Object>> results);

    String[] getKeyword(CreateCampaign campaign,int i);

    String getMatchType(CreateCampaign campaign,int i);

    BigDecimal getBid(CreateCampaign campaign,int i);
}
