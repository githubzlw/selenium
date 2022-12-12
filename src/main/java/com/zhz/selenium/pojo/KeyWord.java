package com.zhz.selenium.pojo;

import lombok.Data;

@Data
public class KeyWord {
    private Integer id;
    private String keyWord;
    private String adminuser;
    private String lastSearchDate;
    private Integer searchCount;
    private Integer useState;
}
