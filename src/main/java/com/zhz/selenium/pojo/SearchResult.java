package com.zhz.selenium.pojo;

import lombok.Data;

@Data
public class SearchResult {
    private Integer id;
    private Integer searchId;
    private Integer keyWordId;
    private String amzUrl;
    private String amzProductName;
    private String amzProductPrice;
    private String amzAsin;
    private Integer amzProductStyle;
    private Integer amzProductPage;
    private Integer amzProductIndex;
    private String amzProductImg;
    private String amzProductStar;
    private String amzProductReviewNum;
    private Integer amzProductDel;
    private Integer count;
    private Integer normalCount;
    private Integer notNormalCount;
    private String searchDate;
    private String amzDoublePrice;

    public SearchResult() {
    }

    public SearchResult(String amzAsin, String normalCount,String notNormalCount) {
    }
}
