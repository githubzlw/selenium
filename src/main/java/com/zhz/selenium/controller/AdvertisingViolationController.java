package com.zhz.selenium.controller;

import cn.hutool.core.date.DateUtil;
import com.zhz.selenium.pojo.SearchResult;
import com.zhz.selenium.service.AdvertisingViolationService;
import com.zhz.selenium.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class AdvertisingViolationController {

    @Autowired
    private AdvertisingViolationService advertisingViolationService;

    @RequestMapping(value = "/advertisingViolation")
    public ModelAndView advertisingViolation(){
        ModelAndView modelAndView = new ModelAndView();
        List<Map<String, Object>> list = advertisingViolationService.selectAdParam();
        for (Map m:list) {
            modelAndView.addObject(StringUtil.toString(m.get("id")),m.get("value"));
        }
        modelAndView.setViewName("advertising_violations");
        return modelAndView;
    }

    @RequestMapping(value = "/advertisingParam")
    public ModelAndView advertisingParam(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("advertising_param");
        return modelAndView;
    }

    @PostMapping(value = "/selectKeyWordAndNoOrder")
    @ResponseBody
    public List<Map<String,Object>> selectKeyWordAndNoOrder(@RequestParam(value = "click")String click){
        List<Map<String, Object>> maps = advertisingViolationService.selectKeyWordAndNoOrder(click);
        return maps;
    }

    @PostMapping(value = "/selectImpressionAndCtrAndNoOrder")
    @ResponseBody
    public List<Map<String,Object>> selectImpressionAndCtrAndNoOrder(@RequestParam(value = "impression")Integer impression,@RequestParam(value = "ctr")String ctr){
        String date = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -3));
        List<Map<String, Object>> list = advertisingViolationService.selectImpressionAndCtrAndNoOrder(date, impression*3, ctr);
        return list;
    }

    @PostMapping(value = "/selectClickAndCtrAndNoOrder")
    @ResponseBody
    public List<Map<String,Object>> selectClickAndCtrAndNoOrder(@RequestParam(value = "click")String click,@RequestParam(value = "ctr")String ctr){
        List<Map<String, Object>> list = advertisingViolationService.selectClickAndCtrAndNoOrder(click,ctr);
        return list;
    }


    @PostMapping(value = "/selectSearchTermByAcos")
    @ResponseBody
    public List<Map<String,Object>> selectSearchTermByAcos(@RequestParam(value = "acos")String acos){
        List<Map<String, Object>> list = advertisingViolationService.selectSearchTermByAcos(acos);
        return list;
    }

    @PostMapping(value = "/selectSearchTermByCtrAndClick")
    @ResponseBody
    public List<Map<String,Object>> selectSearchTermByCtrAndClick(@RequestParam(value = "ctr")Double ctr,@RequestParam(value = "click")String click
            ,@RequestParam(value = "conversionRate")Double conversionRate){
        List<Map<String, Object>> list = advertisingViolationService.selectSearchTermByCtrAndClick(ctr/100,click,conversionRate/100);
        return list;
    }

    @PostMapping(value = "/selectSearchTermByClickAndConrate")
    @ResponseBody
    public List<Map<String,Object>> selectSearchTermByClickAndConrate(@RequestParam(value = "click")String click
            ,@RequestParam(value = "conversionRate")Double conversionRate){
        List<Map<String, Object>> list = advertisingViolationService.selectSearchTermByClickAndConrate(click,conversionRate/100);
        return list;
    }


    @PostMapping(value = "/selectAsinByClick")
    @ResponseBody
    public List<Map<String,Object>> selectAsinByClick(@RequestParam(value = "click")String click){
        List<Map<String, Object>> list = advertisingViolationService.selectAsinByClick(click);
        return list;
    }

    @PostMapping(value = "/selectStopAdvice1")
    @ResponseBody
    public List<Map<String,Object>> selectStopAdvice1(@RequestParam(value = "percent")Double percent){
        List<Map<String, Object>> list = advertisingViolationService.selectStopAdvice1(percent);
        return list;
    }

    @PostMapping(value = "/selectStopAdvice2")
    @ResponseBody
    public List<Map<String,Object>> selectStopAdvice2(@RequestParam(value = "percent")Double percent){
        List<Map<String, Object>> list = advertisingViolationService.selectStopAdvice2(percent);
        return list;
    }

    @PostMapping(value = "/selectStopAdvice3")
    @ResponseBody
    public List<Map<String,Object>> selectStopAdvice3(@RequestParam(value = "percent")Double percent){
        List<Map<String, Object>> list = advertisingViolationService.selectStopAdvice3(percent);
        return list;
    }

    @PostMapping(value = "/selectStopAdvice4")
    @ResponseBody
    public List<Map<String,Object>> selectStopAdvice4(@RequestParam(value = "percent")Double percent){
        List<Map<String, Object>> list = advertisingViolationService.selectStopAdvice4(percent);
        return list;
    }

    @PostMapping(value = "/selectStopAdvice5")
    @ResponseBody
    public List<Map<String,Object>> selectStopAdvice5(@RequestParam(value = "percent")Double percent){
        List<Map<String, Object>> list = advertisingViolationService.selectStopAdvice5(percent);
        return list;
    }

    @PostMapping(value = "/selectStopAdvice6")
    @ResponseBody
    public List<Map<String,Object>> selectStopAdvice6(@RequestParam(value = "percent")Double percent){
        List<Map<String, Object>> list = advertisingViolationService.selectStopAdvice6(percent);
        return list;
    }

    @GetMapping(value = "/updateParam")
    @ResponseBody
    public String updateParam(HttpServletRequest httpServletRequest){
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("fa",httpServletRequest.getParameter("fa"));
            map.put("fb1",httpServletRequest.getParameter("fb1"));
            map.put("fb2",httpServletRequest.getParameter("fb2"));
            map.put("fc1",httpServletRequest.getParameter("fc1"));
            map.put("fc2",httpServletRequest.getParameter("fc2"));
            map.put("fd",httpServletRequest.getParameter("fd"));
            map.put("fe1",httpServletRequest.getParameter("fe1"));
            map.put("fe2",httpServletRequest.getParameter("fe2"));
            map.put("fe3",httpServletRequest.getParameter("fe3"));
            map.put("ff1",httpServletRequest.getParameter("ff1"));
            map.put("ff2",httpServletRequest.getParameter("ff2"));
            map.put("fg1",httpServletRequest.getParameter("fg1"));
            map.put("fh1_1",httpServletRequest.getParameter("fh1_1"));
            map.put("fh2_1",httpServletRequest.getParameter("fh2_1"));
            map.put("fh3_1",httpServletRequest.getParameter("fh3_1"));
            map.put("fh4_1",httpServletRequest.getParameter("fh4_1"));
            map.put("fh5_1",httpServletRequest.getParameter("fh5_1"));
            map.put("fh6_1",httpServletRequest.getParameter("fh6_1"));
            map.put("fi",httpServletRequest.getParameter("fi"));
            map.put("fj",httpServletRequest.getParameter("fj"));
            advertisingViolationService.updateParam(map);
            return "成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "操作失败，联系管理员";
        }
    }

    @PostMapping(value = "/selectSearchTermByLowAcos")
    @ResponseBody
    public List<Map<String,Object>> selectSearchTermByLowAcos(@RequestParam(value = "acos")String acos){
        List<Map<String, Object>> list = advertisingViolationService.selectSearchTermByLowAcos(acos);
        return list;
    }
}
