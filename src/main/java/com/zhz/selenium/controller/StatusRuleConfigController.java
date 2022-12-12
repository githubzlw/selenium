package com.zhz.selenium.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhz.selenium.pojo.StatusRuleConfig;
import com.zhz.selenium.service.StatusRuleConfigService;
import com.zhz.selenium.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin(origins = "*")
public class StatusRuleConfigController {
    @Autowired
    private StatusRuleConfigService statusRuleConfigService;


    /**
     * 配置 Status rule
     * @param config
     */
    @RequestMapping(value = "/saveStatusRule")
    @ResponseBody
    public void saveStatusRule(@RequestBody Map<String,Object> config){
        StatusRuleConfig StatusRuleConfig = JSONObject.parseObject(JSONObject.toJSONString(config), StatusRuleConfig.class);
        statusRuleConfigService.saveStatusRule(StatusRuleConfig);
    }

    @RequestMapping(value = "/deleteStatusRule")
    @ResponseBody
    public void deleteStatusRule(@RequestParam(value = "id") Integer id){
        statusRuleConfigService.deleteStatusRule(id);
    }

    @RequestMapping(value = "/updateStatusRule")
    @ResponseBody
    public void updateStatusRule(@RequestBody Map<String,Object> config){
        saveStatusRule(config);
        deleteStatusRule(Integer.parseInt(StringUtil.toString(config.get("id"))));
    }

    @RequestMapping(value = "/selectStatusRule")
    @ResponseBody
    public List<StatusRuleConfig> selectStatusRule(){
        List<StatusRuleConfig> maps = statusRuleConfigService.selectStatusRule();
        return maps;
    }

    @RequestMapping(value = "/selectStatusRuleDetail")
    @ResponseBody
    public List<StatusRuleConfig> selectStatusRuleDetail(@RequestParam(value = "id") Integer id){
        List<StatusRuleConfig> maps = statusRuleConfigService.selectStatusRuleDetail(id);
        return maps;
    }


}
