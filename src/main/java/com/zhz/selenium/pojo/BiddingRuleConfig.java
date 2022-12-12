package com.zhz.selenium.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BiddingRuleConfig {
    private Integer id;

    @JSONField(name="action_name")
    private String actionName;

    @JSONField(name="action_description")
    private String actionRemark;

    @JSONField(name="keyword_conf_id")
    private Integer keywordConfId;

    @JSONField(name="ad_group_conf_id")
    private Integer adgroupConfId;

    @JSONField(name="campaign_conf_id")
    private Integer campaignConfId;

    @JSONField(name="priorty")
    private String priorty;

    @JSONField(name="min_bid")
    private BigDecimal minBid;

    @JSONField(name="max_bid")
    private BigDecimal maxBid;

    @JSONField(name="days_obj_bid")
    private Integer daysObjBid;

    @JSONField(name="adjust_bid_type")
    private String adjustBidType;

    @JSONField(name="target_acos")
    private BigDecimal targetAcos;

    @JSONField(name="bid_use_per_click")
    private Integer bidUsePerClick;

    @JSONField(name="max_bid_decrease")
    private BigDecimal maxBidDecrease;

    @JSONField(name="max_bid_increase")
    private BigDecimal maxBidIncrease;

    @JSONField(name="bid_adjustment")
    private BigDecimal bidAdjustment;

    private String ruleName;

    private String condition;

    private String executeTime;
}
