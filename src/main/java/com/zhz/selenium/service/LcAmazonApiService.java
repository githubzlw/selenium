package com.zhz.selenium.service;

import com.zhz.selenium.pojo.ApiResult;

import java.util.List;

public interface LcAmazonApiService {


    List<ApiResult> selectRankSearchterm(String keyWord);

    ApiResult selectSalesAsin(Integer dateRange,String asin) ;

    List<ApiResult> selectKeywordsAsin (Integer dateRange,String asin) ;

    List<ApiResult> selectRankAsin(String asin);

    ApiResult listingDetail(String asin) ;
}
