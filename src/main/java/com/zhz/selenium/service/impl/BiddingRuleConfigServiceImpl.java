package com.zhz.selenium.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.zhz.selenium.mapper.BiddingRuleConfigMapper;
import com.zhz.selenium.pojo.BiddingRuleConfig;
import com.zhz.selenium.service.AmazonAdAutoService;
import com.zhz.selenium.service.AmazonFileService;
import com.zhz.selenium.service.BiddingRuleConfigService;
import com.zhz.selenium.service.RuleConfigService;
import com.zhz.selenium.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BiddingRuleConfigServiceImpl implements BiddingRuleConfigService {

    Log log = LogFactory.get();

    @Autowired
    private BiddingRuleConfigMapper biddingRuleConfigMapper;

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
    public void saveBiddingRule(BiddingRuleConfig biddingRuleConfig) {
        try {
            biddingRuleConfigMapper.saveBiddingRule(biddingRuleConfig);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @Override
    public List<BiddingRuleConfig> selectBiddingRule() {
        try {
            List<BiddingRuleConfig> biddingRuleConfigs = biddingRuleConfigMapper.selectBiddingRule();
            return biddingRuleConfigs;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    @Override
    public void deleteBiddingRule(Integer id) {
        try {
            biddingRuleConfigMapper.deleteBiddingRule(id);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }


    @Override
    public List<BiddingRuleConfig> selectBiddingRuleDetail(Integer id) {
        List<BiddingRuleConfig> biddingRuleConfigs = biddingRuleConfigMapper.selectBiddingRuleDetail(id);
        return biddingRuleConfigs;
    }

    @Override
    public void executeBiddingRule(String rulePage,Object value) {
        Map<Object, Object> map = new HashMap<>();
        amazonFileService.getAccessToken(grantType,clientId,refreshToken,clientSecret,map);
        List<BiddingRuleConfig> list = (List<BiddingRuleConfig>) value;
        for (BiddingRuleConfig biddingRuleConfig:list) {
            Map<String, List<Map<String, Object>>> stringListMap = ruleConfigService.selectConfigDetailData(rulePage, biddingRuleConfig.getId());
            // 操作
            // 判断天数是否符合日期，为空则处理
            if (!StringUtil.isEmpty(biddingRuleConfig.getPriorty())){
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String shouldDate = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), Integer.parseInt(biddingRuleConfig.getPriorty())));
                if (!shouldDate.equals(biddingRuleConfig.getExecuteTime())){
                    continue;
                }
            }
            for (Map.Entry e:stringListMap.entrySet()) {
                if ("data".equals(StringUtil.toString(e.getKey()))){
                    List<Map<String, Object>> data = (List)e.getValue();
                    for (Map<String, Object> m: data) {
                        // 判断bid值不为空， 则处理
                        if (!StringUtil.isEmpty(m.get("keyword_bid"))){
                            doTimeout(map);
                            switch (biddingRuleConfig.getAdjustBidType()) {
                                case "Target ACOS":
                                    biddingTargetACOS(biddingRuleConfig,m,StringUtil.toString(m.get("keyword_bid")),map);
                                    break;
                                case "Fix Amount":
                                    biddingFixAmount(biddingRuleConfig,m,map);
                                    break;
                                case "Percentage":
                                    biddingPercentage(biddingRuleConfig,m,StringUtil.toString(m.get("keyword_bid")),map);
                                    break;
                                default:
                                    break;
                                }
                        }
                    }
                }
            }
        }


    }

    /**
     * bid直接乘1加减百分比
     * @param biddingRuleConfig
     * @param m 数据
     * @param bid
     * @param map token
     */
    private void biddingTargetACOS(BiddingRuleConfig biddingRuleConfig,Map<String, Object> m,String bid,Map<Object, Object> map){
        BigDecimal targetAcos = biddingRuleConfig.getTargetAcos();
        BigDecimal tAcos = new BigDecimal(StringUtil.toString(m.get("t_acos")));
        BigDecimal decBid = new BigDecimal(bid);
        if (!StringUtil.isEmpty(targetAcos) && targetAcos.stripTrailingZeros().compareTo(tAcos)<0){
            if (!StringUtil.isEmpty(biddingRuleConfig.getMaxBidDecrease())){
                decBid=decBid.subtract(decBid.multiply(biddingRuleConfig.getMaxBidDecrease()));
            }else {
                decBid=decBid.add(decBid.multiply(biddingRuleConfig.getMaxBidDecrease()));
            }
            BigDecimal resultBid = judge(biddingRuleConfig, decBid);
            // 更新api
            amazonAdAutoService.updateKeywordBid(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
                    , "Bearer " + map.get("access_token"), Long.parseLong(StringUtil.toString(m.get("keyword_id")))
                    ,biddingRuleConfig.getActionName(),resultBid);
            biddingRuleConfigMapper.updateExeDate(biddingRuleConfig.getId(),new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        }
    }

    /**
     * bid 直接变成所填的
     * @param biddingRuleConfig
     * @param m
     * @param map
     */
    private void biddingFixAmount(BiddingRuleConfig biddingRuleConfig,Map<String, Object> m,Map<Object, Object> map){
        if (!StringUtil.isEmpty(biddingRuleConfig.getBidAdjustment())){
            amazonAdAutoService.updateKeywordBid(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
                    , "Bearer " + map.get("access_token"), Long.parseLong(StringUtil.toString(m.get("keyword_id")))
                    ,biddingRuleConfig.getActionName(),biddingRuleConfig.getBidAdjustment());
            biddingRuleConfigMapper.updateExeDate(biddingRuleConfig.getId(),new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        }
    }

    /**
     * bid 直接乘百分比
     * @param biddingRuleConfig
     * @param m
     * @param bid
     * @param map
     */
    private void biddingPercentage(BiddingRuleConfig biddingRuleConfig,Map<String, Object> m,String bid,Map<Object, Object> map){
        BigDecimal bidAdjustment = biddingRuleConfig.getBidAdjustment();
        BigDecimal decBid = new BigDecimal(bid);
        if (!StringUtil.isEmpty(bidAdjustment)){
            decBid = decBid.multiply(bidAdjustment.divide(new BigDecimal(100)));
            BigDecimal resultBid = judge(biddingRuleConfig, decBid);
            // 更新api
            amazonAdAutoService.updateKeywordBid(amazonAdvertisingAPIScope, amazonAdvertisingAPIClientId
                    , "Bearer " + map.get("access_token"), Long.parseLong(StringUtil.toString(m.get("keyword_id")))
                    ,biddingRuleConfig.getActionName(),resultBid);
            biddingRuleConfigMapper.updateExeDate(biddingRuleConfig.getId(),new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        }

    }

    // 判断值是否满足区间内 或者区间外
    private BigDecimal judge(BiddingRuleConfig biddingRuleConfig,BigDecimal decBid){
        if (!StringUtil.isEmpty(biddingRuleConfig.getMinBid()) && biddingRuleConfig.getMinBid().compareTo(decBid)>0){
            return biddingRuleConfig.getMinBid();
        }
        if (!StringUtil.isEmpty(biddingRuleConfig.getMaxBid()) && biddingRuleConfig.getMaxBid().compareTo(decBid)<0){
            return biddingRuleConfig.getMaxBid();
        }
        return decBid;
    }


    private void doTimeout(Map<Object, Object> map){
        if ((Long)map.get("endTime")-new Date().getTime()<=0){
            amazonFileService.getAccessToken(grantType,clientId,refreshToken,clientSecret,map);
        }
    }
}
