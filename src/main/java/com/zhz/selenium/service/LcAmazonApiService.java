package com.zhz.selenium.service;

import com.zhz.selenium.pojo.ApiChildResult;
import com.zhz.selenium.pojo.ApiResult;

import java.util.List;

public interface LcAmazonApiService {


    List<ApiResult> selectRankSearchterm(String keyWord);

    ApiChildResult selectSalesAsin(Integer dateRange, String asin) ;

    List<ApiResult> selectKeywordsAsin (Integer dateRange,String asin) ;

    List<ApiResult> selectRankAsin(String asin);

    ApiResult listingDetail(String asin) ;

    List<ApiChildResult> selectChildAsin (String childAsin) ;
}
