package com.zhz.selenium.service.impl;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.zhz.selenium.mapper.LcAmazonApiMapper;
import com.zhz.selenium.pojo.ApiChildResult;
import com.zhz.selenium.pojo.ApiOther;
import com.zhz.selenium.pojo.ApiResult;
import com.zhz.selenium.service.LcAmazonApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LcAmazonApiServiceImpl implements LcAmazonApiService {
    Log log = LogFactory.get();

    @Autowired
    private LcAmazonApiMapper lcAmazonApiMapper;

    @Override
    public List<ApiResult> selectRankSearchterm(String keyWord) {
        try {
            List<ApiResult> maps = lcAmazonApiMapper.selectRankSearchterm(keyWord);
            return maps;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public ApiChildResult selectSalesAsin(Integer dateRange, String asin) {
        try {
            ApiChildResult bean = lcAmazonApiMapper.selectSalesAsin(dateRange,asin);
            return bean;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public ApiChildResult selectSellAsin(Integer dateRange, String asin) {
        try {
            ApiChildResult bean = lcAmazonApiMapper.selectSellAsin(dateRange,asin);
            return bean;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }


    @Override
    public ApiResult listingDetail(Integer dateRange,String asin) {
        try {
            ApiResult bean = lcAmazonApiMapper.listingDetail(dateRange,asin);
            return bean;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public String getNewDate() {
        try {
            String newDate = lcAmazonApiMapper.getNewDate();
            return newDate;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public String selectSpApiReportNewDate() {
        try {
            String newDate = lcAmazonApiMapper.selectSpApiReportNewDate();
            return newDate;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }


    @Override
    public List<ApiResult> selectKeywordsAsin(Integer dateRange,String asin) {
        try {
            List<ApiResult> maps = lcAmazonApiMapper.selectKeywordsAsin(dateRange,asin);
            return maps;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<ApiResult> selectRankAsin(String asin) {
        try {
            List<ApiResult> maps = lcAmazonApiMapper.selectRankAsin(asin);
            return maps;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }



    @Override
    public List<ApiChildResult> selectChildAsin(String childAsin) {
        try {
            List<ApiChildResult> maps = lcAmazonApiMapper.selectChildAsin(childAsin);
            return maps;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }


    @Override
    public String selectRankSearchtermByNewDate(String keyWord) {
        try {
            String newDate = lcAmazonApiMapper.selectRankSearchtermByNewDate(keyWord);
            return newDate;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<ApiOther> selectCampaignTarget (Integer dateRange, String asin) {
        try {
            List<ApiOther> maps = lcAmazonApiMapper.selectCampaignTarget(dateRange,asin);
            return maps;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
