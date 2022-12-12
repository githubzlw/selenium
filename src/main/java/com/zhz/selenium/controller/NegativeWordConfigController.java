package com.zhz.selenium.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhz.selenium.pojo.NegativeWordConfig;
import com.zhz.selenium.service.NegativeWordConfigService;
import com.zhz.selenium.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin(origins = "*")
public class NegativeWordConfigController {
    @Autowired
    private NegativeWordConfigService negativeWordConfigService;


    /**
     * 配置 Negative Word
     * @param config
     */
    @RequestMapping(value = "/saveNegativeWord")
    @ResponseBody
    public void saveNegativeWord(@RequestBody Map<String,Object> config){
        NegativeWordConfig negativeWordConfig = JSONObject.parseObject(JSONObject.toJSONString(config), NegativeWordConfig.class);
        negativeWordConfigService.saveNegativeWord(negativeWordConfig);
    }

    @RequestMapping(value = "/deleteNegativeWord")
    @ResponseBody
    public void deleteNegativeWord(@RequestParam(value = "id") Integer id){
        negativeWordConfigService.deleteNegativeWord(id);
    }

    @RequestMapping(value = "/updateNegativeWord")
    @ResponseBody
    public void updateNegativeWord(@RequestBody Map<String,Object> config){
        saveNegativeWord(config);
        deleteNegativeWord(Integer.parseInt(StringUtil.toString(config.get("id"))));
    }

    @RequestMapping(value = "/selectNegativeWord")
    @ResponseBody
    public List<NegativeWordConfig> selectNegativeWord(){
        List<NegativeWordConfig> maps = negativeWordConfigService.selectNegativeWord();
        return maps;
    }

    @RequestMapping(value = "/selectNegativeWordDetail")
    @ResponseBody
    public List<NegativeWordConfig> selectNegativeWordDetail(@RequestParam(value = "id") Integer id){
        List<NegativeWordConfig> maps = negativeWordConfigService.selectNegativeWordDetail(id);
        return maps;
    }


}
