package com.zhz.selenium.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
public class AutoCampaign {
    private int id;

    private String campaignName;

    private String sku;
    private String asin;

    private String PortfolioId;

    private Double dailyBudget;

    private String startDate;
    private String endDate;

    //投标策略 key
    private String biddingStrategyKey;
    // 投标策略 value
    private Map<String,String> biddingStrategy = new HashMap<String, String>(){{
        put("Dynamic Down Only","legacyForSales");
        put("Dynamic Up Down","autoForSales");
        put("Fixed","manual");
    }
    };

    private String adGroupName;

    private Double defBid;

    private String initialState;

    // top加价百分比值
    private Double biddingAdjustmentsTopPercentage;
    // page加价百分比值
    private Double biddingAdjustmentsPagePercentage;
}
