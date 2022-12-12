package com.zhz.selenium.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSONObject;
import com.zhz.selenium.mapper.AmazonFileMapper;
import com.zhz.selenium.pojo.Configuration;
import com.zhz.selenium.pojo.JsonRootBean;
import com.zhz.selenium.service.AmazonFileService;
import com.zhz.selenium.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AmazonFileServiceImpl implements AmazonFileService {

    Log log = LogFactory.get();

    @Autowired
    private AmazonFileMapper amazonFileMapper;

    @Override
    public String getAccessToken(String grantType, String clientId, String refreshToken, String clientSecret, Map<Object, Object> map) {
        int count = 3;
        String url = "https://api.amazon.com/auth/o2/token";
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("grant_type",grantType);
        paramMap.put("client_id",clientId);
        paramMap.put("refresh_token",refreshToken);
        paramMap.put("client_secret",clientSecret);
        String results = null;
        while (count>0){
            try {
                results = HttpUtil.post(url, paramMap);
            } catch (Exception e) {
                log.info("getAccessToken失败重试 :"+e.getMessage());
                count--;
                continue;
            }
            if (!StringUtil.isEmpty(results)){
                break;
            }
        }
        if (StringUtil.isEmpty(results)){
            log.info("getAccessToken结果为空");
            return null;
        }
        map.put("endTime",new Date().getTime()+3500000);
        Map<String,Object> resultMap = JSONObject.parseObject(results,Map.class);
        map.put("access_token",resultMap.get("access_token"));
        return StringUtil.toString(resultMap.get("access_token"));
    }

    @Override
    public String getSearchTermReportId(String contentType,String amazonAdvertisingAPIScope,String amazonAdvertisingAPIClientId
            ,String authorization,String searchFields) {
        int count = 3;
        String url = "https://advertising-api.amazon.com/reporting/reports";
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("Content-Type","application/json");
        paramMap.put("Amazon-Advertising-API-ClientId",amazonAdvertisingAPIClientId);
        paramMap.put("Amazon-Advertising-API-Scope",amazonAdvertisingAPIScope);
        paramMap.put("Authorization",authorization);
        Configuration configuration = new Configuration();
        configuration.setAdProduct("SPONSORED_PRODUCTS");
        String groupBy[] = new String[]{"searchTerm"};
        configuration.setGroupBy(Arrays.asList(groupBy));
        String[] split = searchFields.split(",");
        configuration.setColumns(Arrays.asList(split));
        configuration.setFormat("GZIP_JSON");
        configuration.setTimeUnit("DAILY");
        configuration.setReportTypeId("spSearchTerm");
        JsonRootBean jsonRootBean = new JsonRootBean();
        jsonRootBean.setStartDate(new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -22)));
        jsonRootBean.setEndDate(new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -1)));
        jsonRootBean.setConfiguration(configuration);
        log.info("getSearchTermReportId:"+JSONUtil.toJsonStr(jsonRootBean));
        HttpResponse response = null;
        while (count>0){
            try {
                response = HttpRequest.post(url).headerMap(paramMap, false).body(JSONUtil.toJsonStr(jsonRootBean)).execute();
            } catch (Exception e) {
                log.info("getSearchTermReportId失败重试:"+e.getMessage());
                count--;
                continue;
            }
            if (!StringUtil.isEmpty(response)){
                break;
            }
        }
        if (StringUtil.isEmpty(response)){
            log.info("getSearchTermReportId结果为空");
            return null;
        }
        Map<String,Object> resultMap = JSONObject.parseObject(response.body(),Map.class);
        log.info("getSearchTermReportId:"+response);
        return StringUtil.toString(resultMap.get("reportId"));
    }

    @Override
    public void getSearchTermReport(String contentType, String amazonAdvertisingAPIScope, String amazonAdvertisingAPIClientId,String authorization
            , String reportId) throws InterruptedException {
        String url = "https://advertising-api.amazon.com/reporting/reports/"+reportId;
        log.info("reportId : "+reportId);
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("Content-Type",contentType);
        paramMap.put("Amazon-Advertising-API-ClientId",amazonAdvertisingAPIClientId);
        paramMap.put("Amazon-Advertising-API-Scope",amazonAdvertisingAPIScope);
        paramMap.put("Authorization",authorization);
        HttpResponse response = HttpRequest.get(url).headerMap(paramMap, false).execute();
        log.info("getSearchTermReport:"+response);
        Map<String,Object> resultMap = JSONObject.parseObject(response.body(),Map.class);
        if (!"COMPLETED".equals(resultMap.get("status"))){
            Thread.sleep(60000);
            getSearchTermReport(contentType,amazonAdvertisingAPIScope,amazonAdvertisingAPIClientId,authorization,reportId);
        }else {
            String fileName = UUID.randomUUID().toString() + ".json.gz";
            String filePath = download(StringUtil.toString(resultMap.get("url")), fileName);
            StringBuilder stringBuilder = unGzip(filePath);
            log.info(StringUtil.toString(stringBuilder));
            List<Map<String,Object>> list = JSONObject.parseObject(StringUtil.toString(stringBuilder),List.class);
            String startDate = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -22));
            String endDate = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -1));
            for (Map<String,Object> m: list) {
                m.put("Day7TotalUnits",Integer.parseInt(StringUtil.toString(m.get("unitsSoldSameSku7d")))+Integer.parseInt(StringUtil.toString(m.get("unitsSoldOtherSku7d"))));
                if (!StringUtil.isEmpty(m.get("acosClicks7d"))){
                    m.put("acosClicks7d",Double.valueOf(StringUtil.toString(m.get("acosClicks7d")))/100);
                }
                if (!StringUtil.isEmpty(m.get("clickThroughRate"))){
                    m.put("clickThroughRate",Double.valueOf(StringUtil.toString(m.get("clickThroughRate")))/100);
                }
                double v = (Double.valueOf(StringUtil.toString(m.get("unitsSoldClicks7d")))) / (Double.valueOf(StringUtil.toString(m.get("clicks"))));
                if (Double.isInfinite(v) || Double.isNaN(v)) {
                    m.put("Day7ConversionRate",0);
                } else {
                    m.put("Day7ConversionRate",v);
                }
            }
            try {
                amazonFileMapper.deleteDataByTime(startDate,endDate);
                amazonFileMapper.insertSearchTermData(list);
            } catch (Exception e) {
                log.info(e.getMessage());
            }
        }
    }

    @Override
    public String getAmzReportId(String contentType,String amazonAdvertisingAPIScope,String amazonAdvertisingAPIClientId
            ,String authorization,String searchFields) {
        int count = 3;
        String url = "https://advertising-api.amazon.com/reporting/reports";
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("Content-Type","application/json");
        paramMap.put("Amazon-Advertising-API-ClientId",amazonAdvertisingAPIClientId);
        paramMap.put("Amazon-Advertising-API-Scope",amazonAdvertisingAPIScope);
        paramMap.put("Authorization",authorization);
        Configuration configuration = new Configuration();
        configuration.setAdProduct("SPONSORED_PRODUCTS");
        String groupBy[] = new String[]{"advertiser"};
        configuration.setGroupBy(Arrays.asList(groupBy));
        String[] split = searchFields.split(",");
        configuration.setColumns(Arrays.asList(split));
        configuration.setFormat("GZIP_JSON");
        configuration.setTimeUnit("SUMMARY");
        configuration.setReportTypeId("spAdvertisedProduct");
        JsonRootBean jsonRootBean = new JsonRootBean();
        jsonRootBean.setStartDate(new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -1)));
        jsonRootBean.setEndDate(new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -1)));
        jsonRootBean.setConfiguration(configuration);
        log.info("getAmzReportId:"+JSONUtil.toJsonStr(jsonRootBean));
        HttpResponse response = null;
        while (count>0){
            try {
                response = HttpRequest.post(url).headerMap(paramMap, false).body(JSONUtil.toJsonStr(jsonRootBean)).execute();
            } catch (Exception e) {
                log.info(e.getMessage());
                count--;
                continue;
            }
            if (!StringUtil.isEmpty(response)){
                break;
            }
        }
        if (StringUtil.isEmpty(response)){
            return null;
        }
        Map<String,Object> resultMap = JSONObject.parseObject(response.body(),Map.class);
        log.info("getAmzReportId:"+response);
        return StringUtil.toString(resultMap.get("reportId"));
    }

    @Override
    public void getAmzReport(String contentType, String amazonAdvertisingAPIScope, String amazonAdvertisingAPIClientId,String authorization
            , String reportId) throws InterruptedException {
        String url = "https://advertising-api.amazon.com/reporting/reports/"+reportId;
        log.info("reportId :"+reportId);
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("Content-Type",contentType);
        paramMap.put("Amazon-Advertising-API-ClientId",amazonAdvertisingAPIClientId);
        paramMap.put("Amazon-Advertising-API-Scope",amazonAdvertisingAPIScope);
        paramMap.put("Authorization",authorization);
        HttpResponse response = HttpRequest.get(url).headerMap(paramMap, false).execute();
        log.info("getAmzReport:"+response);
        Map<String,Object> resultMap = JSONObject.parseObject(response.body(),Map.class);
        if (!"COMPLETED".equals(resultMap.get("status"))){
            Thread.sleep(60000);
            getAmzReport(contentType,amazonAdvertisingAPIScope,amazonAdvertisingAPIClientId,authorization,reportId);
        }else {
            String fileName = UUID.randomUUID().toString() + ".json.gz";
            String filePath = download(StringUtil.toString(resultMap.get("url")), fileName);
            StringBuilder stringBuilder = unGzip(filePath);
            System.out.println(stringBuilder);
            List<Map<String,Object>> list = JSONObject.parseObject(StringUtil.toString(stringBuilder),List.class);
            for (Map<String,Object> m: list) {
                m.put("Day7TotalUnits",Integer.parseInt(StringUtil.toString(m.get("unitsSoldSameSku7d")))+Integer.parseInt(StringUtil.toString(m.get("unitsSoldOtherSku7d"))));
                if (!StringUtil.isEmpty(m.get("acosClicks7d"))){
                    m.put("acosClicks7d",Double.valueOf(StringUtil.toString(m.get("acosClicks7d")))/100);
                }
                if (!StringUtil.isEmpty(m.get("clickThroughRate"))){
                    m.put("clickThroughRate",Double.valueOf(StringUtil.toString(m.get("clickThroughRate")))/100);
                }
                double v = (Double.valueOf(StringUtil.toString(m.get("unitsSoldClicks7d")))) / (Double.valueOf(StringUtil.toString(m.get("clicks"))));
                if (Double.isInfinite(v) || Double.isNaN(v)) {
                    m.put("Day7ConversionRate",0);
                } else {
                    m.put("Day7ConversionRate",v);
                }
            }
            try {
                amazonFileMapper.insertAmzData(list);
            } catch (Exception e) {
                log.info(e.getMessage());
            }
        }
    }

    @Override
    public void getCampaignReport(String amazonAdvertisingAPIScope, String amazonAdvertisingAPIClientId, String authorization) throws InterruptedException {
        int count = 3;
        String url = "https://advertising-api.amazon.com/v2/sp/campaigns?stateFilter=enabled";
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("Amazon-Advertising-API-ClientId",amazonAdvertisingAPIClientId);
        paramMap.put("Amazon-Advertising-API-Scope",amazonAdvertisingAPIScope);
        paramMap.put("Authorization",authorization);
        HttpResponse response = null;
        while (count>0){
            try {
                response = HttpRequest.get(url).headerMap(paramMap, false).execute();
            } catch (Exception e) {
                log.info("getCampaignReport失败重试:"+e.getMessage());
                count--;
                continue;
            }
            if (!StringUtil.isEmpty(response)){
                break;
            }
        }
        if (StringUtil.isEmpty(response)){
            log.info("getCampaignReport结果为空");
        }
        List<Map<String,Object>> resultMap = JSONObject.parseObject(response.body(),List.class);
        try {
            amazonFileMapper.deleteCampaignData();
            amazonFileMapper.insertCampaignData(resultMap);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @Override
    public void getAdgroupReport(String amazonAdvertisingAPIScope, String amazonAdvertisingAPIClientId, String authorization) {
        int count = 3;
        String url = "https://advertising-api.amazon.com/v2/sp/adGroups?stateFilter=enabled,paused";
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("Amazon-Advertising-API-ClientId",amazonAdvertisingAPIClientId);
        paramMap.put("Amazon-Advertising-API-Scope",amazonAdvertisingAPIScope);
        paramMap.put("Authorization",authorization);
        HttpResponse response = null;
        while (count>0){
            try {
                response = HttpRequest.get(url).headerMap(paramMap, false).execute();
            } catch (Exception e) {
                log.info("getAdgroupReport失败重试 : "+e.getMessage());
                count--;
                continue;
            }
            if (!StringUtil.isEmpty(response)){
                break;
            }
        }
        if (StringUtil.isEmpty(response)){
            log.info("getAdgroupReport结果为空");
        }
        List<Map<String,Object>> resultMap = JSONObject.parseObject(response.body(),List.class);
        try {
            amazonFileMapper.deleteAdgroupData();
            amazonFileMapper.insertAdgroupData(resultMap);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @Override
    public void getNegativeKeyWordReport(String amazonAdvertisingAPIScope, String amazonAdvertisingAPIClientId, String authorization) {
        int count = 3;
        String url = "https://advertising-api.amazon.com/v2/sp/negativeKeywords?stateFilter=enabled";
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("Amazon-Advertising-API-ClientId",amazonAdvertisingAPIClientId);
        paramMap.put("Amazon-Advertising-API-Scope",amazonAdvertisingAPIScope);
        paramMap.put("Authorization",authorization);
        HttpResponse response = null;
        while (count>0){
            try {
                response = HttpRequest.get(url).headerMap(paramMap, false).execute();
            } catch (Exception e) {
                log.info("getNegativeKeyWordReport失败重试 :"+e.getMessage());
                count--;
                continue;
            }
            if (!StringUtil.isEmpty(response)){
                break;
            }
        }
        if (StringUtil.isEmpty(response)){
            log.info("getNegativeKeyWordReport结果为空");
        }
        List<Map<String,Object>> resultMap = JSONObject.parseObject(response.body(),List.class);
        try {
            amazonFileMapper.deleteNegativeKeyWordData();
            amazonFileMapper.insertNegativeKeyWordData(resultMap);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @Override
    public void getImportPortfoliosReport(String amazonAdvertisingAPIScope, String amazonAdvertisingAPIClientId, String authorization) {
        int count = 3;
        String url = "https://advertising-api.amazon.com/v2/portfolios?portfolioStateFilter=enabled";
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("Amazon-Advertising-API-ClientId",amazonAdvertisingAPIClientId);
        paramMap.put("Amazon-Advertising-API-Scope",amazonAdvertisingAPIScope);
        paramMap.put("Authorization",authorization);
        HttpResponse response = null;
        while (count>0){
            try {
                response = HttpRequest.get(url).headerMap(paramMap, false).execute();
            } catch (Exception e) {
                log.info("getImportPortfoliosReport失败重试 :"+e.getMessage());
                count--;
                continue;
            }
            if (!StringUtil.isEmpty(response)){
                break;
            }
        }
        if (StringUtil.isEmpty(response)){
            log.info("getImportPortfoliosReport结果为空");
        }
        List<Map<String,Object>> resultMap = JSONObject.parseObject(response.body(),List.class);
        try {
            amazonFileMapper.deleteImportPortfoliosData();
            amazonFileMapper.insertImportPortfoliosData(resultMap);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }


    private String download(String headPortrait,String fileName){
        System.out.println(headPortrait);
        System.out.println(fileName);
        URL url = null;
        DataInputStream dataInputStream = null;
        FileOutputStream fileOutputStream = null;
        File localFile = null;
        try {
            //根据url下载图片到本地
            //getPath()得到的是构造file的时候的路径。 getAbsolutePath()得到的是全路径
            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            if (!path.exists()) {
                path = new File("");
            }
            File dir = new File(path.getAbsolutePath(), "upload/");
            if (!dir.exists()) {
                dir.mkdir();
            }
            localFile = new File(path.getAbsolutePath(), "upload/" + fileName);
            String localFilePath = localFile.getPath();
            url = new URL(headPortrait);
            dataInputStream = new DataInputStream(url.openStream());
            fileOutputStream = new FileOutputStream(localFilePath);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 10];
            int length;
            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
            return path.getAbsolutePath()+ "/upload/" + fileName;
        } catch (Exception e) {
            try {
                if (dataInputStream != null) {
                    dataInputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Exception exception) {
                log.info(exception.getMessage());
                return null;
            }
            localFile.delete();
            return null;
        }
    }

    private StringBuilder unGzip(String path) throws InterruptedException {
        System.out.println(path);
        Thread.sleep(60000);
        StringBuilder stringBuilder = new StringBuilder();
        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            byte[] bytes = ZipUtil.unGzip(FileUtil.getInputStream(path));
            FileUtil.writeBytes(bytes, path);
            fileInputStream = new FileInputStream(path);
            inputStreamReader = new InputStreamReader(fileInputStream, "utf-8");
            bufferedReader = new BufferedReader(inputStreamReader);
            String tempString;
            while ((tempString = bufferedReader.readLine()) != null) {// 直接返回读取的字符串
                // 将字符串 添加到 stringBuilder中
                stringBuilder.append(tempString);
            }
            fileInputStream.close();
            inputStreamReader.close();
            bufferedReader.close();
            return stringBuilder;
        } catch (IOException e) {
            log.info(e.getMessage());
            try {
                fileInputStream.close();
                inputStreamReader.close();
                bufferedReader.close();
            } catch (IOException ex) {
                log.info(ex.getMessage());
            }
        }
        return null;
    }
}
