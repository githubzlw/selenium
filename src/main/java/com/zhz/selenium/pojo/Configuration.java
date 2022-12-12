package com.zhz.selenium.pojo;

/**
 * Copyright 2022 json.cn
 */
import lombok.Data;

import java.util.List;

/**
 * Auto-generated: 2022-08-09 9:51:23
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
@Data
public class Configuration {

    private String adProduct;
    private List<String> groupBy;
    private List<String> columns;
    private String reportTypeId;
    private String timeUnit;
    private String format;

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }
    public List<String> getColumns() {
        return columns;
    }

    public void setFormat(String format) {
        this.format = format;
    }
    public String getFormat() {
        return format;
    }

}
