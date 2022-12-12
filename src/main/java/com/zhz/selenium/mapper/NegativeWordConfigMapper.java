package com.zhz.selenium.mapper;

import com.zhz.selenium.pojo.NegativeWordConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NegativeWordConfigMapper {

    void saveNegativeWord(NegativeWordConfig negativeWordConfig);

    List<NegativeWordConfig> selectNegativeWord();

    void deleteNegativeWord(@Param(value = "id") Integer id);

    List<NegativeWordConfig> selectNegativeWordDetail(@Param(value = "id") Integer id);
}
