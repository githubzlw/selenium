package com.zhz.selenium.mapper;

import com.zhz.selenium.pojo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public interface AmazonMapper {

    //-----------------爬虫

    // 插入搜索结果表数据
    void insertAmazonResult(List<SearchResult> resultsList);

    //查询关键词
    List<KeyWord> queryKeyWord(@Param("startTime")String startTime,@Param("endTime")String endTime);

    //更新关键词
    void updateKeyWord(List<KeyWord> keyWord);

    //获取查询表主键
    int insertSearch(Search search);

    // 查询关键词产品
    List<MyAsin> queryMyAsin();
//    List<KeyWordProduct> queryKeyWordProduct();

    // 插入分析结果商品
    void insertProduct(List<Product> productResult);

    // 插入搜索关键词
    void insertKeyWord(@Param("startTime")String startTime,@Param("endTime")String endTime);

    //------------------------页面

    // 页面搜索(拆分)
   List<SearchResult> selectAll(@Param("searchDate")String searchDate, @Param("rankMethod")String rankMethod
           ,@Param("num") String num,@Param("keyWord")String keyWord);
   List<String> selectKeyWordIdByKewWord(@Param("keyWord")String keyWord);
   List<String> selectSearchIdByKeyWordId(@Param("keyWordId")String keyWordId,@Param("searchDate")String searchDate);
   List<SearchResult> selectAllProduct(@Param("rankMethod")String rankMethod,@Param("num") String num
           ,@Param("keyWordId")String keyWordId,@Param("searchId")String searchId);


   SearchResult selectCompeteProduct(@Param("searchDate")String searchDate, @Param("keyWord")String keyWord, @Param("asin")String asin);

   List<SearchResult> compareCompeteProduct(@Param("rankMethod")String rankMethod, @Param("keyWord")String keyWord, @Param("ids")String ids);

   Integer searchProductIdByAsin(@Param("asin")String asin);

   // 插入竞品
   void insertKeyWordProduct(List<CompareProduct> list);

    // 通过spend(cpc)查询关键词前四(以最新日期，来查找关键字的cpc的价格排名 )
    List<String> selectKeyWordByCpc();

    // 根据关键词和时间范围查找搜索id
    List<Map<String,Object>> selectSearchIdByKeyWordAndTime(@Param("keyWord")String keyWord,@Param("startTime") String startTime
            ,@Param("endTime") String endTime);

    // 根据搜索id和asin查找产品位置
    List<Map<String,Object>> selectProduct(@Param("asin")String asin,@Param("searchId")String searchId);
    List<Map<String,Object>> selectSearchTermByAsinAndTime(@Param("keyWord")String keyWord,@Param("asin")String asin,@Param("startTime") String startTime
            ,@Param("endTime") String endTime,@Param("campaignName") String campaignName);
    List<Map<String,Object>> selectSearchTermByAsinAndTime1(@Param("keyWord")String keyWord,@Param("asin")String asin,@Param("startTime") String startTime
            ,@Param("endTime") String endTime);

    //根据asin和日期来进行比较---以下(拆分模式，预防数据量太大联合子查询太慢，如果还慢可以把第二个也拆分)
//    List<Map<String,Object>> selectProductByAsin(@Param("asin")String asin);
//    List<Map<String,Object>> selectProductBySearchId(@Param("ids")String ids,@Param("startTime")String startTime,@Param("endTime")String endTime);
    List<Map<String,Object>> selectProductByAsinAndTime(@Param("asin")String asin,@Param("startTime")String startTime,@Param("endTime")String endTime);

    List<Map<String,Object>> selectSearchTermByKeyWordAndAsin(@Param("asin")String asin
                                                                    ,@Param("startTime")String startTime,@Param("endTime")String endTime);


    // 查询近30天内曝光量大于200的全部组
    List<Map<String,Object>> selectCampaignByTime(@Param("startTime")String startTime,@Param("endTime")String endTime
            ,@Param("yesTime")String yesTime,@Param("lasWeekTime")String lasWeekTime);
    // 根据组名来查找所属的相关数据 （近30天）
    List<Map<String,Object>> selectDataByCampaign(@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("campaignId")String campaignId);
    // 根据商品关键词id和asin找竞品
    List<Map<String,Object>> selectCompareIdByAsinAndKeyWordId(@Param("keyWordId")String keyWordId,@Param("asin")String asin);
    // 根据id查找竞品
    List<Map<String,Object>> selectCompareById(@Param("ids")String ids);



    // 根据日期和组名查找组相关的内容
    List<Map<String,Object>> selectCampaignByDate(@Param("startTime")String startTime,@Param("endTime")String endTime
            ,@Param("campaignName")String campaignName,@Param("asin")String asin);

    // 更新关键词
    void updateKeyWordProduct(@Param("keyWordId")Integer keyWordId, @Param("asin")String asin, @Param("date")String date);

    // 查询近30天内曝光量大于200的全部组(根据祖名和负责人)
    List<Map<String, Object>> selectCampaignByTimeAndPerson(@Param("startTime")String startTime, @Param("endTime")String endTime
            ,@Param("campaignName") String campaignName,@Param("portfolio") String portfolio
            ,@Param("yesTime")String yesTime,@Param("lasWeekTime")String lasWeekTime);

    // 根据id查询asin
    List<Map<String, Object>> selectAsinById(@Param("ids")String ids);

    Map<String,Object> selectCampaignByKeyWordAndAsin(@Param("keyWord")String keyWord, @Param("asin") String asin);

    // 查询最近30天的asin列表
    List<Map<String, Object>> selectAsinByTime(@Param("startTime")String startTime, @Param("endTime")String endTime);

    List<Map<String, Object>> selectDataByAsin(@Param("asin")String asin, @Param("startTime")String startTime
            , @Param("date")String date, @Param("yesTime")String yesTime, @Param("lasWeekTime")String lasWeekTime
            , @Param("twLasWeekTime")String twLasWeekTime, @Param("thrLasWeekTime")String thrLasWeekTime);

    List<Map<String, Object>> selectAcosByAsin(@Param("asin")String asin, @Param("startTime")String startTime
            , @Param("date")String date, @Param("lasWeekTime")String lasWeekTime);

    // 查询过期数据
    List<Map<String, Object>> selectOverdueData();

    void deleteOverdueData(@Param("id")String id, @Param("startTime")String startTime
            , @Param("date")String date, @Param("lastsearchdate")String lastsearchdate);

    String selectDataByCampaignId(@Param("name")String name);

    Map<String, Object> selectData(@Param("keywordId")Object keywordId,@Param("asin")Object asin,@Param("index")Object index,@Param("date")Object date);

    List<Map<String, Object>> selectDataByIndex(@Param("keywordId")Object keywordId,@Param("index1")Object index1
            ,@Param("index2")Object index2,@Param("index3")Object index3,@Param("index4")Object index4,@Param("date")Object date);

    List<Map<String, Object>> selectSearchVolume(@Param("startTime")String startTime, @Param("endTime")String endTime);

    String selectImgByAsin(@Param("asin")String asin);

    String selectDay7Acos(@Param("campaignId")Object campaignId,@Param("startTime")String startTime
            ,@Param("endTime")String endTime,@Param("keyword")Object keyword);

    Map<String, Object> selectTacos(@Param("asin")String asin,@Param("startTime")String startTime
            ,@Param("lasWeekTime")String lasWeekTime,@Param("yesTime")String yesTime);

    String selectPriceByAsin(@Param("asin")String asin);

    List<Map<String, Object>> selectAsinMsg(@Param("asin")String asin
            , @Param("startTime")String startTime, @Param("endTime")String endTime);
}
