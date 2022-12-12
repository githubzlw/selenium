package com.zhz.selenium.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AmazonAdAutoMapper {
    List<Map<String,Object>> selectNoOrderKeyWord();

    List<Map<String,Object>> selectRecentNoOrderKeyWord(@Param("startTime") String startTime,@Param("endTime") String endTime);

    List<Map<String,Object>> selectNoOrderAsin();

    List<Map<String,Object>> selectRecentNoOrderAsin(@Param("startTime") String startTime,@Param("endTime") String endTime);

    void insertLog(@Param("logs") String logs,@Param("time") String time);

    List<Map<String,Object>> selectLog();

    List<Map<String,Object>> selectWhiteAsin();

    List<Map<String,Object>> selectConverionRateOrCTR(@Param("startTime") String startTime,@Param("endTime") String endTime);

    String selectSkuByAsin(@Param("asin") String asin);
}
