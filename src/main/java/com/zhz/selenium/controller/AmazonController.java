package com.zhz.selenium.controller;

import cn.hutool.core.date.DateUtil;
import com.zhz.selenium.mapper.AmazonMapper;
import com.zhz.selenium.pojo.KeyWord;
import com.zhz.selenium.pojo.MyAsin;
import com.zhz.selenium.service.AmazonService;
import com.zhz.selenium.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@RestController
@Component
@EnableScheduling
public class AmazonController {

    @Autowired
    private AmazonService amazonService;

    @Autowired
    private AmazonMapper amazonMapper;

    @Resource(name = "taskExecutor")
    private Executor taskExecutor;

    @RequestMapping(value = "/searchAmazon")
//    @Scheduled(cron="0 0,30 0 * * ?")
    public void searchAmazon() throws InterruptedException {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String startTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), -14));
//        String userName = "939677267@qq.com";
        String userName = "1975868046@qq.com";
//        String userName = "cai_jackiee@live.com";
        String passWord = "bbq135..";
//        String passWord = "STE178186lla";
        List<KeyWord> keyWordsAll = amazonMapper.queryKeyWord(startTime,date);
        List<KeyWord> keyWords = keyWordsAll.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(KeyWord::getKeyWord))), ArrayList::new));
        List<List<KeyWord>> lists = averageAssign(keyWords, 5);
       // CountDownLatch taskLatch = new CountDownLatch(lists.size());
        List<MyAsin> keyWordProducts = amazonMapper.queryMyAsin();
        for (List<KeyWord> list :lists) {
            Thread.sleep(60000);
            taskExecutor.execute(()->{
                try {
                    amazonService.getResultBySearch(date,list,userName,passWord,keyWordProducts);
                   // taskLatch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        //taskLatch.await();
       // System.out.println("------------------全部结束-------------");
    }

//    @RequestMapping(value = "/insertKeyWord")
//    @Scheduled(cron="0 0 0 * * ?")
//    public void insertKeyWord() {
//        String date = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -1));
//        String startTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -8));
//        amazonService.insertKeyWord(startTime,date);
//    }

    @RequestMapping(value = "/deleteSearchData")
    public void deleteSearchData(){
        List<Map<String, Object>> maps = amazonMapper.selectOverdueData();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -30));
        String startTime = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -120));
        for (Map<String, Object> map :maps) {
            try {
                if (!StringUtil.isEmpty(map)){
                    amazonMapper.deleteOverdueData(StringUtil.toString(map.get("id")),startTime,date,StringUtil.toString(map.get("lastsearchdate")).substring(0,10));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private List<List<KeyWord>> averageAssign(List<KeyWord> source, int n){
        List<List<KeyWord>> result = new ArrayList<>();
        int remainder = source.size() % n;  //余数
        int number = source.size() / n;  //商
        int offset = 0;//
        for (int i = 0; i < n; i++) {
            List<KeyWord> value = null;
            //当余数不为0，循环把余数循环加入到每个组中
            if (remainder > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remainder--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }


}
