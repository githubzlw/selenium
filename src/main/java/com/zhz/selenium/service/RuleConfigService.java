package com.zhz.selenium.service;

import com.zhz.selenium.pojo.RuleConfig;
import com.zhz.selenium.pojo.RuleConfigDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RuleConfigService {
    void saveConfig(RuleConfig ruleConfig, RuleConfigDetail ruleConfigDetail);

    List<RuleConfig> selectConfig();

    void deleteConfig(Integer id);

    List<RuleConfigDetail> selectConfigDetail(Integer id);

    void updateConfig(RuleConfig ruleConfig, RuleConfigDetail ruleConfigDetail);

    Map<String, List<Map<String, Object>>> selectConfigDetailData(String rulePage,Integer rulePageId);
}
