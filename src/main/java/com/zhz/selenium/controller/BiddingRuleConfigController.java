package com.zhz.selenium.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhz.selenium.pojo.BiddingRuleConfig;
import com.zhz.selenium.service.BiddingRuleConfigService;
import com.zhz.selenium.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin(origins = "*")
public class BiddingRuleConfigController {
    @Autowired
    private BiddingRuleConfigService biddingRuleConfigService;


    /**
     * 配置 billing rule
     * @param config
     */
    @RequestMapping(value = "/saveBiddingRule")
    @ResponseBody
    public void saveBiddingRule(@RequestBody Map<String,Object> config){
        BiddingRuleConfig billingRuleConfig = JSONObject.parseObject(JSONObject.toJSONString(config), BiddingRuleConfig.class);
        biddingRuleConfigService.saveBiddingRule(billingRuleConfig);
    }

    @RequestMapping(value = "/deleteBiddingRule")
    @ResponseBody
    public void deleteBiddingRule(@RequestParam(value = "id") Integer id){
        biddingRuleConfigService.deleteBiddingRule(id);
    }

    @RequestMapping(value = "/updateBiddingRule")
    @ResponseBody
    public void updateBiddingRule(@RequestBody Map<String,Object> config){
        saveBiddingRule(config);
        deleteBiddingRule(Integer.parseInt(StringUtil.toString(config.get("id"))));
    }

    @RequestMapping(value = "/selectBiddingRule")
    @ResponseBody
    public List<BiddingRuleConfig> selectBiddingRule(){
        List<BiddingRuleConfig> maps = biddingRuleConfigService.selectBiddingRule();
        return maps;
    }

    @RequestMapping(value = "/selectBiddingRuleDetail")
    @ResponseBody
    public List<BiddingRuleConfig> selectBiddingRuleDetail(@RequestParam(value = "configId") Integer id){
        List<BiddingRuleConfig> maps = biddingRuleConfigService.selectBiddingRuleDetail(id);
        return maps;
    }



}
