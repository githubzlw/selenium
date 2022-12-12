package com.zhz.selenium.controller;

import com.zhz.selenium.pojo.ApiResult;
import com.zhz.selenium.service.LcAmazonApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/lcAmazonapi")
public class LcAmazonApiController {

    @Autowired
    private LcAmazonApiService lcAmazonApiService;

    @RequestMapping(value = "/rank", method = RequestMethod.GET)
    @ResponseBody
    public String rank(@RequestParam(value="searchterm") String searchterm){
        // 返回 ASIN, 自然排名 和  广告排名  ,用 ；做分割符
        List<ApiResult> srLists = lcAmazonApiService.selectRankSearchterm(searchterm);

        //根据asin聚合 数据
        List<ApiResult> apiList = new ArrayList<>();
        srLists.parallelStream().collect(Collectors.groupingBy(ApiResult::getAmzAsin, Collectors.toList()))
                .forEach((id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new ApiResult(a.getAmzAsin(), a.getZrPm()+"@"+b.getZrPm(), a.getGgPm() +"@"+ b.getGgPm())).ifPresent(apiList::add);
                });

        System.out.println(apiList);

        String strList = "";
        for(ApiResult obj : apiList){
            strList +=obj.getAmzAsin() + ","+obj.getZrPm()+","+obj.getGgPm()+";</br>";
        }

        System.out.println(strList);

        String strTitle ="ASIN,自然排名(最近30天),广告排名(最近30天)</br>";
//        StringBuilder stringBuilder = new StringBuilder();
//        for(ApiResult obj : apiList){
//            stringBuilder.append(obj.getAmzAsin()+",");
//            stringBuilder.append(obj.getZrPm()+",");
//            stringBuilder.append(obj.getGgPm()+";"+"\r\n");
//        }
//
//        System.out.println(stringBuilder);


        return strTitle+strList;
    }

    @RequestMapping(value = "/sales", method = RequestMethod.GET)
    @ResponseBody
    public String sales(@RequestParam(value="daterange") Integer daterange,@RequestParam(value="asin") String asin){

        //返回这段时间的 Session数(自然流量 sp api), 总订单数(sp api)，总广告点击数 (该ASIN所在Campaign的) ，广告花费 和 广告订单数 。   用 ；做分割符
        ApiResult bean = lcAmazonApiService.selectSalesAsin(daterange, asin);

        String strList ="";
        strList +=bean.getAsinSession() + ","+bean.getUnitOrder()+","+bean.getClicks()+","+bean.getSpend()+","+bean.getDay7TotalOrders();

        String strTitle ="Session数(自然流量 sp api),总订单数(sp api),总广告点击数(该ASIN所在Campaign的),广告花费,广告订单数</br>";

        return strTitle+strList;
    }

    @RequestMapping(value = "/keywords", method = RequestMethod.GET)
    @ResponseBody
    public String keywords(@RequestParam(value="daterange") Integer daterange,@RequestParam(value="asin") String asin){

        //返回这段时间的,该ASIN所在Campaign的, 的前20名 search term （最多20个,按点击数排序）,返回数据格式
        List<ApiResult> list = lcAmazonApiService.selectKeywordsAsin(daterange, asin);

        String strList ="";
        for(ApiResult bean : list){
            strList +=bean.getCustomerSearchTerm() + ","+bean.getClicks()+","+bean.getCpc()+","+bean.getImpressions()+","+bean.getDay7TotalOrders()+";"+"</br>";

        }

        String strTitle ="search term,点击数,CPC,曝光量,订单数</br>";

        return strTitle+strList;
    }

    @RequestMapping(value = "/rankAsin", method = RequestMethod.GET)
    @ResponseBody
    public String selectRankAsin(@RequestParam(value="asin") String asin){

        //返回 search term, 自然排名 和  广告排名  ,用 ；做分割符
        List<ApiResult> list = lcAmazonApiService.selectRankAsin(asin);

        String strList ="";
        for(ApiResult bean : list){
            strList +=bean.getCustomerSearchTerm() + ","+bean.getNormalCount()+","+bean.getNotNormalCount()+";"+"</br>";

        }

        String strTitle ="search term,自然排名,广告排名</br>";

        return strTitle+strList;
    }

}
