package com.zhz.selenium.mapper;

import com.zhz.selenium.pojo.ImportRuleConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ImportRuleConfigMapper {

    void saveImportRule(ImportRuleConfig importRuleConfig);

    List<ImportRuleConfig> selectImportRule();

    void deleteImportRule(@Param(value = "id") Integer id);

    List<Map<String,Object>> selectDictionary();

    void insertAdGroupId(@Param(value = "campaignId") String campaignId,@Param(value = "adGroupId") String adGroupId);

    List<ImportRuleConfig> selectImportRuleDetail(@Param(value = "id") Integer id);
}
