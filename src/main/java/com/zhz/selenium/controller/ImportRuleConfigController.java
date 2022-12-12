package com.zhz.selenium.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhz.selenium.pojo.ImportRuleConfig;
import com.zhz.selenium.service.ImportRuleConfigService;
import com.zhz.selenium.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin(origins = "*")
public class ImportRuleConfigController {
    @Autowired
    private ImportRuleConfigService importRuleConfigService;


    /**
     * 配置 Import rule
     * @param config
     */
    @RequestMapping(value = "/saveImportRule")
    @ResponseBody
    public void saveImportRule(@RequestBody Map<String,Object> config){
        ImportRuleConfig importRuleConfig = JSONObject.parseObject(JSONObject.toJSONString(config), ImportRuleConfig.class);
        importRuleConfigService.saveImportRule(importRuleConfig);
    }

    @RequestMapping(value = "/deleteImportRule")
    @ResponseBody
    public void deleteImportRule(@RequestParam(value = "id") Integer id){
        importRuleConfigService.deleteImportRule(id);
    }

    @RequestMapping(value = "/updateImportRule")
    @ResponseBody
    public void updateImportRule(@RequestBody Map<String,Object> config){
        saveImportRule(config);
        deleteImportRule(Integer.parseInt(StringUtil.toString(config.get("id"))));
    }

    @RequestMapping(value = "/selectImportRule")
    @ResponseBody
    public List<ImportRuleConfig> selectImportRule(){
        List<ImportRuleConfig> maps = importRuleConfigService.selectImportRule();
        return maps;
    }

    @RequestMapping(value = "/selectImportRuleDetail")
    @ResponseBody
    public List<ImportRuleConfig> selectImportRuleDetail(@RequestParam(value = "id") Integer id){
        List<ImportRuleConfig> maps = importRuleConfigService.selectImportRuleDetail(id);
        return maps;
    }


}
