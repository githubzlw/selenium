package com.zhz.selenium.util;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
@ConfigurationProperties(prefix = "ids")
public class CfgPrefixPlatIds {
    private List<Map<String,String>> platIds;
    public List<Map<String, String>> getPlatIds() {
        return platIds;
    }
    public void setPlatIds(List<Map<String, String>> platIds) {
        this.platIds = platIds;
    }
}
