package com.zhz.selenium.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ImportRuleConfig {
    private Integer id;

    @JSONField(name="action_name")
    private String actionName;

    @JSONField(name="action_description")
    private String actionRemark;

    @JSONField(name="config_id")
    private Integer confId;

    @JSONField(name="search_terms_source")
    private String searchTermsSource;

    @JSONField(name="min_bid")
    private BigDecimal minBid;

    @JSONField(name="max_bid")
    private BigDecimal maxBid;

    @JSONField(name="import_match_type")
    private String importMatchType;

    @JSONField(name="average_cpc")
    private String averageCpc;

    private String ruleName;

    private String condition;
}
