package com.zhz.selenium.mapper;

import com.zhz.selenium.pojo.NegativeRuleConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NegativeRuleConfigMapper {

    void saveNegativeRule(NegativeRuleConfig negativeRuleConfig);

    List<NegativeRuleConfig> selectNegativeRule();

    void deleteNegativeRule(@Param(value = "id") Integer id);

    List<NegativeRuleConfig> selectNegativeRuleDetail(@Param(value = "id") Integer id);
}
