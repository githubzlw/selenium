package com.zhz.selenium.service.impl;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.zhz.selenium.mapper.NegativeWordConfigMapper;
import com.zhz.selenium.pojo.BiddingRuleConfig;
import com.zhz.selenium.pojo.NegativeWordConfig;
import com.zhz.selenium.service.NegativeWordConfigService;
import com.zhz.selenium.service.RuleConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NegativeWordConfigServiceImpl implements NegativeWordConfigService {

    Log log = LogFactory.get();

    @Autowired
    private NegativeWordConfigMapper negativeWordConfigMapper;

    @Autowired
    private RuleConfigService ruleConfigService;

    @Override
    public void saveNegativeWord(NegativeWordConfig negativeWordConfig) {
        try {
            negativeWordConfigMapper.saveNegativeWord(negativeWordConfig);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @Override
    public List<NegativeWordConfig> selectNegativeWord() {
        try {
            List<NegativeWordConfig> negativeWordConfigs = negativeWordConfigMapper.selectNegativeWord();
            return negativeWordConfigs;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    @Override
    public void deleteNegativeWord(Integer id) {
        try {
            negativeWordConfigMapper.deleteNegativeWord(id);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @Override
    public List<NegativeWordConfig> selectNegativeWordDetail(Integer id) {
        List<NegativeWordConfig> negativeWordConfigs = negativeWordConfigMapper.selectNegativeWordDetail(id);
        return negativeWordConfigs;
    }

    @Override
    public void executeNegativeWord(String rulePage, Object value) {
        List<NegativeWordConfig> list = (List<NegativeWordConfig>) value;
        for (NegativeWordConfig negativeWordConfig:list) {
//            List<Map<String, Object>> maps = ruleConfigService.selectConfigDetailData(rulePage, negativeWordConfig.getId());
            // 操作
        }
    }
}
