package com.zhz.selenium.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface AdvertisingViolationMapper {
    // 点击量大于多少次没出单
    List<Map<String, Object>> selectKeyWordAndNoOrder(@Param("click") String click,@Param("startTime") String startTime,@Param("endTime") String endTime);

    // 3日曝光>1000,CTR<0.25%,未出单
    String selectMaxDate();
    List<Map<String, Object>> selectImpressionAndCtrAndNoOrder(@Param("date") String date,@Param("impression") Integer impression,@Param("ctr") String ctr);

    // 7次点击没订单，而且CTR<0.25%
    List<Map<String,Object>> selectClickAndCtrAndNoOrder(@Param("click") String click,@Param("ctr") String ctr,@Param("startTime") String startTime,@Param("endTime") String endTime);

    // 单个search term 词  ACOS>90%
    List<Map<String,Object>> selectSearchTermByAcos(@Param("acos") Double acos,@Param("startTime") String startTime,@Param("endTime") String endTime);

    // 单个search term ctr>2%,click>20,转化率<3%
    List<Map<String,Object>> selectSearchTermByCtrAndClick(@Param("ctr") Double ctr,@Param("click") String click,@Param("conversionRate") Double conversionRate,@Param("startTime") String startTime,@Param("endTime") String endTime);

    // 单个search term click>20,转化率<5%
    List<Map<String,Object>> selectSearchTermByClickAndConrate(@Param("click") String click,@Param("conversionRate") Double conversionRate,@Param("startTime") String startTime,@Param("endTime") String endTime);

    // 点击4次还没出单的asin
    List<Map<String,Object>> selectAsinByClick(@Param("click") String click,@Param("startTime") String startTime,@Param("endTime") String endTime);

    // 广告组的关停预警，价格0-10；11-20，21-30，31-40，41-50，>50
    List<Map<String,Object>> selectStopAdvice1(@Param("percent") Double percent);
    List<Map<String,Object>> selectStopAdvice2(@Param("percent") Double percent);
    List<Map<String,Object>> selectStopAdvice3(@Param("percent") Double percent);
    List<Map<String,Object>> selectStopAdvice4(@Param("percent") Double percent);
    List<Map<String,Object>> selectStopAdvice5(@Param("percent") Double percent);
    List<Map<String,Object>> selectStopAdvice6(@Param("percent") Double percent);

    List<Map<String,Object>> selectAdParam();

    void updateParam(@Param("map")Map<String, Object> map);

    List<Map<String,Object>> selectSearchTermByLowAcos(@Param("acos") String acos,@Param("startTime") String startTime,@Param("endTime") String endTime);
}
