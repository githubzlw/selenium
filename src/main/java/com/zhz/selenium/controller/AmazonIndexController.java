package com.zhz.selenium.controller;

import cn.hutool.core.date.DateUtil;
import com.zhz.selenium.mapper.AmazonMapper;
import com.zhz.selenium.pojo.SearchResult;
import com.zhz.selenium.service.AmazonIndexService;
import com.zhz.selenium.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class AmazonIndexController {

    @Autowired
    private AmazonIndexService amazonIndexService;

    @Autowired
    private AmazonMapper amazonMapper;

    @RequestMapping(value = "/index")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();
        List<String> list = amazonIndexService.selectKeyWordByCpc();
        if (list.size()>0){
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < list.size(); i++) {
                sb.append(";").append(list.get(i));
            }
            modelAndView.addObject("keyWord",sb.substring(1));
        }
        modelAndView.setViewName("home");
        return modelAndView;
    }

    @GetMapping(value = "/page_1")
    public ModelAndView page_1(@RequestParam(value = "ids")String ids,@RequestParam(value = "asin")String asin
            ,@RequestParam(value = "keyWord")String keyWord){
        ModelAndView modelAndView = new ModelAndView();
//        Integer keyWordId = amazonIndexService.searchProductIdByAsin(asin);
        List<String> keyWordIds = amazonIndexService.selectKeyWordIdByKeyWord(keyWord);
        if (!StringUtil.isEmpty(keyWordIds)){
            amazonIndexService.insertKeyWordProduct(keyWordIds,asin,ids);
        }
        modelAndView.setViewName("page_1");
        return modelAndView;
    }

    @GetMapping(value = "/page_2")
    public ModelAndView page_2(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("page_2");
        return modelAndView;
    }

    @GetMapping(value = "/page_3")
    public ModelAndView page_3(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("page_3");
        return modelAndView;
    }

    @GetMapping(value = "/search_term_compare")
    public ModelAndView search_term_compare(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("search_term_compare");
        return modelAndView;
    }


    @GetMapping(value = "/campaign_list")
    public ModelAndView campagin_list(){
        ModelAndView modelAndView = new ModelAndView();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String startTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), -34));
        String yesTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), -4));
        String lasWeekTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), -11));
        List<Map<String, Object>> list = amazonIndexService.selectCampaignByTime(startTime, yesTime,yesTime,lasWeekTime);
        for (Map<String, Object> m:list) {
            String[] asins = StringUtil.toString(m.get("Advertised_ASIN1")).split(",");
            if (StringUtil.isEmpty(m.get("amz_product_img"))){
                for (String asin:asins) {
                    // 图片
                    String img = amazonMapper.selectImgByAsin(asin);
                    if (!StringUtil.isEmpty(img)){
                        m.put("amz_product_img",img);
                        break;
                    }
                }
            }
            // tacos
            StringBuffer sb = new StringBuffer();
            for (String asin:asins) {
                sb.append(",").append("'").append(asin).append("'");
            }
            Map<String, Object> spapi = amazonMapper.selectTacos(sb.substring(1), startTime, lasWeekTime, yesTime);
            BigDecimal spend7 = new BigDecimal(StringUtil.toString(m.get("spend2")));
            BigDecimal total7 = new BigDecimal(StringUtil.toString(spapi.get("total7")));
            BigDecimal spend30 = new BigDecimal(StringUtil.toString(m.get("spend1")));
            BigDecimal total30 = new BigDecimal(StringUtil.toString(spapi.get("total30")));
            if (total7.compareTo(BigDecimal.ZERO)>0
                    &&spend7.compareTo(BigDecimal.ZERO)>0){
                m.put("tacos7",new DecimalFormat("0.00%").format(spend7.divide(total7,4,BigDecimal.ROUND_HALF_UP)));
            }else {
                m.put("tacos7","0%");
            }
            if (total30.compareTo(BigDecimal.ZERO)>0
                    &&spend30.compareTo(BigDecimal.ZERO)>0){
                m.put("tacos30",new DecimalFormat("0.00%").format(spend30.divide(total30,4,BigDecimal.ROUND_HALF_UP)));
            }else {
                m.put("tacos30","0%");
            }
        }
        modelAndView.addObject("list",list);
        modelAndView.addObject("startTime",startTime);
        modelAndView.addObject("date",yesTime);
        modelAndView.setViewName("campaign_list");
        return modelAndView;
    }




    /**
     * @param rankMethod 排名方式
     */
    @PostMapping(value = "/search")
    @ResponseBody
    public List<SearchResult> search(@RequestParam(value = "searchDate",required = false)String searchDate
            , @RequestParam(value = "rankMethod",required = false)String rankMethod
            , @RequestParam(value = "num" ,required = false,defaultValue = "0")String num
            , @RequestParam(value = "keyWord")String keyWord){
        List<SearchResult> searchResults = amazonIndexService.searchAll(searchDate, rankMethod, num,keyWord);
        return searchResults;
    }

    @PostMapping(value = "/competeProduct")
    @ResponseBody
    public SearchResult competeProduct(@RequestParam(value = "searchDate",required = false)String searchDate
            , @RequestParam(value = "keyWord")String keyWord
            , @RequestParam(value = "asin")String asin){

        SearchResult searchResult = amazonIndexService.competeProduct(searchDate, keyWord, asin);
        return searchResult==null?new SearchResult():searchResult;
    }




    // 根据关键词和排名方式显示竞品
    @PostMapping(value = "/compareCompeteProduct")
    @ResponseBody
    public List<SearchResult> compareCompeteProduct(@RequestParam(value = "rankMethod")String rankMethod
            , @RequestParam(value = "keyWord")String keyWord
            , @RequestParam(value = "ids",required = false)String ids){

        List<SearchResult> searchResults = amazonIndexService.compareCompeteProduct(rankMethod, keyWord, ids);
        return searchResults;

    }

    // 根据关键词和时间显示竞品
    @PostMapping(value = "/rankingByAsin")
    @ResponseBody
    public List<Map<String, Object>> rankingByAsin(@RequestParam(value = "startTime")String startTime,@RequestParam(value = "endTime")String endTime
            ,@RequestParam(value = "asin")String asin,@RequestParam(value = "keyWord")String keyWord,@RequestParam(value = "campaignName",required = false)String campaignName){

        if (StringUtil.isEmpty(campaignName) || "null".equals(campaignName)){
            return amazonIndexService.rankingByAsin1(startTime, endTime, asin, keyWord);
        }else {
            List<Map<String, Object>> list = amazonIndexService.rankingByAsin(startTime, endTime, asin, keyWord,campaignName);
            return list;
        }

    }


    // 根据asin和时间来比较
    @PostMapping(value = "/compareProductByAsinAndDate")
    @ResponseBody
    public List<Map<String, Object>> compareProductByAsinAndDate(@RequestParam(value = "startTime")String startTime,@RequestParam(value = "endTime")String endTime
            ,@RequestParam(value = "asin")String asin){
        List<Map<String, Object>> list = amazonIndexService.compareProductByAsinAndDate(startTime, endTime, asin);
        return list;
    }

    // 根据组名来查找所属的相关数据 （近30天）
    @PostMapping(value = "/selectDataByCampaign")
    @ResponseBody
    public List<Map<String,Object>> selectDataByCampaign(@RequestParam(value = "campaignName")String campaignName){
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String startTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), -32));
        String endTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), -2));
        List<Map<String, Object>> list = amazonIndexService.selectDataByCampaign(startTime, endTime,campaignName);
        return list;
    }

    // 根据日期和组名查找组相关的内容
    @PostMapping(value = "/selectCampaignByDate1")
    @ResponseBody
    public List<Map<String,Object>> selectCampaignByDate1(@RequestParam(value = "startTime")String startTime
            ,@RequestParam(value = "endTime")String endTime,@RequestParam(value = "keyWord")String keyWord
            ,@RequestParam(value = "asin")String asin,@RequestParam(value = "campaignName",required = false)String campaignName){
        if (StringUtil.isEmpty(campaignName) || "null".equals(campaignName)){
            Map<String, Object> map = amazonIndexService.selectCampaignByKeyWordAndAsin(keyWord, asin);
            if (!StringUtil.isEmpty(map)){
                String s = amazonMapper.selectDataByCampaignId(campaignName);
                List<Map<String, Object>> list = amazonIndexService.selectCampaignByDate(startTime,endTime,s,asin);
                return list;
            }else {
                return null;
            }
        }else {
            String s = amazonMapper.selectDataByCampaignId(campaignName);
            List<Map<String, Object>> list = amazonIndexService.selectCampaignByDate(startTime,endTime,s,asin);
            return list;
        }
    }


    // 根据日期和组名查找组相关的内容
    @PostMapping(value = "/selectCampaignByTimeAndPerson")
    @ResponseBody
    public List<Map<String, Object>> selectCampaignByTimeAndPerson(@RequestParam(value = "campaignName",required = false)String campaignName
            , @RequestParam(value = "portfolio",required = false)String portfolio){
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String startTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), -30));
        String yesTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), -1));
        String lasWeekTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), -7));
        List<Map<String, Object>> list = amazonIndexService.selectCampaignByTimeAndPerson(startTime, date
                , campaignName, portfolio,yesTime,lasWeekTime);
        return list;
    }


    // 根据日期和asin查找相关内容
    @PostMapping(value = "/selectDataByAsin")
    @ResponseBody
    public List<Map<String, Object>> selectDataByAsin(@RequestParam(value = "asin")String asin){
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String startTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), -30));
        String yesTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), -2));
        String lasWeekTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), -7));
        String twLasWeekTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), -14));
        String thrLasWeekTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), -21));
        List<Map<String, Object>> list = amazonIndexService.selectDataByAsin(asin,startTime,date,yesTime,lasWeekTime,twLasWeekTime,thrLasWeekTime);
        return list;
    }

    // 根据日期和asin查找acos
    @PostMapping(value = "/selectAcosByAsin")
    @ResponseBody
    public List<Map<String, Object>> selectAcosByAsin(@RequestParam(value = "asin")String asin){
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String startTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), -30));
        String lasWeekTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), -7));
        List<Map<String, Object>> list = amazonIndexService.selectAcosByAsin(asin,startTime,date,lasWeekTime);
        return list;
    }

}
