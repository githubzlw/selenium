package com.zhz.selenium.service.impl;

import cn.hutool.core.date.DateUtil;
import com.zhz.selenium.mapper.AdvertisingViolationMapper;
import com.zhz.selenium.service.AdvertisingViolationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AdvertisingViolationServiceImpl implements AdvertisingViolationService {

    @Autowired
    private AdvertisingViolationMapper advertisingViolationMapper;

    @Override
    public List<Map<String, Object>> selectAdParam() {
        try {
            List<Map<String, Object>> maps = advertisingViolationMapper.selectAdParam();
            return maps;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectKeyWordAndNoOrder(String click) {
        try {
            String date = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -1));
            String startTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -30));
            List<Map<String, Object>> list = advertisingViolationMapper.selectKeyWordAndNoOrder(click,startTime,date);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    @Override
//    public String selectMaxDate() {
//        try {
//            String date = advertisingViolationMapper.selectMaxDate();
//            return date;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    @Override
    public List<Map<String, Object>> selectImpressionAndCtrAndNoOrder(String date, Integer impression, String ctr) {
        try {
            List<Map<String, Object>> maps = advertisingViolationMapper.selectImpressionAndCtrAndNoOrder(date, impression, ctr);
            return maps;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectClickAndCtrAndNoOrder(String click, String ctr) {
        try {
            String date = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -1));
            String startTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -30));
            List<Map<String, Object>> maps = advertisingViolationMapper.selectClickAndCtrAndNoOrder(click, ctr,startTime,date);
            return  maps;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectSearchTermByAcos(String acos) {
        try {
            String date = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -1));
            String startTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -30));
            List<Map<String, Object>> maps = advertisingViolationMapper.selectSearchTermByAcos(Double.valueOf(acos)/100.0,startTime,date);
            return maps;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectSearchTermByCtrAndClick(Double ctr, String click, Double conversionRate) {
        try {
            String date = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -1));
            String startTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -30));
            List<Map<String, Object>> maps = advertisingViolationMapper.selectSearchTermByCtrAndClick(ctr, click, conversionRate,startTime,date);
            return  maps;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectSearchTermByClickAndConrate(String click, Double conversionRate) {
        try {
            String date = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -1));
            String startTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -30));
            List<Map<String, Object>> maps = advertisingViolationMapper.selectSearchTermByClickAndConrate(click, conversionRate,startTime,date);
            return maps;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectAsinByClick(String click) {
        try {
            String date = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -1));
            String startTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -30));
            List<Map<String, Object>> maps = advertisingViolationMapper.selectAsinByClick(click,startTime,date);
            return maps;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectStopAdvice1(Double percent) {
        try {
            List<Map<String, Object>> maps = advertisingViolationMapper.selectStopAdvice1(percent / 100);
            return maps;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectStopAdvice2(Double percent) {
        try {
            List<Map<String, Object>> maps = advertisingViolationMapper.selectStopAdvice2(percent / 100);
            return maps;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectStopAdvice3(Double percent) {
        try {
            List<Map<String, Object>> maps = advertisingViolationMapper.selectStopAdvice3(percent / 100);
            return maps;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectStopAdvice4(Double percent) {
        try {
            List<Map<String, Object>> maps = advertisingViolationMapper.selectStopAdvice4(percent / 100);
            return maps;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectStopAdvice5(Double percent) {
        try {
            List<Map<String, Object>> maps = advertisingViolationMapper.selectStopAdvice5(percent / 100);
            return maps;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectStopAdvice6(Double percent) {
        try {
            List<Map<String, Object>> maps = advertisingViolationMapper.selectStopAdvice6(percent / 100);
            return maps;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateParam(Map<String, Object> map) {
        advertisingViolationMapper.updateParam(map);
    }

    @Override
    public List<Map<String, Object>> selectSearchTermByLowAcos(String acos) {
        try {
            String date = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -1));
            String startTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -30));
            List<Map<String, Object>> maps = advertisingViolationMapper.selectSearchTermByLowAcos(acos,startTime,date);
            return maps;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
