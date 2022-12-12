package com.zhz.selenium.mapper;

import com.zhz.selenium.pojo.BiddingRuleConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BiddingRuleConfigMapper {

    void saveBiddingRule(BiddingRuleConfig biddingRuleConfig);

    List<BiddingRuleConfig> selectBiddingRule();

    void deleteBiddingRule(@Param(value = "id") Integer id);

    void updateExeDate(@Param(value = "id")Integer id,@Param(value = "time")String time);

    List<BiddingRuleConfig> selectBiddingRuleDetail(@Param(value = "id") Integer id);
}
