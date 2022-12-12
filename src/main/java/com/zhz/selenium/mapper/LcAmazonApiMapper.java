package com.zhz.selenium.mapper;

import com.zhz.selenium.pojo.ApiResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LcAmazonApiMapper {

    List<ApiResult>  selectRankSearchterm(@Param("keyWord")String keyWord);

    ApiResult  selectSalesAsin(@Param("dateRange")Integer dateRange,@Param("asin")String asin);

    List<ApiResult> selectKeywordsAsin(@Param("dateRange")Integer dateRange,@Param("asin")String asin);

    List<ApiResult>  selectRankAsin(@Param("asin")String asin);
}
