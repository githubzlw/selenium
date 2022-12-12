package com.zhz.selenium.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class NegativeWordConfig {
    private Integer id;

    @JSONField(name="action_name")
    private String actionName;

    @JSONField(name="action_description")
    private String actionRemark;

    @JSONField(name="config_id")
    private Integer confId;

    @JSONField(name="search_terms_source")
    private String searchTermsSource;

    @JSONField(name="add_to_blacklist")
    private Integer addToBlacklist;

    @JSONField(name="ignore_whitelist")
    private Integer ignoreWhitelist;

    private String ruleName;

    private String condition;
}
