package com.zhz.selenium.pojo;

/**
 * Copyright 2022 json.cn
 */
import lombok.Data;

import java.util.Date;

/**
 * Auto-generated: 2022-08-09 9:51:23
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
@Data
public class JsonRootBean {

    private String startDate;
    private String endDate;
    private Configuration configuration;

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
    public Configuration getConfiguration() {
        return configuration;
    }

}