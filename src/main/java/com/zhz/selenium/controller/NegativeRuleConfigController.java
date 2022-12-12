package com.zhz.selenium.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhz.selenium.pojo.NegativeRuleConfig;
import com.zhz.selenium.service.NegativeRuleConfigService;
import com.zhz.selenium.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin(origins = "*")
public class NegativeRuleConfigController {
    @Autowired
    private NegativeRuleConfigService negativeRuleConfigService;


    /**
     * 配置 Negative rule
     * @param config
     */
    @RequestMapping(value = "/saveNegativeRule")
    @ResponseBody
    public void saveNegativeRule(@RequestBody Map<String,Object> config){
        NegativeRuleConfig negativeRuleConfig = JSONObject.parseObject(JSONObject.toJSONString(config), NegativeRuleConfig.class);
        negativeRuleConfigService.saveNegativeRule(negativeRuleConfig);
    }

    @RequestMapping(value = "/deleteNegativeRule")
    @ResponseBody
    public void deleteNegativeRule(@RequestParam(value = "id") Integer id){
        negativeRuleConfigService.deleteNegativeRule(id);
    }

    @RequestMapping(value = "/updateNegativeRule")
    @ResponseBody
    public void updateNegativeRule(@RequestBody Map<String,Object> config){
        saveNegativeRule(config);
        deleteNegativeRule(Integer.parseInt(StringUtil.toString(config.get("id"))));
    }

    @RequestMapping(value = "/selectNegativeRule")
    @ResponseBody
    public List<NegativeRuleConfig> selectNegativeRule(){
        List<NegativeRuleConfig> maps = negativeRuleConfigService.selectNegativeRule();
        return maps;
    }


    @RequestMapping(value = "/selectNegativeRuleDetail")
    @ResponseBody
    public List<NegativeRuleConfig> selectNegativeRuleDetail(@RequestParam(value = "id") Integer id){
        List<NegativeRuleConfig> maps = negativeRuleConfigService.selectNegativeRuleDetail(id);
        return maps;
    }


}
