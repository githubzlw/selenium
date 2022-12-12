package com.zhz.selenium.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.hash.Hash;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.zhz.selenium.mapper.AmazonAdAutoMapper;
import com.zhz.selenium.pojo.AutoCampaign;
import com.zhz.selenium.pojo.CreateCampaign;
import com.zhz.selenium.service.AmazonAdAutoService;
import com.zhz.selenium.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AmazonAdAutoServiceImpl implements AmazonAdAutoService {


    @Autowired
    private AmazonAdAutoMapper amazonAdAutoMapper;

    @Override
    public void createNegativeKeywords(String amazonAdvertisingAPIScope, String amazonAdvertisingAPIClientId, String token
            , Long campaignId
            , Long adGroupId, String keywordText,String rule) {
        int count = 3;
        String url = "https://advertising-api.amazon.com/v2/sp/negativeKeywords";
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("Amazon-Advertising-API-ClientId",amazonAdvertisingAPIClientId);
        paramMap.put("Amazon-Advertising-API-Scope",amazonAdvertisingAPIScope);
        paramMap.put("Authorization",token);
        List<Map<String,Object>> list = new ArrayList<>();
        HashMap<String, Object> reqBody = new HashMap<>();
        reqBody.put("campaignId",campaignId);
        reqBody.put("adGroupId",adGroupId);
        reqBody.put("state","enabled");
        reqBody.put("keywordText",keywordText);
        reqBody.put("matchType","negativeExact");
        list.add(reqBody);
        HttpResponse response = null;
        while (count>0){
            try {
                response = HttpRequest.post(url).headerMap(paramMap, false).body(JSONUtil.toJsonStr(list)).execute();
            } catch (Exception e) {
                System.out.println("createNegativeKeywords失败重试");
                e.printStackTrace();
                count--;
                continue;
            }
            if (!StringUtil.isEmpty(response)){
                break;
            }
        }
        if (StringUtil.isEmpty(response)){
            System.out.println("createNegativeKeywords结果为空");
            return;
        }
        System.out.println(response);
        List<Map<String,Object>> resultMap = JSONObject.parseObject(response.body(),List.class);
        if ("SUCCESS".equals(resultMap.get(0).get("code"))){
            try {
                String date = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -0));
                amazonAdAutoMapper.insertLog(rule+",添加否定Search Term：campaignId:"+campaignId+",adGroupId:"+adGroupId+"关键词:"+keywordText,date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void createNegativeAsin(String amazonAdvertisingAPIScope, String amazonAdvertisingAPIClientId, String token
            , Long campaignId, Long adGroupId, String asin, String rule) {
        int count = 3;
        String url = "https://advertising-api.amazon.com/v2/sp/negativeTargets";
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("Amazon-Advertising-API-ClientId",amazonAdvertisingAPIClientId);
        paramMap.put("Amazon-Advertising-API-Scope",amazonAdvertisingAPIScope);
        paramMap.put("Authorization",token);
        List<Map<String,Object>> list = new ArrayList<>();
        HashMap<String, Object> reqBody = new HashMap<>();
        reqBody.put("campaignId",campaignId);
        reqBody.put("adGroupId",adGroupId);
        reqBody.put("state","enabled");
        reqBody.put("expressionType","manual");
        List<Map<String,Object>> expressionList = new ArrayList<>();
        HashMap<String, Object> expressionMap = new HashMap<>();
        expressionMap.put("type","asinSameAs");
        expressionMap.put("value",asin);
        expressionList.add(expressionMap);
        reqBody.put("expression",expressionList);
        list.add(reqBody);
        HttpResponse response = null;
        while (count>0){
            try {
                response = HttpRequest.post(url).headerMap(paramMap, false).body(JSONUtil.toJsonStr(list)).execute();
            } catch (Exception e) {
                System.out.println("createNegativeAsin失败重试");
                e.printStackTrace();
                count--;
                continue;
            }
            if (!StringUtil.isEmpty(response)){
                break;
            }
        }
        if (StringUtil.isEmpty(response)){
            System.out.println("createNegativeAsin结果为空");
            return;
        }
        System.out.println(response);
        List<Map<String,Object>> resultMap = JSONObject.parseObject(response.body(),List.class);
        if ("SUCCESS".equals(resultMap.get(0).get("code"))){
            try {
                String date = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -0));
                amazonAdAutoMapper.insertLog(rule+",添加否定Asin：campaignId:"+campaignId+",adGroupId:"+adGroupId+"Asin:"+asin,date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateCampaignStatus(String amazonAdvertisingAPIScope, String amazonAdvertisingAPIClientId, String token
            , Long campaignId, String rule) {
        int count = 3;
        String url = "https://advertising-api.amazon.com/v2/sp/campaigns";
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("Amazon-Advertising-API-ClientId",amazonAdvertisingAPIClientId);
        paramMap.put("Amazon-Advertising-API-Scope",amazonAdvertisingAPIScope);
        paramMap.put("Authorization",token);
        List<Map<String,Object>> list = new ArrayList<>();
        HashMap<String, Object> reqBody = new HashMap<>();
        reqBody.put("campaignId",campaignId);
        reqBody.put("state","paused");
        list.add(reqBody);
        HttpResponse response = null;
        while (count>0){
            try {
                response = HttpRequest.put(url).headerMap(paramMap, false).body(JSONUtil.toJsonStr(list)).execute();
            } catch (Exception e) {
                System.out.println("updateCampaignStatus失败重试");
                e.printStackTrace();
                count--;
                continue;
            }
            if (!StringUtil.isEmpty(response)){
                break;
            }
        }
        if (StringUtil.isEmpty(response)){
            System.out.println("updateCampaignStatus结果为空");
            return;
        }
        System.out.println(response);
        List<Map<String,Object>> resultMap = JSONObject.parseObject(response.body(),List.class);
        if ("SUCCESS".equals(resultMap.get(0).get("code"))){
            try {
                String date = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -0));
                amazonAdAutoMapper.insertLog(rule+",把campaign暂停：campaignId:"+campaignId,date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateAdgroupStatus(String amazonAdvertisingAPIScope, String amazonAdvertisingAPIClientId, String token
            , Long adGroupId, String rule) {
        int count = 3;
        String url = "https://advertising-api.amazon.com/v2/sp/adGroups";
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("Amazon-Advertising-API-ClientId",amazonAdvertisingAPIClientId);
        paramMap.put("Amazon-Advertising-API-Scope",amazonAdvertisingAPIScope);
        paramMap.put("Authorization",token);
        List<Map<String,Object>> list = new ArrayList<>();
        HashMap<String, Object> reqBody = new HashMap<>();
        reqBody.put("adGroupId",adGroupId);
        reqBody.put("state","paused");
        list.add(reqBody);
        HttpResponse response = null;
        while (count>0){
            try {
                response = HttpRequest.put(url).headerMap(paramMap, false).body(JSONUtil.toJsonStr(list)).execute();
            } catch (Exception e) {
                System.out.println("updateAdgroupStatus失败重试");
                e.printStackTrace();
                count--;
                continue;
            }
            if (!StringUtil.isEmpty(response)){
                break;
            }
        }
        if (StringUtil.isEmpty(response)){
            System.out.println("updateAdgroupStatus结果为空");
            return;
        }
        System.out.println(response);
        List<Map<String,Object>> resultMap = JSONObject.parseObject(response.body(),List.class);
        if ("SUCCESS".equals(resultMap.get(0).get("code"))){
            try {
                String date = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -0));
                amazonAdAutoMapper.insertLog(rule+",把adGroup暂停：adGroupId:"+adGroupId,date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateKeywordBid(String amazonAdvertisingAPIScope, String amazonAdvertisingAPIClientId, String token
            , Long keywordId, String rule, BigDecimal bid) {
        int count = 3;
        String url = "https://advertising-api.amazon.com/v2/sp/keywords";
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("Amazon-Advertising-API-ClientId",amazonAdvertisingAPIClientId);
        paramMap.put("Amazon-Advertising-API-Scope",amazonAdvertisingAPIScope);
        paramMap.put("Authorization",token);
        List<Map<String,Object>> list = new ArrayList<>();
        HashMap<String, Object> reqBody = new HashMap<>();
        reqBody.put("keywordId",keywordId);
        reqBody.put("state","enabled");
        reqBody.put("bid",bid);
        list.add(reqBody);
        HttpResponse response = null;
        while (count>0){
            try {
                response = HttpRequest.put(url).headerMap(paramMap, false).body(JSONUtil.toJsonStr(list)).execute();
            } catch (Exception e) {
                System.out.println("updateKeywordBid失败重试");
                e.printStackTrace();
                count--;
                continue;
            }
            if (!StringUtil.isEmpty(response)){
                break;
            }
        }
        if (StringUtil.isEmpty(response)){
            System.out.println("updateKeywordBid结果为空");
            return;
        }
        System.out.println(response);
        List<Map<String,Object>> resultMap = JSONObject.parseObject(response.body(),List.class);
        if ("SUCCESS".equals(resultMap.get(0).get("code"))){
            try {
                String date = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -0));
                amazonAdAutoMapper.insertLog(rule+",更新keywordBid：keywordId:"+keywordId+",bid"+bid,date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void createKeyword(String amazonAdvertisingAPIScope, String amazonAdvertisingAPIClientId, String token
            , Long campaignId, Long adGroupId, String rule, String matchType, String keyWord, BigDecimal bid) {
        int count = 3;
        String url = "https://advertising-api.amazon.com/v2/sp/keywords";
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("Amazon-Advertising-API-ClientId",amazonAdvertisingAPIClientId);
        paramMap.put("Amazon-Advertising-API-Scope",amazonAdvertisingAPIScope);
        paramMap.put("Authorization",token);
        List<Map<String,Object>> list = new ArrayList<>();
        HashMap<String, Object> reqBody = new HashMap<>();
        reqBody.put("campaignId",campaignId);
        reqBody.put("adGroupId",adGroupId);
        reqBody.put("state","enabled");
        reqBody.put("keywordText",keyWord);
        reqBody.put("matchType",matchType);
        reqBody.put("bid",bid);
        list.add(reqBody);
        HttpResponse response = null;
        while (count>0){
            try {
                response = HttpRequest.post(url).headerMap(paramMap, false).body(JSONUtil.toJsonStr(list)).execute();
            } catch (Exception e) {
                System.out.println("createKeyword失败重试");
                e.printStackTrace();
                count--;
                continue;
            }
            if (!StringUtil.isEmpty(response)){
                break;
            }
        }
        if (StringUtil.isEmpty(response)){
            System.out.println("createKeyword结果为空");
            return;
        }
        System.out.println(response);
        List<Map<String,Object>> resultMap = JSONObject.parseObject(response.body(),List.class);
        if ("SUCCESS".equals(resultMap.get(0).get("code"))){
            try {
                String date = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -0));
                amazonAdAutoMapper.insertLog(rule+",新建Keyword：campaignId:"+campaignId+",adGroupId:"+adGroupId+",keywordText"
                        +keyWord+",matchType"+matchType+",bid"+bid,date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String createAdGroup(String amazonAdvertisingAPIScope, String amazonAdvertisingAPIClientId, String token
            , Long campaignId, String rule,String name,Double bid,String state) {
        int count = 3;
        String url = "https://advertising-api.amazon.com/v2/sp/adGroups";
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("Amazon-Advertising-API-ClientId",amazonAdvertisingAPIClientId);
        paramMap.put("Amazon-Advertising-API-Scope",amazonAdvertisingAPIScope);
        paramMap.put("Authorization",token);
        List<Map<String,Object>> list = new ArrayList<>();
        HashMap<String, Object> reqBody = new HashMap<>();
        reqBody.put("name",name);
        reqBody.put("campaignId",campaignId);
        reqBody.put("defaultBid",bid);
        reqBody.put("state",state);
        list.add(reqBody);
        HttpResponse response = null;
        while (count>0){
            try {
                response = HttpRequest.post(url).headerMap(paramMap, false).body(JSONUtil.toJsonStr(list)).execute();
            } catch (Exception e) {
                System.out.println("createAdGroup失败重试");
                e.printStackTrace();
                count--;
                continue;
            }
            if (!StringUtil.isEmpty(response)){
                break;
            }
        }
        if (StringUtil.isEmpty(response)){
            System.out.println("createAdGroup结果为空");
            return null;
        }
        System.out.println(response);
        List<Map<String,Object>> resultMap = JSONObject.parseObject(response.body(),List.class);
        if ("SUCCESS".equals(resultMap.get(0).get("code"))){
            try {
                String date = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -0));
                amazonAdAutoMapper.insertLog(rule+",新建AdGroup：campaignId:"+campaignId+",adGroupId:"+resultMap.get(0).get("adGroupId"),date);
                return StringUtil.toString(resultMap.get(0).get("adGroupId"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public String createCampaign(String amazonAdvertisingAPIScope, String amazonAdvertisingAPIClientId, String token
            , String rule,CreateCampaign createCampaign,String campaignName) throws ParseException {
        int count = 3;
        String url = "https://advertising-api.amazon.com/v2/sp/campaigns";
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("Amazon-Advertising-API-ClientId",amazonAdvertisingAPIClientId);
        paramMap.put("Amazon-Advertising-API-Scope",amazonAdvertisingAPIScope);
        paramMap.put("Authorization",token);
        List<Map<String,Object>> list = new ArrayList<>();
        HashMap<String, Object> reqBody = new HashMap<>();
        reqBody.put("name",campaignName);
        reqBody.put("campaignType","sponsoredProducts");
        reqBody.put("targetingType",createCampaign.getTargetingType());
        reqBody.put("dailyBudget",createCampaign.getDailyBudget());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        reqBody.put("startDate",com.zhz.selenium.util.DateUtil.parseDate(sdf.parse(createCampaign.getStartDate())
                , com.zhz.selenium.util.DateUtil.DATE_PATTERN_yyyyMMdd));
        if (!StringUtil.isEmpty(createCampaign.getEndDate())){
            reqBody.put("endDate",com.zhz.selenium.util.DateUtil.parseDate(sdf.parse(createCampaign.getEndDate())
                    , com.zhz.selenium.util.DateUtil.DATE_PATTERN_yyyyMMdd));
        }
        reqBody.put("state","enabled");
        HashMap<String, Object> biddingMap = new HashMap<>();
        biddingMap.put("strategy",createCampaign.getBiddingStrategy().get(createCampaign.getBiddingStrategyKey()));
        List<Map<String,Object>> adjustments = new ArrayList<>();
        HashMap<String, Object> adjustmentsMap = new HashMap<>();
        adjustmentsMap.put("predicate",createCampaign.getBiddingAdjustmentsPredicate());
        adjustmentsMap.put("percentage",createCampaign.getBiddingAdjustmentsPercentage());
        adjustments.add(adjustmentsMap);
        biddingMap.put("adjustments",adjustments);
        reqBody.put("bidding",biddingMap);
        list.add(reqBody);
        HttpResponse response = null;
        while (count>0){
            try {
                response = HttpRequest.post(url).headerMap(paramMap, false).body(JSONUtil.toJsonStr(list)).execute();
            } catch (Exception e) {
                System.out.println("createCampaign失败重试");
                e.printStackTrace();
                count--;
                continue;
            }
            if (!StringUtil.isEmpty(response)){
                break;
            }
        }
        if (StringUtil.isEmpty(response)){
            System.out.println("createCampaign结果为空");
            return null;
        }
        System.out.println(response);
        List<Map<String,Object>> resultMap = JSONObject.parseObject(response.body(),List.class);
        if ("SUCCESS".equals(resultMap.get(0).get("code"))){
            try {
                String date = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -0));
                amazonAdAutoMapper.insertLog(rule+",新建Campaign：campaignId:"+resultMap.get(0).get("campaignId"),date);
                return StringUtil.toString(resultMap.get(0).get("campaignId"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    @Override
    public String createCampaign(String amazonAdvertisingAPIScope, String amazonAdvertisingAPIClientId, String token
            , String rule, AutoCampaign campaign, String campaignName) throws ParseException {
        int count = 3;
        String url = "https://advertising-api.amazon.com/v2/sp/campaigns";
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("Amazon-Advertising-API-ClientId",amazonAdvertisingAPIClientId);
        paramMap.put("Amazon-Advertising-API-Scope",amazonAdvertisingAPIScope);
        paramMap.put("Authorization",token);
        List<Map<String,Object>> list = new ArrayList<>();
        HashMap<String, Object> reqBody = new HashMap<>();
        reqBody.put("name",campaignName);
        reqBody.put("campaignType","sponsoredProducts");
        reqBody.put("targetingType","auto");
        reqBody.put("dailyBudget",campaign.getDailyBudget());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        reqBody.put("startDate",com.zhz.selenium.util.DateUtil.parseDate(sdf.parse(campaign.getStartDate())
                , com.zhz.selenium.util.DateUtil.DATE_PATTERN_yyyyMMdd));
        if (!StringUtil.isEmpty(campaign.getEndDate())){
            reqBody.put("endDate",com.zhz.selenium.util.DateUtil.parseDate(sdf.parse(campaign.getEndDate())
                    , com.zhz.selenium.util.DateUtil.DATE_PATTERN_yyyyMMdd));
        }
        reqBody.put("state","enabled");
        HashMap<String, Object> biddingMap = new HashMap<>();
        biddingMap.put("strategy",campaign.getBiddingStrategy().get(campaign.getBiddingStrategyKey()));
        List<Map<String,Object>> adjustments = new ArrayList<>();
        if (!StringUtil.isEmpty(campaign.getBiddingAdjustmentsTopPercentage())){
            HashMap<String, Object> adjustmentsMap = new HashMap<>();
            adjustmentsMap.put("predicate","placementTop");
            adjustmentsMap.put("percentage",campaign.getBiddingAdjustmentsTopPercentage());
            adjustments.add(adjustmentsMap);
        }
        if (!StringUtil.isEmpty(campaign.getBiddingAdjustmentsPagePercentage())){
            HashMap<String, Object> adjustmentsMap = new HashMap<>();
            adjustmentsMap.put("predicate","placementProductPage");
            adjustmentsMap.put("percentage",campaign.getBiddingAdjustmentsPagePercentage());
            adjustments.add(adjustmentsMap);
        }
        biddingMap.put("adjustments",adjustments);
        reqBody.put("bidding",biddingMap);
        list.add(reqBody);
        HttpResponse response = null;
        while (count>0){
            try {
                response = HttpRequest.post(url).headerMap(paramMap, false).body(JSONUtil.toJsonStr(list)).execute();
            } catch (Exception e) {
                System.out.println("createCampaign失败重试");
                e.printStackTrace();
                count--;
                continue;
            }
            if (!StringUtil.isEmpty(response)){
                break;
            }
        }
        if (StringUtil.isEmpty(response)){
            System.out.println("createCampaign结果为空");
            return null;
        }
        System.out.println(response);
        List<Map<String,Object>> resultMap = JSONObject.parseObject(response.body(),List.class);
        if ("SUCCESS".equals(resultMap.get(0).get("code"))){
            try {
                String date = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -0));
                amazonAdAutoMapper.insertLog(rule+",新建Campaign：campaignId:"+resultMap.get(0).get("campaignId"),date);
                return StringUtil.toString(resultMap.get(0).get("campaignId"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void createProductAds(String amazonAdvertisingAPIScope, String amazonAdvertisingAPIClientId, String token
            , String rule
            , String sku,Long campaignId, Long adGroupId) {
        int count = 3;
        String url = "https://advertising-api.amazon.com/v2/sp/productAds";
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("Amazon-Advertising-API-ClientId",amazonAdvertisingAPIClientId);
        paramMap.put("Amazon-Advertising-API-Scope",amazonAdvertisingAPIScope);
        paramMap.put("Authorization",token);
        List<Map<String,Object>> list = new ArrayList<>();
        HashMap<String, Object> reqBody = new HashMap<>();
        reqBody.put("adGroupId",adGroupId);
        reqBody.put("campaignId",campaignId);
        reqBody.put("sku",sku);
        reqBody.put("state","enabled");
        list.add(reqBody);
        HttpResponse response = null;
        while (count>0){
            try {
                response = HttpRequest.post(url).headerMap(paramMap, false).body(JSONUtil.toJsonStr(list)).execute();
            } catch (Exception e) {
                System.out.println("createProductAds失败重试");
                e.printStackTrace();
                count--;
                continue;
            }
            if (!StringUtil.isEmpty(response)){
                break;
            }
        }
        if (StringUtil.isEmpty(response)){
            System.out.println("createProductAds结果为空");
        }
        System.out.println(response);
        List<Map<String,Object>> resultMap = JSONObject.parseObject(response.body(),List.class);
        if ("SUCCESS".equals(resultMap.get(0).get("code"))){
            try {
                String date = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -0));
                amazonAdAutoMapper.insertLog(rule+",新建ProductAds：campaignId:"+campaignId+",adGroupId:"+adGroupId,date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void createTarget(String amazonAdvertisingAPIScope, String amazonAdvertisingAPIClientId, String token
            , String rule, Long campaignId, Long adGroupId, String targetWord, BigDecimal bid) {
        int count = 3;
        String url = "https://advertising-api.amazon.com/v2/sp/targets";
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("Amazon-Advertising-API-ClientId",amazonAdvertisingAPIClientId);
        paramMap.put("Amazon-Advertising-API-Scope",amazonAdvertisingAPIScope);
        paramMap.put("Authorization",token);
        List<Map<String,Object>> list = new ArrayList<>();
        HashMap<String, Object> reqBody = new HashMap<>();
        reqBody.put("campaignId",campaignId);
        reqBody.put("adGroupId",adGroupId);
        reqBody.put("state","enabled");
        reqBody.put("expressionType","manual");
        reqBody.put("bid",bid);
        List<Map<String,Object>> expressionList = new ArrayList<>();
        HashMap<String, Object> expressionMap = new HashMap<>();
        expressionMap.put("type","asinSameAs");
        expressionMap.put("value",targetWord);
        expressionList.add(expressionMap);
        reqBody.put("expression",expressionList);
        list.add(reqBody);
        HttpResponse response = null;
        while (count>0){
            try {
                response = HttpRequest.post(url).headerMap(paramMap, false).body(JSONUtil.toJsonStr(list)).execute();
            } catch (Exception e) {
                System.out.println("createTarget失败重试");
                e.printStackTrace();
                count--;
                continue;
            }
            if (!StringUtil.isEmpty(response)){
                break;
            }
        }
        if (StringUtil.isEmpty(response)){
            System.out.println("createTarget结果为空");
            return;
        }
        System.out.println(response);
        List<Map<String,Object>> resultMap = JSONObject.parseObject(response.body(),List.class);
        if ("SUCCESS".equals(resultMap.get(0).get("code"))){
            try {
                String date = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -0));
                amazonAdAutoMapper.insertLog(rule+",添加Target：campaignId:"+campaignId+",adGroupId:"+adGroupId+"Asin:"+targetWord,date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Map<String,List<Map<String,Object>>> dealNegativeKeywords(List<Map<String, Object>> results) {
        Map<String,List<Map<String,Object>>> reslutMap = new HashMap<>(16);
        List<String> removeList = new ArrayList<>();
        List<Map<String, Object>> whiteAsin = amazonAdAutoMapper.selectWhiteAsin();
        for (Map<String, Object> m: results) {
            String key = m.get("campaign_id")+"&"+m.get("ad_group_id");
            if (reslutMap.containsKey(key)){
                if (StringUtil.isEmpty(reslutMap.get(key))){
                    List<Map<String,Object>> sList = new ArrayList<>();
                    sList.add(m);
                    reslutMap.put(key,sList);
                }else {
                    List<Map<String, Object>> sList = reslutMap.get(key);
                    sList.add(m);
                    reslutMap.put(key,sList);
                }
            }else {
                List<Map<String,Object>> sList = new ArrayList<>();
                sList.add(m);
                reslutMap.put(key,sList);
            }

            for (Map<String, Object> asin:whiteAsin) {
                // asin白名单
                if ("1".equals(asin.get("flag")) && StringUtil.toString(m.get("Advertised_ASIN")).equals(asin.get("msg"))){
                    removeList.add(key);
                }
                // campaign白名单
                if ("2".equals(asin.get("flag")) && StringUtil.toString(m.get("name")).contains(StringUtil.toString(asin.get("msg")))){
                    removeList.add(key);
                }
                // polo白名单
                if ("3".equals(asin.get("flag")) && StringUtil.toString(m.get("Portfolio_name")).equals(asin.get("msg"))){
                    removeList.add(key);
                }
            }

        }

        for (String str : removeList) {
            if (reslutMap.containsKey(str)){
                reslutMap.remove(str);
            }
        }
        return reslutMap;
    }

    @Override
    public List<Map<String, Object>> dealAsin(List<Map<String, Object>> results) {
        List<Map<String, Object>> result  = new ArrayList<>();
        for (Map<String, Object> m :results) {
            String asin = StringUtil.toString(m.get("Customer_Search_Term"));
            if (asin.toLowerCase().startsWith("b0")
                    && asin.length() == 10){
                result.add(m);
            }
        }
        return result;
    }

    @Override
    public String[] getKeyword(CreateCampaign campaign,int i) {
        String keyWord;
        switch (i) {
            case 1:
                keyWord = campaign.getCampaignKeyword1();
                break;
            case 2:
                keyWord = campaign.getCampaignKeyword2();
                break;
            case 3:
                keyWord = campaign.getCampaignKeyword3();
                break;
            case 4:
                keyWord = campaign.getCampaignKeyword4();
                break;
            case 5:
                keyWord = campaign.getCampaignKeyword5();
                break;
            case 6:
                keyWord = campaign.getCampaignKeyword6();
                break;
            default:
                keyWord = null;
                break;
        }
        if (!StringUtil.isEmpty(keyWord)){
            String[] split = keyWord.split(",");
            return split;
        }
        return new String[0];
    }

    @Override
    public String getMatchType(CreateCampaign campaign, int i) {
        String matchType;
        switch (i) {
            case 1:
                matchType = campaign.getMatchType1();
                break;
            case 2:
                matchType = campaign.getMatchType2();
                break;
            case 3:
                matchType = campaign.getMatchType3();
                break;
            case 4:
                matchType = campaign.getMatchType4();
                break;
            case 5:
                matchType = campaign.getMatchType5();
                break;
            case 6:
                matchType = campaign.getMatchType6();
                break;
            default:
                matchType = "exact";
                break;
        }
        return matchType;
    }

    @Override
    public BigDecimal getBid(CreateCampaign campaign, int i) {
        String bid;
        switch (i) {
            case 1:
                bid = campaign.getBid1();
                break;
            case 2:
                bid = campaign.getBid2();
                break;
            case 3:
                bid = campaign.getBid3();
                break;
            case 4:
                bid = campaign.getBid4();
                break;
            case 5:
                bid = campaign.getBid5();
                break;
            case 6:
                bid = campaign.getBid6();
                break;
            default:
                bid = null;
                break;
        }
        try {
            if (!StringUtil.isEmpty(bid)){
                return new BigDecimal(bid);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return campaign.getDefBid();
    }

}
