package com.zhz.selenium.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.zhz.selenium.mapper.RuleConfigMapper;
import com.zhz.selenium.pojo.RuleConfig;
import com.zhz.selenium.pojo.RuleConfigDetail;
import com.zhz.selenium.service.AmazonAdAutoService;
import com.zhz.selenium.service.RuleConfigService;
import com.zhz.selenium.util.RuleUtil;
import com.zhz.selenium.util.StringUtil;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RuleConfigServiceImpl implements RuleConfigService {
    Log log = LogFactory.get();

    @Autowired
    private RuleConfigMapper ruleConfigMapper;

    @Autowired
    private RuleUtil ruleUtil;

    @Autowired
    private AmazonAdAutoService amazonAdAutoService;

    @Override
    public void saveConfig(RuleConfig ruleConfig, RuleConfigDetail ruleConfigDetail) {
        try {
            StringBuffer condition = ruleUtil.condition(ruleConfigDetail);
            ruleConfig.setCondition(StringUtil.isEmpty(StringUtil.toString(condition))?"":condition.substring(1));
            ruleConfig.setDelState("1");
            ruleConfigMapper.saveRuleConfig(ruleConfig);
            ruleConfigDetail.setKeyId(ruleConfig.getId());
            ruleConfigMapper.saveRuleConfigDetail(ruleConfigDetail);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @Override
    public List<RuleConfig> selectConfig() {
        try {
            List<RuleConfig> maps = ruleConfigMapper.selectConfig();
            return maps;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    @Override
    public void deleteConfig(Integer id) {
        try {
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            ruleConfigMapper.deleteConfig(id,date);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @Override
    public List<RuleConfigDetail> selectConfigDetail(Integer id) {
        try {
            List<RuleConfigDetail> maps = ruleConfigMapper.selectConfigDetail(id);
            return maps;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    @Override
    public void updateConfig(RuleConfig ruleConfig, RuleConfigDetail ruleConfigDetail) {
        try {
            StringBuffer condition = ruleUtil.condition(ruleConfigDetail);
            ruleConfig.setCondition(StringUtil.toString(condition.substring(1)));
            ruleConfigMapper.updateConfig(ruleConfig);
            ruleConfigDetail.setKeyId(ruleConfig.getId());
            ruleConfigMapper.saveRuleConfigDetail(ruleConfigDetail);
            ruleConfigMapper.deleteConfigDetail(ruleConfig.getId());
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @Override
    public Map<String, List<Map<String, Object>>> selectConfigDetailData(String rulePage,Integer rulePageId) {
        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        String configId;
        String group;
        // 根据页面id查询规则id
        try {
            if ("tab_bidding_rule_confing".equals(rulePage)){
                String field = "IFNULL(keyword_conf_id,0)+IFNULL(ad_group_conf_id,0)+IFNULL(campaign_conf_id,0) configId" +
                        ",keyword_conf_id Customer_Search_Term,ad_group_conf_id ad_group_id,campaign_conf_id campaign_id";
                Map<String, Object> config = ruleConfigMapper.selectConfigIdByRuleId(field, rulePage, rulePageId);
                configId = StringUtil.toString(config.get("configId"));
                if (!StringUtil.isEmpty(config.get("Customer_Search_Term"))){
                    group = "keyword_id,campaign_id";
                }else if (!StringUtil.isEmpty(config.get("ad_group_id"))){
                    group = "ad_group_id,keyword_id";
                }else{
                    group = "campaign_id,keyword_id";
                }
            }else {
                Map<String, Object> config = ruleConfigMapper.selectConfigIdByRuleId("config_id,search_terms_source", rulePage, rulePageId);
                configId = StringUtil.toString(config.get("config_id"));
                group = StringUtil.toString(config.get("search_terms_source"));
                if ("ad_group_id".equals(group)){
                    group = "ad_group_id,Customer_Search_Term";
                }else if ("asin".equals(group)){

                }else {
                    group = "campaign_id,Customer_Search_Term";
                }
            }
            // 根据规则id查询规则
            List<RuleConfigDetail> ruleConfigDetails = ruleConfigMapper.selectConfigDetail(Integer.parseInt(configId));
            // 根据规则和页面id的分组 -> 组合sql
            if (ruleConfigDetails.size()>0){
                String field = "*,IFNULL(round((sum(spend)/sum(Day7_TotalSales))*100,2),0) t_acos" +
                        ",IFNULL(round((sum(Day7_TotalSales)/sum(spend))*100,2),0) t_roas" +
                        ",round(sum(Spend)/sum(Clicks),2) t_cpc" +
                        ",IFNULL(round((sum(Day7_TotalOrders)/sum(Clicks))*100,2),0) t_conversionRate" +
                        ",IFNULL(round((sum(Clicks)/sum(Impressions))*100,2),0) t_ctr";
                StringBuffer where = ruleUtil.viewData(ruleConfigDetails.get(0));
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String startTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), -32));
                String lastWeekTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), -9));
                String endTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), -2));
                String influenceStartTime = lastWeekTime;
                if ("最近7天".equals(ruleConfigDetails.get(0).getDateDuring())){
                    where.append(" and DATE_FORMAT(date, '%Y-%m-%d' ) BETWEEN '"+lastWeekTime+"'  AND '"+endTime+"'");
                    influenceStartTime = lastWeekTime;
                }else if ("最近30天".equals(ruleConfigDetails.get(0).getDateDuring())){
                    where.append(" and DATE_FORMAT(date, '%Y-%m-%d' ) BETWEEN '"+startTime+"'  AND '"+endTime+"'");
                    influenceStartTime = startTime;
                }
                StringBuffer having = new StringBuffer();
                ruleUtil.dealConditionData(having,ruleConfigDetails.get(0).getCpcMin(),ruleConfigDetails.get(0).getCpcMax(),"t_cpc");
                ruleUtil.dealConditionData(having,ruleConfigDetails.get(0).getCtrMin(),ruleConfigDetails.get(0).getCtrMax(),"t_ctr");
                ruleUtil.dealConditionData(having,ruleConfigDetails.get(0).getConversionMin(),ruleConfigDetails.get(0).getConversionMax(),"t_conversionRate");
                ruleUtil.dealConditionData(having,ruleConfigDetails.get(0).getAcosMin(),ruleConfigDetails.get(0).getAcosMax(),"t_acos");
                ruleUtil.dealConditionData(having,ruleConfigDetails.get(0).getRoasMin(),ruleConfigDetails.get(0).getAcosMax(),"t_roas");
                if ("asin".equals(group)){
                    List<Map<String, Object>> data = ruleConfigMapper.selectConfigDetailData(field, "searchterm"
                            , StringUtil.isEmpty(StringUtil.toString(where))?"":where.substring(4), "Customer_Search_Term,campaign_id"
                            , StringUtil.isEmpty(StringUtil.toString(having))?"":having.substring(4));
                    List<Map<String, Object>> list = amazonAdAutoService.dealAsin(data);
                    result.put("data",list);
                    for (Map<String, Object> m:list) {
                        List<Map<String, Object>> infData = ruleConfigMapper.selectKeywordInfluenceData(m.get("Customer_Search_Term"), influenceStartTime, endTime);
                        result.put(StringUtil.toString(m.get("Customer_Search_Term")),infData);
                    }
                    return result;
                }else {
                    List<Map<String, Object>> data = ruleConfigMapper.selectConfigDetailData(field, "searchterm"
                            , StringUtil.isEmpty(StringUtil.toString(where))?"":where.substring(4), StringUtil.toString(group)
                            , StringUtil.isEmpty(StringUtil.toString(having))?"":having.substring(4));
                    result.put("data",data);
                    for (Map<String, Object> m:data) {
                        if ("tab_bidding_rule_confing".equals(rulePage)){
                            if (!result.containsKey(m.get("Targeting"))){
                                List<Map<String, Object>> infData = ruleConfigMapper.selectTargetingInfluenceData(m.get("Targeting"), influenceStartTime, endTime);
                                result.put(StringUtil.toString(m.get("Targeting")),infData);
                            }
                        }else {
                            if (!result.containsKey(m.get("Customer_Search_Term"))){
                                List<Map<String, Object>> infData = ruleConfigMapper.selectKeywordInfluenceData(m.get("Customer_Search_Term"), influenceStartTime, endTime);
                                result.put(StringUtil.toString(m.get("Customer_Search_Term")),infData);
                            }
                        }
                    }
                    return result;
                }
            }
        } catch (NumberFormatException e) {
            log.info(e.getMessage());
        }
        return null;
    }
}
