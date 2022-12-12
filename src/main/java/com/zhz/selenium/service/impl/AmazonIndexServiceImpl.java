package com.zhz.selenium.service.impl;

import cn.hutool.core.date.DateUtil;
import com.zhz.selenium.mapper.AmazonMapper;
import com.zhz.selenium.pojo.CompareProduct;
import com.zhz.selenium.pojo.SearchResult;
import com.zhz.selenium.service.AmazonIndexService;
import com.zhz.selenium.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AmazonIndexServiceImpl implements AmazonIndexService {

    @Autowired
    private AmazonMapper amazonMapper;

    @Override
    public List<SearchResult> searchAll(String searchDate, String rankMethod, String num,String keyWord) {
        try {
            StringBuffer sb = new StringBuffer();
            keyWordSplit(sb,keyWord);
//            return amazonMapper.selectAll(searchDate+"%",rankMethod,num, sb.substring(1));
            List<String> keyWordId = amazonMapper.selectKeyWordIdByKewWord(sb.substring(1));
            if(keyWordId.size()>0){
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < keyWordId.size(); i++) {
                    stringBuffer.append(",").append("'").append(keyWordId.get(i)).append("'");
                }
                List<String> searchId = amazonMapper.selectSearchIdByKeyWordId(stringBuffer.substring(1), searchDate + "%");
                if (searchId.size()>0){
                    StringBuffer strb = new StringBuffer();
                    for (int i = 0; i < searchId.size(); i++) {
                        strb.append(",").append("'").append(searchId.get(i)).append("'");
                    }
                    List<SearchResult> searchResults = amazonMapper.selectAllProduct(rankMethod, num, stringBuffer.substring(1), strb.substring(1));
                    return searchResults;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public SearchResult competeProduct(String searchDate, String keyWord, String asin) {
        try {
            StringBuffer sb = new StringBuffer();
            keyWordSplit(sb,keyWord);
            return amazonMapper.selectCompeteProduct(searchDate+"%", sb.substring(1),asin);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<SearchResult> compareCompeteProduct(String rankMethod, String keyWord, String ids) {
        try {
            StringBuffer sb = new StringBuffer();
            idsSplit(sb,ids);
            return amazonMapper.compareCompeteProduct(rankMethod,keyWord,sb.substring(1));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer searchProductIdByAsin(String asin) {
        try {
            return amazonMapper.searchProductIdByAsin(asin);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void insertKeyWordProduct(List<String> keyWordIds,String asin,String ids) {
        try {
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            for (String keyWordId:keyWordIds) {
                amazonMapper.updateKeyWordProduct(Integer.parseInt(keyWordId),asin,date);
            }
            List<CompareProduct> list = new ArrayList<>();
            StringBuffer sb = new StringBuffer();
            idsSplit(sb,ids);
            List<Map<String, Object>> proList = amazonMapper.selectAsinById(sb.substring(1));
            for (Map<String, Object> m: proList) {
                for (String keyWordId:keyWordIds) {
                    CompareProduct compareProduct = new CompareProduct();
                    compareProduct.setKeyWordId(Integer.parseInt(keyWordId));
                    compareProduct.setAmzProductAsin(asin);
                    compareProduct.setAmzProductCompeteId(StringUtil.toString(m.get("id")));
                    compareProduct.setAmzProductCompeteAsin(StringUtil.toString(m.get("amz_asin")));
                    compareProduct.setAmzProductDel(0);
                    list.add(compareProduct);
                }
            }
            amazonMapper.insertKeyWordProduct(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> selectKeyWordByCpc() {
        try {
            return amazonMapper.selectKeyWordByCpc();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public List<Map<String,Object>> rankingByAsin(String startTime, String endTime, String asin, String keyWord,String campaignName) {
        try {
            // 找对应的出价曝光量等
            String campaignId = amazonMapper.selectDataByCampaignId(campaignName);
            List<Map<String, Object>> searchList = amazonMapper.selectSearchTermByAsinAndTime(keyWord, asin, startTime, endTime,campaignId);
            return searchList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> rankingByAsin1(String startTime, String endTime, String asin, String keyWord) {
        try {
            // 找对应的出价曝光量等
            List<Map<String, Object>> searchList = amazonMapper.selectSearchTermByAsinAndTime1(keyWord, asin, startTime, endTime);
            return searchList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> compareProductByAsinAndDate(String startTime, String endTime, String asin) {
        try {
            // 找到抓取到的全部数据
            List<Map<String, Object>> keywordList = amazonMapper.selectProductByAsinAndTime(asin,startTime,endTime);
            if (keywordList.size()>0) {
                for (Map<String,Object> map :keywordList) {
                    map.put("impressions","0");
                    map.put("amz_product_normal_count",map.get("amz_product_normal_count").toString().replaceAll(",","-->"));
                    map.put("amz_product_not_normal_count",map.get("amz_product_not_normal_count").toString().replaceAll(",","-->"));
                    // 根据关键词去曝光量
                    List<Map<String, Object>> impressionList = amazonMapper.selectSearchTermByKeyWordAndAsin(asin, startTime, endTime);
                    if (impressionList.size()>0){
                        for (Map<String,Object> imap:impressionList) {
                            if (StringUtil.toString(map.get("keyword")).equals(StringUtil.toString(imap.get("Customer_Search_Term")))){
                                map.put("impressions",StringUtil.toString(imap.get("impressions")).replaceAll(",","-->"));
                                break;
                            }
                        }
                    }
                }
                return keywordList;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectCampaignByTime(String startTime, String endTime,String yesTime,String lasWeekTime) {
        try {
            return amazonMapper.selectCampaignByTime(startTime,endTime,yesTime,lasWeekTime);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectCampaignByTimeAndPerson(String startTime, String endTime
            , String campaignName, String portfolio,String yesTime,String lasWeekTime) {
        try {
            return amazonMapper.selectCampaignByTimeAndPerson(startTime,endTime,campaignName,portfolio, yesTime,lasWeekTime);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectDataByCampaign(String startTime, String endTime, String campaignName) {
        try {
            String campaignId = amazonMapper.selectDataByCampaignId(campaignName);
            List<Map<String, Object>> list = amazonMapper.selectDataByCampaign(startTime, endTime, campaignId);
            List<Map<String, Object>> maps = amazonMapper.selectSearchVolume(startTime, endTime);
            for (Map<String,Object> map:list) {
                if (!StringUtil.isEmpty(map.get("Advertised_ASIN")) && !StringUtil.isEmpty(map.get("keywordid")) && !"0".equals(map.get("Advertised_ASIN")) && !"0".equals(map.get("keywordid"))){
                    List<Map<String,Object>> ids = amazonMapper.selectCompareIdByAsinAndKeyWordId(map.get("keywordid").toString(), map.get("Advertised_ASIN").toString());
                    if (!StringUtil.isEmpty(ids)){
                        StringBuffer sb = new StringBuffer();
                        for (Map m:ids) {
                            sb.append(",").append("'").append(m.get("amz_product_compete_id")).append("'");
                        }
                        List<Map<String, Object>> compares = amazonMapper.selectCompareById(sb.substring(1));
                        List<Map<String, Object>> before_5_product = new ArrayList<>();
                        List<Map<String, Object>> before_15_product = new ArrayList<>();
                        for (Map m:compares) {
                            for (Map idm:ids) {
                                if (StringUtil.toString(idm.get("amz_product_compete_id")).equals(StringUtil.toString(m.get("id")))){
                                    m.put("min_price",idm.get("min_price"));
                                    m.put("max_price",idm.get("max_price"));
                                }
                            }
                            if (0<Integer.parseInt(m.get("amz_product_count").toString()) &&
                                    Integer.parseInt(m.get("amz_product_count").toString())<=5){
                                Map<String, Object> hashMap = new HashMap<>();
                                hashMap.put("amz_product_price",m.get("amz_product_price"));
                                hashMap.put("amz_product_img",m.get("amz_product_img"));
                                hashMap.put("min_price",m.get("min_price"));
                                hashMap.put("max_price",m.get("max_price"));
                                before_5_product.add(hashMap);
                            }
                            if (5<Integer.parseInt(m.get("amz_product_count").toString()) &&
                                    Integer.parseInt(m.get("amz_product_count").toString())<=15){
                                Map<String, Object> hashMap = new HashMap<>();
                                hashMap.put("amz_product_price",m.get("amz_product_price"));
                                hashMap.put("amz_product_img",m.get("amz_product_img"));
                                hashMap.put("min_price",m.get("min_price"));
                                hashMap.put("max_price",m.get("max_price"));
                                before_15_product.add(hashMap);
                            }
                        }
                        map.put("before_5_num",before_5_product.size());
                        map.put("before_5_product",before_5_product.size()==0?"-":before_5_product);
                        map.put("before_15_num",before_15_product.size());
                        map.put("before_15_product",before_15_product.size()==0?"-":before_15_product);
                    }else {
                        map.put("before_5_num","-");
                        map.put("before_5_product","-");
                        map.put("before_15_num","-");
                        map.put("before_15_product","-");
                    }
                }else {
                    map.put("before_5_num","-");
                    map.put("before_5_product","-");
                    map.put("before_15_num","-");
                    map.put("before_15_product","-");
                }
                // 添加前后商品
                if (!"0".equals(StringUtil.toString(map.get("amz_product_not_normal_count")))
                        && !"".equals(StringUtil.toString(map.get("search_date")))){
                    Map<String, Object> indexResult = amazonMapper.selectData(map.get("keywordid"), map.get("Advertised_ASIN")
                            , map.get("amz_product_not_normal_count"),StringUtil.toString(map.get("search_date")).substring(0,10));
                    if(!StringUtil.isEmpty(indexResult)){
                        List<Map<String, Object>> indexResultList = amazonMapper.selectDataByIndex(map.get("keywordid")
                                , (Integer.parseInt(StringUtil.saveInt(StringUtil.toString(map.get("amz_product_not_normal_count")))) - 2)
                                , (Integer.parseInt(StringUtil.saveInt(StringUtil.toString(map.get("amz_product_not_normal_count")))) - 1)
                                , (Integer.parseInt(StringUtil.saveInt(StringUtil.toString(map.get("amz_product_not_normal_count")))) + 1)
                                , (Integer.parseInt(StringUtil.saveInt(StringUtil.toString(map.get("amz_product_not_normal_count")))) + 2)
                                , StringUtil.toString(map.get("search_date")).substring(0,10));
                        if (!StringUtil.isEmpty(indexResultList)){
                            for (int i = 0; i < indexResultList.size(); i++) {
                                map.put("index"+i+"_img",indexResultList.get(i).get("amz_product_img"));
                                map.put("index"+i+"_price",indexResultList.get(i).get("amz_product_price"));
                            }
                        }
                    }
                }
                // 添加月搜索量
                for (Map<String, Object> m :maps) {
                    if (StringUtil.toString(map.get("customer_search_term")).equals(StringUtil.toString(m.get("keyword_phrase")))){
                        map.put("searchCount",m.get("search_volume"));
                        break;
                    }
                }
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String start = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), -9));
                String acos7 = amazonMapper.selectDay7Acos(map.get("campaign_id"), start, endTime, map.get("customer_search_term"));
                map.put("acos7","0.00".equals(acos7)?"0":acos7);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectCampaignByDate(String startTime, String endTime, String campaignName,String asin) {
        try {
            List<Map<String, Object>> maps = amazonMapper.selectCampaignByDate(startTime + " 00:00:00", endTime + " 23:59:59", campaignName,asin);
            // 根据日期添加 单价，session，产品销量，tacos
            String price = amazonMapper.selectPriceByAsin(asin);
            List<Map<String, Object>> msg = amazonMapper.selectAsinMsg(asin, startTime, endTime);
            for (Map<String, Object> fmap: maps) {
                fmap.put("price",price);
                for (Map<String, Object> smap:msg) {
                    if (fmap.get("date").equals(smap.get("create_date"))){
                        BigDecimal sale = new BigDecimal(StringUtil.toString(smap.get("sale")));
                        BigDecimal spend = new BigDecimal(StringUtil.toString(fmap.get("spend")));
                        if (sale.compareTo(BigDecimal.ZERO)>0
                                &&spend.compareTo(BigDecimal.ZERO)>0){
                            fmap.put("tacos",new DecimalFormat("0.00%").format(spend.divide(sale,4,BigDecimal.ROUND_HALF_UP)));
                        }else {
                            fmap.put("tacos","0%");
                        }
                        fmap.put("asin_session",smap.get("asin_session"));
                        fmap.put("sale",new DecimalFormat("0.00").format(sale));
                        break;
                    }
                }
            }
            return maps;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<String> selectKeyWordIdByKeyWord(String keyWord) {
        try {
            StringBuffer sb = new StringBuffer();
            keyWordSplit(sb,keyWord);
            return amazonMapper.selectKeyWordIdByKewWord(sb.substring(1));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Map<String, Object> selectCampaignByKeyWordAndAsin(String keyWord, String asin) {
        try {
            return amazonMapper.selectCampaignByKeyWordAndAsin(keyWord,asin);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectAsinByTime(String startTime, String date) {
        try {
            return amazonMapper.selectAsinByTime(startTime,date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectDataByAsin(String asin, String startTime, String date, String yesTime, String lasWeekTime, String twLasWeekTime, String thrLasWeekTime) {
        try {
            return amazonMapper.selectDataByAsin(asin, startTime+" 00:00:00", date+" 23:59:59", yesTime+"%", lasWeekTime+"%", twLasWeekTime+"%", thrLasWeekTime+"%");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectAcosByAsin(String asin, String startTime, String date, String lasWeekTime) {
        try {
            return amazonMapper.selectAcosByAsin(asin, startTime+" 00:00:00", date+" 23:59:59",  lasWeekTime+"%");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 处理关键词 / 分号分割
    private void keyWordSplit(StringBuffer sb,String keyWord){
        if (StringUtils.isNotEmpty(keyWord)){
            String[] split = keyWord.split(";");
            for (String str:split){
                sb.append(",").append("'").append(str).append("'");
            }
        }
    }

    // 处理点击的id / 逗号分割
    private void idsSplit(StringBuffer sb,String ids){
        if (StringUtils.isNotEmpty(ids)){
            String[] split = ids.split(",");
            for (String str:split){
                sb.append(",").append("'").append(str).append("'");
            }
        }
    }

}
