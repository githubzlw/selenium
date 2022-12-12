package com.zhz.selenium.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
public class CreateCampaign {
    private String asin;

    private String campaignName;
    // 类型：手动
    private String targetingType = "manual";
    // 默认bid
    private BigDecimal defBid;
    // 加价类型百分比
    private String biddingAdjustmentsPredicate = "placementTop";
    // 加价百分比值
    private Double biddingAdjustmentsPercentage;

    //投标策略 key
    private String biddingStrategyKey;
    // 投标策略 value
    private Map<String,String> biddingStrategy = new HashMap<String, String>(){{
            put("Dynamic Down Only","legacyForSales");
            put("Dynamic Up Down","autoForSales");
            put("Fixed","manual");
        }
    };
    // keyword 匹配类型
    private String matchType1;
    private String matchType2;
    private String matchType3;
    private String matchType4;
    private String matchType5;
    private String matchType6;

    // 组关键词
    private String campaignKeyword1;
    private String campaignKeyword2;
    private String campaignKeyword3;
    private String campaignKeyword4;
    private String campaignKeyword5;
    private String campaignKeyword6;

    // 每个组的词分别多少钱
    private String bid1;
    private String bid2;
    private String bid3;
    private String bid4;
    private String bid5;
    private String bid6;

    // 否定关键词（适用于所有组）
    private String negativeKeyword;

    private Double dailyBudget;
    // yyyyMMdd
    private String startDate;
    private String endDate;
    // 默认空
    private String PortfolioId;

    // 打到哪些asin
    private String adAsins;

    // 哪些asin打到这里
    private String beAdAsins;


}
