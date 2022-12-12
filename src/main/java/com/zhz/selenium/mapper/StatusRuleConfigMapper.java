package com.zhz.selenium.mapper;

import com.zhz.selenium.pojo.StatusRuleConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusRuleConfigMapper {

    void saveStatusRule(StatusRuleConfig statusRuleConfig);

    List<StatusRuleConfig> selectStatusRule();

    void deleteStatusRule(@Param(value = "id") Integer id);

    List<StatusRuleConfig> selectStatusRuleDetail(@Param(value = "id") Integer id);
}
