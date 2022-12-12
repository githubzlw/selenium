package com.zhz.selenium.pojo;

import lombok.Data;

@Data
public class Product {
    private Integer id;
    private Integer searchId;
    private Integer keyWordId;
    private String productAsin;
    private Integer productPage;
    private Integer productIndex;
    private Integer productState;
    private Integer count;
    private Integer normalCount;
    private Integer notNormalCount;
    private String keyWord;
    private String searchDate;
    private String productImg;
}
