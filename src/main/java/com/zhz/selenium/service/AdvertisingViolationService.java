package com.zhz.selenium.service;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AdvertisingViolationService {

    //查询参数
    List<Map<String, Object>> selectAdParam();

    // 点击量大于多少次没出单
    List<Map<String, Object>> selectKeyWordAndNoOrder( String click);

    // 3日曝光>1000,CTR<0.25%,未出单
//    String selectMaxDate();
    List<Map<String, Object>> selectImpressionAndCtrAndNoOrder(String date,Integer impression,String ctr);

    // 7次点击没订单，而且CTR<0.25%
    List<Map<String,Object>> selectClickAndCtrAndNoOrder(String click, String ctr);

    // 单个search term 词  ACOS>90%
    List<Map<String,Object>> selectSearchTermByAcos( String acos);

    // 单个search term ctr>2%,click>20,转化率<3%
    List<Map<String,Object>> selectSearchTermByCtrAndClick(Double ctr, String click, Double conversionRate);

    // 单个search term click>20,转化率<5%
    List<Map<String,Object>> selectSearchTermByClickAndConrate(String click,Double conversionRate);

    // 点击4次还没出单的asin
    List<Map<String,Object>> selectAsinByClick( String click);

    // 广告组的关停预警，价格0-10；11-20，21-30，31-40，41-50，>50
    List<Map<String,Object>> selectStopAdvice1(@Param("percent") Double percent);
    List<Map<String,Object>> selectStopAdvice2(@Param("percent") Double percent);
    List<Map<String,Object>> selectStopAdvice3(@Param("percent") Double percent);
    List<Map<String,Object>> selectStopAdvice4(@Param("percent") Double percent);
    List<Map<String,Object>> selectStopAdvice5(@Param("percent") Double percent);
    List<Map<String,Object>> selectStopAdvice6(@Param("percent") Double percent);

    void updateParam(Map<String,Object> map);

    List<Map<String, Object>> selectSearchTermByLowAcos(String acos);
}
