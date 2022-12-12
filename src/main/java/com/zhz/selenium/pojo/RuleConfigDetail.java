package com.zhz.selenium.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.Value;

import java.math.BigDecimal;

@Data
public class RuleConfigDetail {

    private Integer id;

    @JSONField(name="key_id")
    private Integer keyId;

    @JSONField(name="impression_min")
    private BigDecimal impressionMin;

    @JSONField(name="impression_max")
    private BigDecimal impressionMax;

    @JSONField(name="clicks_min")
    private BigDecimal clicksMin;

    @JSONField(name="clicks_max")
    private BigDecimal clicksMax;

    @JSONField(name="orders_min")
    private BigDecimal ordersMin;

    @JSONField(name="orders_max")
    private BigDecimal ordersMax;

    @JSONField(name="units_min")
    private BigDecimal unitsMin;

    @JSONField(name="units_max")
    private BigDecimal unitsMax;

    @JSONField(name="cpc_min")
    private BigDecimal cpcMin;

    @JSONField(name="cpc_max")
    private BigDecimal cpcMax;

    @JSONField(name="ctr_min")
    private BigDecimal ctrMin;

    @JSONField(name="ctr_max")
    private BigDecimal ctrMax;

    @JSONField(name="conversion_min")
    private BigDecimal conversionMin;

    @JSONField(name="conversion_max")
    private BigDecimal conversionMax;

    @JSONField(name="acos_min")
    private BigDecimal acosMin;

    @JSONField(name="acos_max")
    private BigDecimal acosMax;

    @JSONField(name="roas_min")
    private BigDecimal roasMin;

    @JSONField(name="roas_max")
    private BigDecimal roasMax;

    @JSONField(name="spend_min")
    private BigDecimal spendMin;

    @JSONField(name="spend_max")
    private BigDecimal spendMax;

    @JSONField(name="sales_min")
    private BigDecimal salesMin;

    @JSONField(name="sales_max")
    private BigDecimal salesMax;

    @JSONField(name="date_during")
    private String dateDuring;

}
