package com.zhz.selenium.service;


import com.zhz.selenium.pojo.NegativeRuleConfig;

import java.util.List;

public interface NegativeRuleConfigService {
    void saveNegativeRule(NegativeRuleConfig negativeRuleConfig);

    List<NegativeRuleConfig> selectNegativeRule();

    void deleteNegativeRule(Integer id);

    void executeNegativeRule(String pageRule,Object value);

    List<NegativeRuleConfig> selectNegativeRuleDetail(Integer id);
}
