package com.zhz.selenium.service;


import com.zhz.selenium.pojo.BiddingRuleConfig;

import java.util.List;

public interface BiddingRuleConfigService {
    void saveBiddingRule(BiddingRuleConfig biddingRuleConfig);

    List<BiddingRuleConfig> selectBiddingRule();

    void deleteBiddingRule(Integer id);

    void executeBiddingRule(String rulePage,Object value);

    List<BiddingRuleConfig> selectBiddingRuleDetail(Integer id);
}
