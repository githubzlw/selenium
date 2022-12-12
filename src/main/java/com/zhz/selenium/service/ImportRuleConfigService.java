package com.zhz.selenium.service;


import com.zhz.selenium.pojo.ImportRuleConfig;

import java.util.List;

public interface ImportRuleConfigService {
    void saveImportRule(ImportRuleConfig importRuleConfig);

    List<ImportRuleConfig> selectImportRule();

    void deleteImportRule(Integer id);

    void executeImportRule(String rulePage,Object value);

    List<ImportRuleConfig> selectImportRuleDetail(Integer id);
}
