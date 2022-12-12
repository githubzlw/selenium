package com.zhz.selenium.pojo;

import lombok.Data;

@Data
public class Search {
    private Integer id;
    private Integer keyWordId;
    private String searchDate;
    private int successState;

    public Search(Integer keyWordId,String searchDate,int successState){
        this.keyWordId = keyWordId;
        this.searchDate= searchDate;
        this.successState = successState;
    }
}
