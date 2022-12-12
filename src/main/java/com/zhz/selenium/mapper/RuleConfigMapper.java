package com.zhz.selenium.mapper;

import com.zhz.selenium.pojo.RuleConfig;
import com.zhz.selenium.pojo.RuleConfigDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RuleConfigMapper {
    Integer saveRuleConfig(RuleConfig ruleConfig);

    void saveRuleConfigDetail(RuleConfigDetail ruleConfigDetail);

    List<RuleConfig> selectConfig();

    void deleteConfig(@Param(value = "id") Integer id,@Param(value = "date") String date);

    List<RuleConfigDetail> selectConfigDetail(@Param(value = "id")Integer id);

    void updateConfig(RuleConfig ruleConfig);

    void deleteConfigDetail(@Param(value = "id")Integer id);

    List<Map<String,Object>> selectConfigDetailData(@Param(value = "field")String field,@Param(value = "table")String table
            ,@Param(value = "where")String where,@Param(value = "group")String group,@Param(value = "having")String having);

    Map<String,Object> selectConfigIdByRuleId(@Param(value = "field")String field
            ,@Param(value = "table")String table,@Param(value = "id")Integer id);

    List<Map<String,Object>> selectKeywordInfluenceData(@Param(value = "searchTerm")Object searchTerm, @Param(value = "startTime")String influenceStartTime, @Param(value = "endTime")String endTime);

    List<Map<String,Object>> selectTargetingInfluenceData(@Param(value = "targeting")Object targeting, @Param(value = "startTime")String influenceStartTime, @Param(value = "endTime")String endTime);
}
