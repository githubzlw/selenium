package com.zhz.selenium.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class NegativeRuleConfig {
    private Integer id;

    @JSONField(name="action_name")
    private String actionName;

    @JSONField(name="action_description")
    private String actionRemark;

    @JSONField(name="config_id")
    private Integer confId;

    @JSONField(name="search_terms_source")
    private String searchTermsSource;

    @JSONField(name="scope")
    private String scope;

    @JSONField(name="add_to_blacklist")
    private Integer addToBlacklist;

    @JSONField(name="ignore_whitelist")
    private Integer ignoreWhitelist;

    @JSONField(name="new_negative_type")
    private String newNegativeType;

    private String ruleName;

    private String condition;
}
