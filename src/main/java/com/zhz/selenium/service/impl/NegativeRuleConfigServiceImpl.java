package com.zhz.selenium.service.impl;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.zhz.selenium.mapper.NegativeRuleConfigMapper;
import com.zhz.selenium.pojo.BiddingRuleConfig;
import com.zhz.selenium.pojo.NegativeRuleConfig;
import com.zhz.selenium.pojo.NegativeWordConfig;
import com.zhz.selenium.service.AmazonAdAutoService;
import com.zhz.selenium.service.AmazonFileService;
import com.zhz.selenium.service.NegativeRuleConfigService;
import com.zhz.selenium.service.RuleConfigService;
import com.zhz.selenium.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NegativeRuleConfigServiceImpl implements NegativeRuleConfigService {

    Log log = LogFactory.get();

    @Autowired
    private NegativeRuleConfigMapper negativeRuleConfigMapper;

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
    public void saveNegativeRule(NegativeRuleConfig negativeRuleConfig) {
        try {
            negativeRuleConfigMapper.saveNegativeRule(negativeRuleConfig);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @Override
    public List<NegativeRuleConfig> selectNegativeRule() {
        try {
            List<NegativeRuleConfig> negativeRuleConfigs = negativeRuleConfigMapper.selectNegativeRule();
            return negativeRuleConfigs;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    @Override
    public void deleteNegativeRule(Integer id) {
        try {
            negativeRuleConfigMapper.deleteNegativeRule(id);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }


    @Override
    public List<NegativeRuleConfig> selectNegativeRuleDetail(Integer id) {
        List<NegativeRuleConfig> negativeRuleConfigs = negativeRuleConfigMapper.selectNegativeRuleDetail(id);
        return negativeRuleConfigs;
    }

    @Override
    public void executeNegativeRule(String pageRule, Object value) {
        Map<Object, Object> map = new HashMap<>();
        List<NegativeRuleConfig> list = (List<NegativeRuleConfig>) value;
        amazonFileService.getAccessToken(grantType,clientId,refreshToken,clientSecret,map);
        for (NegativeRuleConfig negativeRuleConfig:list) {
            Map<String, List<Map<String, Object>>> stringListMap = ruleConfigService.selectConfigDetailData(pageRule, negativeRuleConfig.getId());
            // 操作
            for (Map.Entry e:stringListMap.entrySet()) {
                if (!"data".equals(StringUtil.toString(e.getKey()))){
                    List<Map<String, Object>> data = (List)e.getValue();
                    for (Map<String, Object> m: data) {
                        doTimeout(map);
                        if("asin".equals(negativeRuleConfig.getSearchTermsSource())){
                            amazonAdAutoService.createNegativeAsin(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
                                    , "Bearer " + map.get("access_token"), Long.parseLong(StringUtil.toString(m.get("campaign_id")))
                                    ,Long.parseLong(StringUtil.toString(m.get("ad_group_id"))),StringUtil.toString(e.getKey())
                                    ,negativeRuleConfig.getActionName());
                        }else {
                            amazonAdAutoService.createNegativeKeywords(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
                                    , "Bearer " + map.get("access_token"), Long.parseLong(StringUtil.toString(m.get("campaign_id")))
                                    ,Long.parseLong(StringUtil.toString(m.get("ad_group_id"))),StringUtil.toString(e.getKey())
                                    ,negativeRuleConfig.getActionName());
                        }
                    }
                }
            }
        }
    }

    private void doTimeout(Map<Object, Object> map){
        if ((Long)map.get("endTime")-new Date().getTime()<=0){
            amazonFileService.getAccessToken(grantType,clientId,refreshToken,clientSecret,map);
        }
    }
}
