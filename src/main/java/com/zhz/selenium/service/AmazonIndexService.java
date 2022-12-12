package com.zhz.selenium.service;

import com.zhz.selenium.pojo.SearchResult;

import java.util.List;
import java.util.Map;

public interface AmazonIndexService {
    // 搜索全部内容
    List<SearchResult> searchAll(String searchDate, String rankMethod, String num,String keyWord);

    // 根据asin 日期 关键字搜索商品
    SearchResult competeProduct(String searchDate, String keyWord, String asin);

    // 根据关键词和排名 对传入的id进行搜索，找竞品
    List<SearchResult> compareCompeteProduct(String rankMethod, String keyWord, String ids);

    // 根据asin找关键词id
    Integer searchProductIdByAsin(String asin);

    //插入关键词的商品的asin和竞品的id
    void insertKeyWordProduct(List<String> keyWordId,String asin,String ids);

    // 通过spend(cpc)查询关键词前四(以最新日期，来查找关键字的cpc的价格排名 )
    List<String> selectKeyWordByCpc();

    // 商品排名分析(关键词，asin，搜索日期来进行查找相应的自然排名和广告排名)
    List<Map<String,Object>> rankingByAsin(String startTime, String endTime, String asin, String keyWord,String campaignName);
    List<Map<String,Object>> rankingByAsin1(String startTime, String endTime, String asin, String keyWord);

    // 根据asin和日期时间点进行比较
    List<Map<String,Object>> compareProductByAsinAndDate(String startTime, String endTime, String asin);

    // 查询近30天内曝光量大于200的全部组
    List<Map<String,Object>> selectCampaignByTime(String startTime,String endTime,String yesTime,String lasWeekTime);
    // 查询近30天内曝光量大于200的全部组(加组名和负责人)
    List<Map<String,Object>> selectCampaignByTimeAndPerson(String startTime,String endTime,String campaignName
            ,String portfolio,String yesTime,String lasWeekTime);

    // 根据组名来查找所属的相关数据 （近30天）
    List<Map<String,Object>> selectDataByCampaign(String startTime,String endTime,String campaignName);

    // 根据日期和组名查找组相关的内容
    List<Map<String, Object>> selectCampaignByDate(String startTime, String endTime, String campaignName,String asin);

    List<String> selectKeyWordIdByKeyWord(String keyWord);

    Map<String,Object> selectCampaignByKeyWordAndAsin(String keyWord,String asin);

    // 最近30天曝光量大于200的asin列表
    List<Map<String, Object>> selectAsinByTime(String startTime, String date);

    // 根据日期和asin查找相关内容
    List<Map<String, Object>> selectDataByAsin(String asin, String startTime, String date, String yesTime, String lasWeekTime
            , String twLasWeekTime, String thrLasWeekTime);

    // 根据日期和asin查找acos
    List<Map<String, Object>> selectAcosByAsin(String asin, String startTime, String date, String lasWeekTime);
}
