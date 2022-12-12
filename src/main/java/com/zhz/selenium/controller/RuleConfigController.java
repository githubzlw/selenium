package com.zhz.selenium.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.zhz.selenium.mapper.*;
import com.zhz.selenium.pojo.BiddingRuleConfig;
import com.zhz.selenium.pojo.RuleConfig;
import com.zhz.selenium.pojo.RuleConfigDetail;
import com.zhz.selenium.service.*;
import com.zhz.selenium.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin(origins = "*")
public class RuleConfigController {
    @Autowired
    private RuleConfigService ruleConfigService;

    @Autowired
    private BiddingRuleConfigService biddingRuleConfigService;

    @Autowired
    private BiddingRuleConfigMapper biddingRuleConfigMapper;

    @Autowired
    private ImportRuleConfigService importRuleConfigService;

    @Autowired
    private ImportRuleConfigMapper importRuleConfigMapper;

    @Autowired
    private NegativeRuleConfigService negativeRuleConfigService;

    @Autowired
    private NegativeRuleConfigMapper negativeRuleConfigMapper;

    @Autowired
    private NegativeWordConfigService negativeWordConfigService;

    @Autowired
    private NegativeWordConfigMapper negativeWordConfigMapper;

    @Autowired
    private StatusRuleConfigService statusRuleConfigService;

    @Autowired
    private StatusRuleConfigMapper statusRuleConfigMapper;


    /**
     * 基本配置 config
     * @param config
     */
    @RequestMapping(value = "/saveConfig")
    @ResponseBody
    public void saveConfig(@RequestBody Map<String,Object> config){
        RuleConfig ruleConfig = new RuleConfig();
        ruleConfig.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        ruleConfig.setKeyName(StringUtil.toString(config.get("name")));
        RuleConfigDetail ruleConfigDetail = JSONObject.parseObject(JSONObject.toJSONString(config), RuleConfigDetail.class);
        ruleConfigService.saveConfig(ruleConfig,ruleConfigDetail);
    }

    @RequestMapping(value = "/deleteConfig")
    @ResponseBody
    public void deleteConfig(@RequestParam(value = "configId") Integer id){
        ruleConfigService.deleteConfig(id);
    }

    @RequestMapping(value = "/updateConfig")
    @ResponseBody
    public void updateConfig(@RequestBody Map<String,Object> config){
        RuleConfig ruleConfig = new RuleConfig();
        ruleConfig.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        ruleConfig.setKeyName(StringUtil.toString(config.get("name")));
        ruleConfig.setId(Integer.parseInt(StringUtil.toString(config.get("keyid"))));
        RuleConfigDetail ruleConfigDetail = JSONObject.parseObject(JSONObject.toJSONString(config), RuleConfigDetail.class);
        ruleConfigService.updateConfig(ruleConfig,ruleConfigDetail);
    }

    @RequestMapping(value = "/selectConfig")
    @ResponseBody
    public List<RuleConfig> selectConfig(){
        List<RuleConfig> maps = ruleConfigService.selectConfig();
        return maps;
    }

    @RequestMapping(value = "/selectConfigDetail")
    @ResponseBody
    public List<RuleConfigDetail> selectConfigDetail(@RequestParam(value = "configId") Integer id){
        List<RuleConfigDetail> maps = ruleConfigService.selectConfigDetail(id);
        return maps;
    }

    /**
     * 影响数据展示
     * @param rulePage 哪个页面名字
     * @param rulePageId 页面所对应的id
     * @return
     */
    @RequestMapping(value = "/selectConfigDetailData")
    @ResponseBody
    public Map<String, List<Map<String, Object>>> selectConfigDetailData(@RequestParam(value = "rulePage") String rulePage,@RequestParam(value = "rulePageId") Integer rulePageId){
        Map<String, List<Map<String, Object>>> maps = ruleConfigService.selectConfigDetailData(rulePage,rulePageId);
        return maps;
    }



    @RequestMapping(value = "/advertisingExecute")
    @ResponseBody
    public void advertisingExecute(){
        Map<String, Object> map = new HashMap<>();
        map.put("tab_bidding_rule_confing",biddingRuleConfigMapper.selectBiddingRule());
        map.put("tab_import_rule_config",importRuleConfigMapper.selectImportRule());
        map.put("tab_negative_rule_config",negativeRuleConfigMapper.selectNegativeRule());
        map.put("tab_negative_word_config",negativeWordConfigMapper.selectNegativeWord());
        map.put("tab_status_rule_config",statusRuleConfigMapper.selectStatusRule());
        for (Map.Entry entry :map.entrySet()) {
            switch (entry.getKey().toString()) {
                case "tab_bidding_rule_confing":
                    biddingRuleConfigService.executeBiddingRule("tab_bidding_rule_confing",entry.getValue());
                    break;
                case "tab_import_rule_config":
                    importRuleConfigService.executeImportRule("tab_import_rule_config",entry.getValue());
                    break;
                case "tab_negative_rule_config":
                    negativeRuleConfigService.executeNegativeRule("tab_negative_rule_config",entry.getValue());
                    break;
                case "tab_negative_word_config":
                    negativeWordConfigService.executeNegativeWord("tab_negative_word_config",entry.getValue());
                    break;
                case "tab_status_rule_config":
                    statusRuleConfigService.executeStatusRule("tab_status_rule_config",entry.getValue());
                    break;
                default:
                    break;
            }
        }
    }
}
