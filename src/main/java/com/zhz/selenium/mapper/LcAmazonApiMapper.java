package com.zhz.selenium.mapper;

import com.zhz.selenium.pojo.ApiChildResult;
import com.zhz.selenium.pojo.ApiOther;
import com.zhz.selenium.pojo.ApiResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LcAmazonApiMapper {

    List<ApiResult>  selectRankSearchterm(@Param("keyWord")String keyWord);

    ApiChildResult selectSalesAsin(@Param("dateRange")Integer dateRange, @Param("asin")String asin);

    ApiChildResult selectSellAsin(@Param("dateRange")Integer dateRange, @Param("asin")String asin);

    List<ApiResult> selectKeywordsAsin(@Param("dateRange")Integer dateRange,@Param("asin")String asin);

    List<ApiResult>  selectRankAsin(@Param("asin")String asin);

    ApiResult  listingDetail(@Param("dateRange")Integer dateRange,@Param("asin")String asin);


    List<ApiChildResult> selectChildAsin(@Param("childAsin")String childAsin);

    String getNewDate();

    String selectSpApiReportNewDate();

    String selectRankSearchtermByNewDate(@Param("keyWord")String keyWord);

    List<ApiOther> selectCampaignTarget (@Param("dateRange")Integer dateRange, @Param("asin")String  asin) ;

}
