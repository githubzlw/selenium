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
//        autoAdKeyWord(map,resultsList,"????????????????????????????????? ?????? ???????????? 11????????????");
//        doTimeout(map);
//        autoAdKeyWord(map,recentResultsList,"????????????????????????????????????????????????????????????????????????????????? 20????????????");
////        doTimeout(map);
////        List<Map<String, Object>> maps = amazonAdAutoMapper.selectConverionRateOrCTR(new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), -17))
////                , new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), -3)));
////        Map<String, List<Map<String, Object>>> acosList = amazonAdAutoService.dealNegativeKeywords(maps);
////        autoAd(map,acosList,"?????? ACOS > 50%, Converion Rate ????????? <3%????????? ACOS > 50%, CTR ????????? <0.2%");
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
//        // ??????asin
//        List<Map<String, Object>> resultList = amazonAdAutoService.dealAsin(results);
//        List<Map<String, Object>> recentResultList = amazonAdAutoService.dealAsin(recentResults);
//        Map<String, List<Map<String, Object>>> resultsList = amazonAdAutoService.dealNegativeKeywords(resultList);
//        Map<String, List<Map<String, Object>>> recentResultsList = amazonAdAutoService.dealNegativeKeywords(recentResultList);
//        amazonFileService.getAccessToken(grantType,clientId,refreshToken,clientSecret,map);
//        doTimeout(map);
//        autoAdAsin(map,resultsList,"????????????????????????????????? ?????? ???????????? 3????????????");
//        doTimeout(map);
//        autoAdAsin(map,recentResultsList,"????????????????????????????????????????????????????????????????????????????????? 5????????????");
//    }


    /**
     * ?????????????????????
     * @param campaign
     */
    @RequestMapping(value = "/autoCreateCampaign")
    @ResponseBody
    public synchronized String autoCreateCampaign(CreateCampaign campaign){
        StringBuffer sb = new StringBuffer();
        try {
            Map<Object, Object> map = new HashMap<>();
            // ????????????asin??????sku
            String sku = amazonAdAutoMapper.selectSkuByAsin(campaign.getAsin());
            if (!StringUtil.isEmpty(sku)){
                amazonFileService.getAccessToken(grantType,clientId,refreshToken,clientSecret,map);
                doTimeout(map);
                // ??????????????????????????????
                for (int i = 1; i <7 ; i++) {
                    String[] keywords = amazonAdAutoService.getKeyword(campaign, i);
                    String campaignId = null;
                    String adGroupId = null;
                    if (!StringUtil.isEmpty(keywords)){
                        // ??????campaign id
                        doTimeout(map);
                        sb.append(", camName:"+campaign.getCampaignName()+keywords[0]);
                        campaignId = amazonAdAutoService.createCampaign(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
                                , "Bearer " + map.get("access_token"), "?????????????????????campagin ", campaign, campaign.getCampaignName()+" - "+keywords[0]);
                        // ?????? ad group id
                        if (!StringUtil.isEmpty(campaignId)){
                            doTimeout(map);
                            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                            sb.append(", adGroupName:"+"Mass " + date);
                            adGroupId = amazonAdAutoService.createAdGroup(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
                                    , "Bearer " + map.get("access_token"), Long.parseLong(campaignId)
                                    , "?????????????????????adgroup ", "Mass " + date,0.2,"enabled");
                        }
                        // ?????????
                        if (!StringUtil.isEmpty(campaignId) && !StringUtil.isEmpty(adGroupId)){
                            for (String keyword:keywords) {
                                BigDecimal bid = amazonAdAutoService.getBid(campaign, i);
                                String matchType = amazonAdAutoService.getMatchType(campaign, i);
                                doTimeout(map);
                                sb.append("??? ??????????????????"+keyword+", bid:"+bid);
                                amazonAdAutoService.createKeyword(amazonAdvertisingAPIScope,amazonAdvertisingAPIClientId
                                        ,"Bearer " + map.get("access_token"),Long.parseLong(campaignId),Long.parseLong(adGroupId)
                                        ,"?????????????????????keyword ", matchType,keyword, bid);
                            }
                        }
                        // ??????
                        sb.append(", ???????????????sku???"+sku);
                        amazonAdAutoService.createProductAds(amazonAdvertisingAPIScope,amazonAdvertisingAPIClientId
                                , "Bearer " + map.get("access_token"), "????????????"
                                , sku,Long.parseLong(campaignId),Long.parseLong(adGroupId));
                        // ?????????????????????
                        String negativeKeyword = campaign.getNegativeKeyword();
                        if (!StringUtil.isEmpty(negativeKeyword)){
                            String[] split = negativeKeyword.split(",");
                            for (String keywordText:split) {
                                doTimeout(map);
                                sb.append(", ??????????????????"+keywordText);
                                amazonAdAutoService.createNegativeKeywords(amazonAdvertisingAPIScope,amazonAdvertisingAPIClientId
                                        ,"Bearer " + map.get("access_token"),Long.parseLong(campaignId),Long.parseLong(adGroupId)
                                        ,keywordText,"?????????????????????negativeKeyword ");
                            }
                        }
                    }
                }
            }else {
                return "??????,asin?????????sku???"+campaign.getAsin();
            }
            return "???????????????"+sb;
        } catch (Exception e) {
            log.info(e.getMessage());
            return "??????????????????????????????????????????,??????Amazon????????????"+sb;
        }
    }

    /**
     * ??????asin
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
                return "??????????????????asin";
            }
            Map<Object, Object> map = new HashMap<>();
            amazonFileService.getAccessToken(grantType,clientId,refreshToken,clientSecret,map);
//            if (!StringUtil.isEmpty(adAsins)){
//                String campaignId;
//                String adGroupId = null;
//                // ?????? campaign adgroup ????????????????????????adAsin?????????
//                doTimeout(map);
//                sb.append(", camName:##ASIN Target"+campaign.getAsin()+"##"+UUID.randomUUID().toString().substring(0,4));
//                campaignId = amazonAdAutoService.createCampaign(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
//                        , "Bearer " + map.get("access_token"), "?????????????????????campagin ", campaign
//                        , "##ASIN Target"+campaign.getAsin()+"##"+UUID.randomUUID().toString().substring(0,4));
//
//                if (!StringUtil.isEmpty(campaignId)){
//                    doTimeout(map);
//                    sb.append(", adGroupName: ##ASIN Target"+campaign.getAsin());
//                    adGroupId = amazonAdAutoService.createAdGroup(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
//                            , "Bearer " + map.get("access_token"), Long.parseLong(campaignId), "?????????????????????adgroup "
//                            , "##ASIN Target"+campaign.getAsin(),0.2,"enabled");
//                }
//                // ?????????
//                if (!StringUtil.isEmpty(campaignId) && !StringUtil.isEmpty(adGroupId)){
//                    String sku = amazonAdAutoMapper.selectSkuByAsin(campaign.getAsin());
//                    // ??????
//                    doTimeout(map);
//                    amazonAdAutoService.createProductAds(amazonAdvertisingAPIScope,amazonAdvertisingAPIClientId
//                            , "Bearer " + map.get("access_token"), "????????????"
//                            , sku,Long.parseLong(campaignId),Long.parseLong(adGroupId));
//                    // ???
//                    String[] adArr = adAsins.split(",");
//                    sb.append("??? ??????asin???"+adAsins+" ,  bid???"+campaign.getDefBid()+"\n");
//                    for (String adAsin:adArr) {
//                        doTimeout(map);
//                        amazonAdAutoService.createTarget(amazonAdvertisingAPIScope,amazonAdvertisingAPIClientId
//                                ,"Bearer " + map.get("access_token"), "??????ASIN??????", Long.parseLong(campaignId)
//                                , Long.parseLong(adGroupId), adAsin, campaign.getDefBid());
//                    }
//                }
//            }

            String[] adArr = beAdAsins.split(",");
            // ???????????? campaign adgroup ??????????????????asin???beAsin?????????
            for (String beAdAsin:adArr) {
                String campaignId;
                String adGroupId = null;
                // ?????? campaign adgroup ????????????????????????adAsin?????????
                sb.append(", camName:##ASIN Target"+campaign.getAsin()+"##"+UUID.randomUUID().toString().substring(0,4));
                doTimeout(map);
                campaignId = amazonAdAutoService.createCampaign(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
                        , "Bearer " + map.get("access_token"), "?????????????????????campagin ", campaign
                        , "##ASIN Target"+beAdAsin+"##"+ UUID.randomUUID().toString().substring(0,4));
                if (!StringUtil.isEmpty(campaignId)){
                    doTimeout(map);
                    sb.append(", adGroupName: ##ASIN Target"+campaign.getAsin());
                    adGroupId = amazonAdAutoService.createAdGroup(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
                            , "Bearer " + map.get("access_token"), Long.parseLong(campaignId), "?????????????????????adgroup "
                            , "##ASIN Target"+beAdAsin,0.2,"enabled");
                }
                // ?????????
                if (!StringUtil.isEmpty(campaignId) && !StringUtil.isEmpty(adGroupId)){
                    String sku = amazonAdAutoMapper.selectSkuByAsin(beAdAsin);
                    // ??????
                    sb.append("??? ??????asin???"+Arrays.toString(adArr)+" ,  bid???"+campaign.getDefBid()+"\n");
                    if (StringUtil.isEmpty(sku)){
                        return "???????????????????????????asin???"+beAdAsin+"??????????????????sku"+sb;
                    }
                    doTimeout(map);
                    amazonAdAutoService.createProductAds(amazonAdvertisingAPIScope,amazonAdvertisingAPIClientId
                            , "Bearer " + map.get("access_token"), "????????????"
                            , sku,Long.parseLong(campaignId),Long.parseLong(adGroupId));
                    String[] asinSplit = asin.split(",");
                    for (String as:asinSplit) {
                        if (!beAdAsin.equals(as)){
                            doTimeout(map);
                            amazonAdAutoService.createTarget(amazonAdvertisingAPIScope,amazonAdvertisingAPIClientId
                                    ,"Bearer " + map.get("access_token"), "??????ASIN??????", Long.parseLong(campaignId)
                                    , Long.parseLong(adGroupId), as, campaign.getDefBid());
                        }
                    }
                }
            }
            return "???????????????"+sb;
        } catch (Exception e) {
            e.printStackTrace();
            return "??????????????????????????????????????????,??????Amazon????????????"+sb;
        }
    }

    /**
     * ??????????????????
     * @param campaign
     * @return
     */
    @RequestMapping(value = "/autoCampaign")
    @ResponseBody
    public synchronized String autoCampaign(AutoCampaign campaign){
        try {
            // ??????token
            Map<Object, Object> map = new HashMap<>();
            amazonFileService.getAccessToken(grantType,clientId,refreshToken,clientSecret,map);
            // ??????campaign
            String campaignId = null;
            String adGroupId = null;
            if (!StringUtil.isEmpty(campaign.getSku())){
                doTimeout(map);
                campaignId = amazonAdAutoService.createCampaign(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
                        , "Bearer " + map.get("access_token"), "?????????????????????campagin ", campaign
                        , campaign.getCampaignName());
                // ??????adGroup
                if (!StringUtil.isEmpty(campaignId)){
                    doTimeout(map);
                    adGroupId = amazonAdAutoService.createAdGroup(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
                            , "Bearer " + map.get("access_token"), Long.parseLong(campaignId), "?????????????????????adgroup "
                            , campaign.getAdGroupName(),campaign.getDefBid(),campaign.getInitialState());
                }
            }
            // ????????????
            if (!StringUtil.isEmpty(campaignId) && !StringUtil.isEmpty(adGroupId)){
                // ??????
                doTimeout(map);
                amazonAdAutoService.createProductAds(amazonAdvertisingAPIScope,amazonAdvertisingAPIClientId
                        , "Bearer " + map.get("access_token"), "??????????????????"
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
//     * ????????????????????????
//     * @param campaignId
//     * @param adgroupId
//     * @param keyWord
//     * @param ruleName
//     * @param flag 1?????????????????????2?????????asin
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
