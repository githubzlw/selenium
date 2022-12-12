package com.zhz.selenium.service;

import io.swagger.client.api.ReportsApi;
import io.swagger.client.model.CreateReportSpecification;

public interface AmazonSpapiService {
    String getSpapiReportId(ReportsApi reportsApi, CreateReportSpecification body);

    String getSpapiReportDocumentId(ReportsApi reportsApi,String reportId) throws InterruptedException;

    String getSpapiReportUrl(ReportsApi reportsApi, String spapiReportDocumentId);

    void getSpapiReport(ReportsApi reportsApi, String url,String time) throws InterruptedException;

    void getSpapiPriceReport(ReportsApi reportsApi, String url);
}
