package com.zhz.selenium.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSONObject;
import com.zhz.selenium.mapper.AmazonFileMapper;
import com.zhz.selenium.service.AmazonSpapiService;
import com.zhz.selenium.util.ReportUtils;
import com.zhz.selenium.util.StringUtil;
import io.swagger.client.api.ReportsApi;
import io.swagger.client.model.CreateReportResponse;
import io.swagger.client.model.CreateReportSpecification;
import io.swagger.client.model.Report;
import io.swagger.client.model.ReportDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AmazonSpapiServiceImpl implements AmazonSpapiService {
    Log log = LogFactory.get();
    @Autowired
    private ReportUtils reportUtils;

    @Autowired
    private AmazonFileMapper amazonFileMapper;

    @Override
    public String getSpapiReportId(ReportsApi reportsApi,CreateReportSpecification body) {
        int count = 3;
        CreateReportResponse response = null;
        while (count>0){
            try {
                response = reportsApi.createReport(body);
            } catch (Exception e) {
                log.info("getSpapi失败重试:"+e.getMessage());
                count--;
                continue;
            }
            if (!StringUtil.isEmpty(response)){
                break;
            }
        }
        if (StringUtil.isEmpty(response)){
            log.info("getSpapi结果为空");
            return null;
        }
        log.info(StringUtil.toString(response));
        return response.getReportId();
    }

    @Override
    public String getSpapiReportDocumentId(ReportsApi reportsApi,String reportId) throws InterruptedException {
        int count = 5;
        Report response = null;
        while (count>0){
            try {
                response = reportsApi.getReport(reportId);
            } catch (Exception e) {
                log.info("getSpapiReportDocumentId失败重试:"+e.getMessage());
                count--;
                continue;
            }
            if ("DONE".equals(StringUtil.toString(response.getProcessingStatus()))){
                break;
            }else {
                log.info(StringUtil.toString(response));
                Thread.sleep(60000);
                count--;
                continue;
            }
        }
        if (StringUtil.isEmpty(response)){
            log.info("getSpapiReportDocumentId结果为空");
            return null;
        }
        log.info(StringUtil.toString(response));
        return response.getReportDocumentId();
    }

    @Override
    public String getSpapiReportUrl(ReportsApi reportsApi, String spapiReportDocumentId) {
        int count = 3;
        ReportDocument response = null;
        while (count>0){
            try {
                response = reportsApi.getReportDocument(spapiReportDocumentId);
            } catch (Exception e) {
                log.info("getSpapiReportUrl失败重试:"+e.getMessage());
                count--;
                continue;
            }
            if (!StringUtil.isEmpty(response)){
                break;
            }
        }
        if (StringUtil.isEmpty(response)){
            log.info("getSpapiReportUrl结果为空");
            return null;
        }
        log.info(StringUtil.toString(response));
        return response.getUrl();
    }

    @Override
    public void getSpapiReport(ReportsApi reportsApi, String url,String time) throws InterruptedException {
        String fileName = UUID.randomUUID().toString() + ".gz";
        String filePath = reportUtils.download(url, fileName);
        StringBuilder stringBuilder = reportUtils.unGzip(filePath);
        Map<String,Object> map = JSONObject.parseObject(StringUtil.toString(stringBuilder),Map.class);
        List<Map<String, Object>> list = (List)map.get("salesAndTrafficByAsin");
        try {
            amazonFileMapper.deleteSpapiData(time);
            amazonFileMapper.insertSpapiData(list,time);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @Override
    public void getSpapiPriceReport(ReportsApi reportsApi, String url) {
        String fileName = UUID.randomUUID().toString() + ".txt";
        String filePath = reportUtils.download(url, fileName);
        List<String> readUtf8Lines = FileUtil.readLines(filePath, CharsetUtil.UTF_8, new ArrayList<>());
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 1; i <readUtf8Lines.size() ; i++) {
            Map<String, Object> map = new HashMap<>();
            String[] split = readUtf8Lines.get(i).split("\\t");
            map.put("sku",split[0]);
            map.put("asin",split[1]);
            map.put("price",split[2]);
            result.add(map);
        }
        try {
            amazonFileMapper.deleteSpapiPirceData();
            amazonFileMapper.insertSpapiPirceData(result);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }
}
