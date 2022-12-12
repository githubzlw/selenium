package com.zhz.selenium.pojo;

import lombok.Data;

@Data
public class CompareProduct {
    private Integer id;
    private Integer keyWordId;
    private String amzProductAsin;
    private String amzProductCompeteId;
    private Integer amzProductDel;
    private String delDate;
    private String amzProductCompeteAsin;
}
