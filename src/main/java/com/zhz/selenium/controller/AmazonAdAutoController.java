package com.zhz.selenium.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.zhz.selenium.mapper.AmazonAdAutoMapper;
import com.zhz.selenium.pojo.AutoCampaign;
import com.zhz.selenium.pojo.CreateCampaign;
import com.zhz.selenium.service.AmazonAdAutoService;
import com.zhz.selenium.service.AmazonFileService;
import com.zhz.selenium.util.StringUtil;
import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@Component
@EnableScheduling
public class AmazonAdAutoController {
    Log log = LogFactory.get();

    @Autowired
    private AmazonAdAutoService amazonAdAutoService;

    @Autowired
    private AmazonFileService amazonFileService;

    @Autowired
    private AmazonAdAutoMapper amazonAdAutoMapper;

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

    @GetMapping(value = "/ad_log")
    public ModelAndView campagin_list(){
        ModelAndView modelAndView = new ModelAndView();
        List<Map<String, Object>> list = amazonAdAutoMapper.selectLog();
        modelAndView.addObject("list",list);
        modelAndView.setViewName("ad_log");
        return modelAndView;
    }

//    @RequestMapping(value = "/createNegativeKeywords")
//    @Scheduled(cron="0 0 19 * * ?")
//    public void createNegativeKeywords() {
//        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        String startTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), -30));
//        Map<Object, Object> map = new HashMap<>();
//        List<Map<String, Object>> results = amazonAdAutoMapper.selectNoOrderKeyWord();
//        List<Map<String, Object>> recentResults = amazonAdAutoMapper.selectRecentNoOrderKeyWord(startTime,date);
//        Map<String, List<Map<String, Object>>> resultsList = amazonAdAutoService.dealNegativeKeywords(results);
//        Map<String, List<Map<String, Object>>> recentResultsList = amazonAdAutoService.dealNegativeKeywords(recentResults);
//        amazonFileService.getAccessToken(grantType,clientId,refreshToken,clientSecret,map);
//        doTimeout(map);
//        autoAdKeyWord(map,resultsList,"历史上就没出过单，那么 只要 点击超过 11次就否定");
//        doTimeout(map);
//        autoAdKeyWord(map,recentResultsList,"历史上有出过单，但最近没出单，那么距离上次出单已经超过 20次就否定");
////        doTimeout(map);
////        List<Map<String, Object>> maps = amazonAdAutoMapper.selectConverionRateOrCTR(new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), -17))
////                , new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), -3)));
////        Map<String, List<Map<String, Object>>> acosList = amazonAdAutoService.dealNegativeKeywords(maps);
////        autoAd(map,acosList,"如果 ACOS > 50%, Converion Rate 转化率 <3%或如果 ACOS > 50%, CTR 点击率 <0.2%");
//    }
//
//
//    @RequestMapping(value = "/createNegativeAsin")
//    @Scheduled(cron="0 0,30 19 * * ?")
//    public void createNegativeAsin() {
//        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        String startTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), -30));
//        Map<Object, Object> map = new HashMap<>();
//        List<Map<String, Object>> results = amazonAdAutoMapper.selectNoOrderAsin();
//        List<Map<String, Object>> recentResults = amazonAdAutoMapper.selectRecentNoOrderAsin(startTime,date);
//        // 处理asin
//        List<Map<String, Object>> resultList = amazonAdAutoService.dealAsin(results);
//        List<Map<String, Object>> recentResultList = amazonAdAutoService.dealAsin(recentResults);
//        Map<String, List<Map<String, Object>>> resultsList = amazonAdAutoService.dealNegativeKeywords(resultList);
//        Map<String, List<Map<String, Object>>> recentResultsList = amazonAdAutoService.dealNegativeKeywords(recentResultList);
//        amazonFileService.getAccessToken(grantType,clientId,refreshToken,clientSecret,map);
//        doTimeout(map);
//        autoAdAsin(map,resultsList,"历史上就没出过单，那么 只要 点击超过 3次就否定");
//        doTimeout(map);
//        autoAdAsin(map,recentResultsList,"历史上有出过单，但最近没出单，那么距离上次出单已经超过 5次就否定");
//    }


    /**
     * 自动创建广告组
     * @param campaign
     */
    @RequestMapping(value = "/autoCreateCampaign")
    @ResponseBody
    public synchronized String autoCreateCampaign(CreateCampaign campaign){
        StringBuffer sb = new StringBuffer();
        try {
            Map<Object, Object> map = new HashMap<>();
            // 根据填写asin查找sku
            String sku = amazonAdAutoMapper.selectSkuByAsin(campaign.getAsin());
            if (!StringUtil.isEmpty(sku)){
                amazonFileService.getAccessToken(grantType,clientId,refreshToken,clientSecret,map);
                doTimeout(map);
                // 不同组创建不同关键词
                for (int i = 1; i <7 ; i++) {
                    String[] keywords = amazonAdAutoService.getKeyword(campaign, i);
                    String campaignId = null;
                    String adGroupId = null;
                    if (!StringUtil.isEmpty(keywords)){
                        // 创建campaign id
                        doTimeout(map);
                        sb.append(", camName:"+campaign.getCampaignName()+keywords[0]);
                        campaignId = amazonAdAutoService.createCampaign(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
                                , "Bearer " + map.get("access_token"), "自动生成并创建campagin ", campaign, campaign.getCampaignName()+" - "+keywords[0]);
                        // 创建 ad group id
                        if (!StringUtil.isEmpty(campaignId)){
                            doTimeout(map);
                            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                            sb.append(", adGroupName:"+"Mass " + date);
                            adGroupId = amazonAdAutoService.createAdGroup(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
                                    , "Bearer " + map.get("access_token"), Long.parseLong(campaignId)
                                    , "自动生成并创建adgroup ", "Mass " + date,0.2,"enabled");
                        }
                        // 创建词
                        if (!StringUtil.isEmpty(campaignId) && !StringUtil.isEmpty(adGroupId)){
                            for (String keyword:keywords) {
                                BigDecimal bid = amazonAdAutoService.getBid(campaign, i);
                                String matchType = amazonAdAutoService.getMatchType(campaign, i);
                                doTimeout(map);
                                sb.append("， 创建关键词："+keyword+", bid:"+bid);
                                amazonAdAutoService.createKeyword(amazonAdvertisingAPIScope,amazonAdvertisingAPIClientId
                                        ,"Bearer " + map.get("access_token"),Long.parseLong(campaignId),Long.parseLong(adGroupId)
                                        ,"自动生成并创建keyword ", matchType,keyword, bid);
                            }
                        }
                        // 投放
                        sb.append(", 投放广告：sku："+sku);
                        amazonAdAutoService.createProductAds(amazonAdvertisingAPIScope,amazonAdvertisingAPIClientId
                                , "Bearer " + map.get("access_token"), "广告投放"
                                , sku,Long.parseLong(campaignId),Long.parseLong(adGroupId));
                        // 创建否定关键词
                        String negativeKeyword = campaign.getNegativeKeyword();
                        if (!StringUtil.isEmpty(negativeKeyword)){
                            String[] split = negativeKeyword.split(",");
                            for (String keywordText:split) {
                                doTimeout(map);
                                sb.append(", 否定关键词："+keywordText);
                                amazonAdAutoService.createNegativeKeywords(amazonAdvertisingAPIScope,amazonAdvertisingAPIClientId
                                        ,"Bearer " + map.get("access_token"),Long.parseLong(campaignId),Long.parseLong(adGroupId)
                                        ,keywordText,"自动生成并创建negativeKeyword ");
                            }
                        }
                    }
                }
            }else {
                return "失败,asin查不到sku："+campaign.getAsin();
            }
            return "结果：成功"+sb;
        } catch (Exception e) {
            log.info(e.getMessage());
            return "结果：出现异常，请联系管理员,请去Amazon后台检查"+sb;
        }
    }

    /**
     * 防御asin
     */
    @RequestMapping(value = "/defenseAsin")
    @ResponseBody
    public synchronized String DefenseAsin(CreateCampaign campaign){
        StringBuffer sb = new StringBuffer();
        try {
//            String adAsins = campaign.getAdAsins();
            String asin = campaign.getAsin();
            String beAdAsins = campaign.getBeAdAsins();
            if (StringUtil.isEmpty(asin) || StringUtil.isEmpty(beAdAsins)){
                return "失败，请填写asin";
            }
            Map<Object, Object> map = new HashMap<>();
            amazonFileService.getAccessToken(grantType,clientId,refreshToken,clientSecret,map);
//            if (!StringUtil.isEmpty(adAsins)){
//                String campaignId;
//                String adGroupId = null;
//                // 创建 campaign adgroup 然后把创建好的往adAsin里面塞
//                doTimeout(map);
//                sb.append(", camName:##ASIN Target"+campaign.getAsin()+"##"+UUID.randomUUID().toString().substring(0,4));
//                campaignId = amazonAdAutoService.createCampaign(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
//                        , "Bearer " + map.get("access_token"), "自动生成并创建campagin ", campaign
//                        , "##ASIN Target"+campaign.getAsin()+"##"+UUID.randomUUID().toString().substring(0,4));
//
//                if (!StringUtil.isEmpty(campaignId)){
//                    doTimeout(map);
//                    sb.append(", adGroupName: ##ASIN Target"+campaign.getAsin());
//                    adGroupId = amazonAdAutoService.createAdGroup(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
//                            , "Bearer " + map.get("access_token"), Long.parseLong(campaignId), "自动生成并创建adgroup "
//                            , "##ASIN Target"+campaign.getAsin(),0.2,"enabled");
//                }
//                // 创建词
//                if (!StringUtil.isEmpty(campaignId) && !StringUtil.isEmpty(adGroupId)){
//                    String sku = amazonAdAutoMapper.selectSkuByAsin(campaign.getAsin());
//                    // 投放
//                    doTimeout(map);
//                    amazonAdAutoService.createProductAds(amazonAdvertisingAPIScope,amazonAdvertisingAPIClientId
//                            , "Bearer " + map.get("access_token"), "广告投放"
//                            , sku,Long.parseLong(campaignId),Long.parseLong(adGroupId));
//                    // 塞
//                    String[] adArr = adAsins.split(",");
//                    sb.append("， 应打asin："+adAsins+" ,  bid："+campaign.getDefBid()+"\n");
//                    for (String adAsin:adArr) {
//                        doTimeout(map);
//                        amazonAdAutoService.createTarget(amazonAdvertisingAPIScope,amazonAdvertisingAPIClientId
//                                ,"Bearer " + map.get("access_token"), "防御ASIN广告", Long.parseLong(campaignId)
//                                , Long.parseLong(adGroupId), adAsin, campaign.getDefBid());
//                    }
//                }
//            }

            String[] adArr = beAdAsins.split(",");
            // 循环创建 campaign adgroup 然后把输入的asin往beAsin里面塞
            for (String beAdAsin:adArr) {
                String campaignId;
                String adGroupId = null;
                // 创建 campaign adgroup 然后把创建好的往adAsin里面塞
                sb.append(", camName:##ASIN Target"+campaign.getAsin()+"##"+UUID.randomUUID().toString().substring(0,4));
                doTimeout(map);
                campaignId = amazonAdAutoService.createCampaign(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
                        , "Bearer " + map.get("access_token"), "自动生成并创建campagin ", campaign
                        , "##ASIN Target"+beAdAsin+"##"+ UUID.randomUUID().toString().substring(0,4));
                if (!StringUtil.isEmpty(campaignId)){
                    doTimeout(map);
                    sb.append(", adGroupName: ##ASIN Target"+campaign.getAsin());
                    adGroupId = amazonAdAutoService.createAdGroup(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
                            , "Bearer " + map.get("access_token"), Long.parseLong(campaignId), "自动生成并创建adgroup "
                            , "##ASIN Target"+beAdAsin,0.2,"enabled");
                }
                // 创建词
                if (!StringUtil.isEmpty(campaignId) && !StringUtil.isEmpty(adGroupId)){
                    String sku = amazonAdAutoMapper.selectSkuByAsin(beAdAsin);
                    // 投放
                    sb.append("， 应打asin："+Arrays.toString(adArr)+" ,  bid："+campaign.getDefBid()+"\n");
                    if (StringUtil.isEmpty(sku)){
                        return "结果：出现异常，此asin："+beAdAsin+"不存在对应的sku"+sb;
                    }
                    doTimeout(map);
                    amazonAdAutoService.createProductAds(amazonAdvertisingAPIScope,amazonAdvertisingAPIClientId
                            , "Bearer " + map.get("access_token"), "广告投放"
                            , sku,Long.parseLong(campaignId),Long.parseLong(adGroupId));
                    String[] asinSplit = asin.split(",");
                    for (String as:asinSplit) {
                        if (!beAdAsin.equals(as)){
                            doTimeout(map);
                            amazonAdAutoService.createTarget(amazonAdvertisingAPIScope,amazonAdvertisingAPIClientId
                                    ,"Bearer " + map.get("access_token"), "防御ASIN广告", Long.parseLong(campaignId)
                                    , Long.parseLong(adGroupId), as, campaign.getDefBid());
                        }
                    }
                }
            }
            return "结果：成功"+sb;
        } catch (Exception e) {
            e.printStackTrace();
            return "结果：出现异常，请联系管理员,请去Amazon后台检查"+sb;
        }
    }

    /**
     * 自动创建广告
     * @param campaign
     * @return
     */
    @RequestMapping(value = "/autoCampaign")
    @ResponseBody
    public synchronized String autoCampaign(AutoCampaign campaign){
        try {
            // 获取token
            Map<Object, Object> map = new HashMap<>();
            amazonFileService.getAccessToken(grantType,clientId,refreshToken,clientSecret,map);
            // 创建campaign
            String campaignId = null;
            String adGroupId = null;
            if (!StringUtil.isEmpty(campaign.getSku())){
                doTimeout(map);
                campaignId = amazonAdAutoService.createCampaign(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
                        , "Bearer " + map.get("access_token"), "自动生成并创建campagin ", campaign
                        , campaign.getCampaignName());
                // 创建adGroup
                if (!StringUtil.isEmpty(campaignId)){
                    doTimeout(map);
                    adGroupId = amazonAdAutoService.createAdGroup(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
                            , "Bearer " + map.get("access_token"), Long.parseLong(campaignId), "自动生成并创建adgroup "
                            , campaign.getAdGroupName(),campaign.getDefBid(),campaign.getInitialState());
                }
            }
            // 创建广告
            if (!StringUtil.isEmpty(campaignId) && !StringUtil.isEmpty(adGroupId)){
                // 投放
                doTimeout(map);
                amazonAdAutoService.createProductAds(amazonAdvertisingAPIScope,amazonAdvertisingAPIClientId
                        , "Bearer " + map.get("access_token"), "自动创建广告"
                        , campaign.getSku(),Long.parseLong(campaignId),Long.parseLong(adGroupId));
            }else {
                return campaign.getId()+",fail";
            }
            return campaign.getId()+",success";
        } catch (Exception e) {
            log.info(e.getMessage());
            return campaign.getId()+",fail";
        }
    }

    @RequestMapping(value = "/getSkuByAsin")
    @ResponseBody
    public String getSkuByAsin(@RequestParam(value = "asin") String asin){
        String sku = amazonAdAutoMapper.selectSkuByAsin(asin);
        return sku;
    }

    private void autoAdKeyWord(Map<Object, Object> map,Map<String, List<Map<String, Object>>> resultsList,String ruleName){
        for (Map.Entry result :resultsList.entrySet()) {
            List<Map<String, Object>> value = (List)result.getValue();
            for (Map<String, Object> m: value) {
                amazonAdAutoService.createNegativeKeywords(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
                        , "Bearer " + map.get("access_token"), Long.parseLong(StringUtil.toString(m.get("campaign_id")))
                        ,Long.parseLong(StringUtil.toString(m.get("ad_group_id")))
                        ,StringUtil.toString(m.get("Customer_Search_Term")),ruleName);
            }
        }
    }
    private void autoAdAsin(Map<Object, Object> map,Map<String, List<Map<String, Object>>> resultsList,String ruleName){
        for (Map.Entry result :resultsList.entrySet()) {
            List<Map<String, Object>> value = (List)result.getValue();
            for (Map<String, Object> m: value) {
                amazonAdAutoService.createNegativeAsin(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
                        , "Bearer " + map.get("access_token"), Long.parseLong(StringUtil.toString(m.get("campaign_id")))
                        ,Long.parseLong(StringUtil.toString(m.get("ad_group_id")))
                        ,StringUtil.toString(m.get("Customer_Search_Term")),ruleName);
            }
        }
    }


    //    /**
//     * 页面，目前没有用
//     * @param campaignId
//     * @param adgroupId
//     * @param keyWord
//     * @param ruleName
//     * @param flag 1表示反关键词，2表示反asin
//     */
//    @RequestMapping(value = "/autoAd")
//    public void createautoAd(@RequestParam(value = "campaignId")String campaignId
//            ,@RequestParam(value = "adgroupId")String adgroupId,@RequestParam(value = "keyWord")String keyWord
//            ,@RequestParam(value = "ruleName")String ruleName,@RequestParam(value = "flag")String flag) {
//        Map<String, Object> resultsMap = new HashMap<>();
//        resultsMap.put("campaign_id",campaignId);
//        resultsMap.put("ad_group_id",adgroupId);
//        resultsMap.put("Customer_Search_Term",keyWord);
//        Map<Object, Object> map = new HashMap<>();
//        List<Map<String, Object>> results = new ArrayList<>();
//        results.add(resultsMap);
//        amazonFileService.getAccessToken(grantType,clientId,refreshToken,clientSecret,map);
//        if ("1".equals(flag)){
//            Map<String, List<Map<String, Object>>> resultsList = amazonAdAutoService.dealNegativeKeywords(results);
//            doTimeout(map);
//            autoAdKeyWord(map,resultsList,ruleName);
//        }else if ("2".equals(flag)){
//            List<Map<String, Object>> asins = amazonAdAutoService.dealAsin(results);
//            Map<String, List<Map<String, Object>>> resultsList = amazonAdAutoService.dealNegativeKeywords(asins);
//            doTimeout(map);
//            autoAdAsin(map,resultsList,ruleName);
//        }
//    }

    private void doTimeout(Map<Object, Object> map){
        if ((Long)map.get("endTime")-new Date().getTime()<=0){
            amazonFileService.getAccessToken(grantType,clientId,refreshToken,clientSecret,map);
        }
    }

}
