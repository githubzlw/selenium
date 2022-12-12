package com.zhz.selenium.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AmazonFileMapper {
    void deleteDataByTime(@Param("startTime") String startTime,@Param("endTime") String endTime);

    void insertSearchTermData(List<Map<String,Object>> data);

    void insertAmzData(List<Map<String,Object>> data);

    void deleteCampaignData();

    void insertCampaignData(List<Map<String,Object>> data);

    void deleteAdgroupData();

    void insertAdgroupData(List<Map<String,Object>> data);

    void deleteNegativeKeyWordData();

    void insertNegativeKeyWordData(List<Map<String,Object>> data);

    void deleteImportPortfoliosData();

    void insertImportPortfoliosData(List<Map<String,Object>> data);

    void deleteSpapiData(@Param("time") String time);

    void insertSpapiData(List<Map<String,Object>> list,@Param("time") String time);

    void deleteSpapiPirceData();

    void insertSpapiPirceData(List<Map<String, Object>> result);
}
