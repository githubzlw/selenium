package com.zhz.selenium.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StatusRuleConfig {
    private Integer id;

    @JSONField(name="action_name")
    private String actionName;

    @JSONField(name="action_description")
    private String actionRemark;

    @JSONField(name="config_id")
    private Integer confId;

    @JSONField(name="new_campaign_status")
    private String newCampaignStatus;

    @JSONField(name="new_ad_group_status")
    private String newAdGroupStatus;

    @JSONField(name="priority")
    private BigDecimal priority;

    @JSONField(name="search_terms_source")
    private String searchTermsSource;

    private String ruleName;

    private String condition;
}
