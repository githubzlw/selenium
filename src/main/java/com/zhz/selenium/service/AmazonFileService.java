package com.zhz.selenium.service;

import java.util.Map;

public interface AmazonFileService {
    String getAccessToken(String grantType, String clientId, String refreshToken, String clientSecret, Map<Object, Object> map);

    String getSearchTermReportId(String contentType,String amazonAdvertisingAPIScope,String amazonAdvertisingAPIClientId,String authorization
            ,String searchFields) throws InterruptedException;

    void getSearchTermReport(String contentType,String amazonAdvertisingAPIScope,String amazonAdvertisingAPIClientId,String authorization,String reportId) throws InterruptedException;

    String getAmzReportId(String contentType,String amazonAdvertisingAPIScope,String amazonAdvertisingAPIClientId,String authorization
            ,String searchFields);

    void getAmzReport(String contentType,String amazonAdvertisingAPIScope,String amazonAdvertisingAPIClientId,String authorization,String reportId) throws InterruptedException;

    void getCampaignReport(String amazonAdvertisingAPIScope,String amazonAdvertisingAPIClientId,String authorization) throws InterruptedException;

    void getAdgroupReport(String amazonAdvertisingAPIScope, String amazonAdvertisingAPIClientId, String authorization);

    void getNegativeKeyWordReport(String amazonAdvertisingAPIScope, String amazonAdvertisingAPIClientId, String authorization);

    void getImportPortfoliosReport(String amazonAdvertisingAPIScope, String amazonAdvertisingAPIClientId, String authorization);
}
