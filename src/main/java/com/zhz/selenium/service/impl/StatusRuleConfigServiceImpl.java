package com.zhz.selenium.service.impl;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.zhz.selenium.mapper.StatusRuleConfigMapper;
import com.zhz.selenium.pojo.BiddingRuleConfig;
import com.zhz.selenium.pojo.StatusRuleConfig;
import com.zhz.selenium.service.AmazonAdAutoService;
import com.zhz.selenium.service.AmazonFileService;
import com.zhz.selenium.service.RuleConfigService;
import com.zhz.selenium.service.StatusRuleConfigService;
import com.zhz.selenium.util.StringUtil;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatusRuleConfigServiceImpl implements StatusRuleConfigService {

    Log log = LogFactory.get();

    @Autowired
    private StatusRuleConfigMapper statusRuleConfigMapper;

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
    public void saveStatusRule(StatusRuleConfig statusRuleConfig) {
        try {
            statusRuleConfigMapper.saveStatusRule(statusRuleConfig);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @Override
    public List<StatusRuleConfig> selectStatusRule() {
        try {
            List<StatusRuleConfig> statusRuleConfigs = statusRuleConfigMapper.selectStatusRule();
            return statusRuleConfigs;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    @Override
    public void deleteStatusRule(Integer id) {
        try {
            statusRuleConfigMapper.deleteStatusRule(id);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @Override
    public List<StatusRuleConfig> selectStatusRuleDetail(Integer id) {
        List<StatusRuleConfig> statusRuleConfigs = statusRuleConfigMapper.selectStatusRuleDetail(id);
        return statusRuleConfigs;
    }

    @Override
    public void executeStatusRule(String rulePage, Object value) {
        Map<Object, Object> map = new HashMap<>();
        List<StatusRuleConfig> list = (List<StatusRuleConfig>) value;
        for (StatusRuleConfig statusRuleConfig:list) {
            Map<String, List<Map<String, Object>>> stringListMap = ruleConfigService.selectConfigDetailData(rulePage, statusRuleConfig.getId());
            // 操作
            for (Map.Entry e:stringListMap.entrySet()) {
                if ("data".equals(StringUtil.toString(e.getKey()))){
                    List<Map<String, Object>> data = (List)e.getValue();
                    for (Map<String, Object> m: data) {
                        doTimeout(map);
                        if("campaign_id".equals(statusRuleConfig.getSearchTermsSource())){
                            amazonAdAutoService.updateCampaignStatus(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
                                    , "Bearer " + map.get("access_token"), Long.parseLong(StringUtil.toString(m.get("campaign_id")))
                                    ,statusRuleConfig.getActionName());
                        }else {
                            amazonAdAutoService.updateAdgroupStatus(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
                                    , "Bearer " + map.get("access_token"), Long.parseLong(StringUtil.toString(m.get("ad_group_id")))
                                    ,statusRuleConfig.getActionName());
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
