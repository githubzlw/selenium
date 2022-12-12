package com.zhz.selenium.service;



import com.zhz.selenium.pojo.StatusRuleConfig;

import java.util.List;

public interface StatusRuleConfigService {
    void saveStatusRule(StatusRuleConfig statusRuleConfig);

    List<StatusRuleConfig> selectStatusRule();

    void deleteStatusRule(Integer id);

    void executeStatusRule(String rulePage,Object value);

    List<StatusRuleConfig> selectStatusRuleDetail(Integer id);
}
