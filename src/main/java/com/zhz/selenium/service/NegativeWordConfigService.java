package com.zhz.selenium.service;


import com.zhz.selenium.pojo.NegativeWordConfig;

import java.util.List;

public interface NegativeWordConfigService {
    void saveNegativeWord(NegativeWordConfig negativeWordConfig);

    List<NegativeWordConfig> selectNegativeWord();

    void deleteNegativeWord(Integer id);

    void executeNegativeWord(String rulePage,Object value);

    List<NegativeWordConfig> selectNegativeWordDetail(Integer id);
}
