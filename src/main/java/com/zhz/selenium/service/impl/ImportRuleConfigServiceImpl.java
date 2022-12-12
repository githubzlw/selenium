package com.zhz.selenium.service.impl;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.zhz.selenium.mapper.BiddingRuleConfigMapper;
import com.zhz.selenium.mapper.ImportRuleConfigMapper;
import com.zhz.selenium.pojo.BiddingRuleConfig;
import com.zhz.selenium.pojo.ImportRuleConfig;
import com.zhz.selenium.service.*;
import com.zhz.selenium.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImportRuleConfigServiceImpl implements ImportRuleConfigService {

    Log log = LogFactory.get();

    @Autowired
    private ImportRuleConfigMapper importRuleConfigMapper;

    @Autowired
    private RuleConfigService ruleConfigService;

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
    public void saveImportRule(ImportRuleConfig importRuleConfig) {
        try {
            importRuleConfigMapper.saveImportRule(importRuleConfig);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @Override
    public List<ImportRuleConfig> selectImportRule() {
        try {
            List<ImportRuleConfig> importRuleConfig = importRuleConfigMapper.selectImportRule();
            return importRuleConfig;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    @Override
    public void deleteImportRule(Integer id) {
        try {
            importRuleConfigMapper.deleteImportRule(id);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }


    @Override
    public List<ImportRuleConfig> selectImportRuleDetail(Integer id) {
        List<ImportRuleConfig> importRuleConfigs = importRuleConfigMapper.selectImportRuleDetail(id);
        return importRuleConfigs;
    }

    /**
     *  先字典表--》 dictionary map中 map(campaign_id,ad_group_id)
     *  比较map是否有 campaign id
     *  有：直接拿到adgroup id 创建keyword
     *  没有： 新建一个adgroup id 插入字典表，插入map，新建keyword
     * @param rulePage
     * @param value
     */
    @Override
    public void executeImportRule(String rulePage, Object value) {
        Map<Object, Object> map = new HashMap<>();
        amazonFileService.getAccessToken(grantType,clientId,refreshToken,clientSecret,map);
        List<ImportRuleConfig> list = (List<ImportRuleConfig>) value;
        // 处理字典表
        Map<String, Object> dictionaryData = dealDictionaryData();
        for (ImportRuleConfig importRuleConfig:list) {
            Map<String, List<Map<String, Object>>> stringListMap = ruleConfigService.selectConfigDetailData(rulePage, importRuleConfig.getId());
            // 操作
            for (Map.Entry e:stringListMap.entrySet()) {
                if ("data".equals(StringUtil.toString(e.getKey()))){
                    List<Map<String, Object>> data = (List)e.getValue();
                    for (Map<String, Object> m: data) {
                        // cpc*cpc% = bid  为空不处理
                        if (!StringUtil.isEmpty(importRuleConfig.getAverageCpc())){
                            String tCpc = StringUtil.toString(m.get("t_cpc"));
                            BigDecimal bid = new BigDecimal(tCpc).multiply(new BigDecimal(importRuleConfig.getAverageCpc()));
                            BigDecimal resultBid = judge(importRuleConfig, bid);
                            doTimeout(map);
                            if (dictionaryData.containsKey(StringUtil.toString(m.get("campaign_id")))){
                                amazonAdAutoService.createKeyword(amazonAdvertisingAPIScope,amazonAdvertisingAPIClientId
                                        ,"Bearer " + map.get("access_token"),Long.parseLong(StringUtil.toString(m.get("campaign_id")))
                                        ,Long.parseLong(StringUtil.toString(m.get("ad_group_id"))), importRuleConfig.getRuleName()
                                        , importRuleConfig.getImportMatchType(), StringUtil.toString(m.get("Customer_Search_Term"))
                                        , resultBid);
                            }else {
                                String adGroupId = amazonAdAutoService.createAdGroup(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
                                        , "Bearer " + map.get("access_token"), Long.parseLong(StringUtil.toString(m.get("campaign_id")))
                                        , importRuleConfig.getRuleName(),"ImportRule规则组",0.2,"enabled");
                                if (!StringUtil.isEmpty(adGroupId)){
                                    try {
                                        importRuleConfigMapper.insertAdGroupId(StringUtil.toString(m.get("campaign_id"))
                                                ,StringUtil.toString(dictionaryData.get(StringUtil.toString(m.get("campaign_id")))));
                                        dictionaryData.put(StringUtil.toString(m.get("campaign_id")),adGroupId);
                                        amazonAdAutoService.createKeyword(amazonAdvertisingAPIScope,amazonAdvertisingAPIClientId
                                                ,"Bearer " + map.get("access_token"),Long.parseLong(StringUtil.toString(m.get("campaign_id")))
                                                ,Long.parseLong(adGroupId), importRuleConfig.getRuleName()
                                                , importRuleConfig.getImportMatchType(), StringUtil.toString(m.get("Customer_Search_Term"))
                                                , resultBid);
                                    } catch (Exception ex) {
                                        log.info(ex.getMessage());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private Map<String,Object> dealDictionaryData(){
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> maps = importRuleConfigMapper.selectDictionary();
        for (Map<String, Object> map:maps) {
            for (Map.Entry e:map.entrySet()) {
                if (!StringUtil.isEmpty(e.getKey())){
                    result.put(e.getKey().toString(),e.getValue());
                }
            }
        }
        return result;
    }

    // 判断值是否满足区间内 或者区间外
    private BigDecimal judge(ImportRuleConfig importRuleConfig,BigDecimal decBid){
        if (!StringUtil.isEmpty(importRuleConfig.getMinBid()) && importRuleConfig.getMinBid().compareTo(decBid)>0){
            return importRuleConfig.getMinBid();
        }
        if (!StringUtil.isEmpty(importRuleConfig.getMaxBid()) && importRuleConfig.getMaxBid().compareTo(decBid)<0){
            return importRuleConfig.getMaxBid();
        }
        return decBid;
    }


    private void doTimeout(Map<Object, Object> map){
        if ((Long)map.get("endTime")-new Date().getTime()<=0){
            amazonFileService.getAccessToken(grantType,clientId,refreshToken,clientSecret,map);
        }
    }
}
