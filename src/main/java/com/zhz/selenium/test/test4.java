package com.zhz.selenium.test;

import lombok.Data;
import org.assertj.core.util.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class test4 {
    public static void main(String[] args) {
        ArrayList<DemoPrice> demoPrices =
                Lists.newArrayList(new DemoPrice("A1", BigDecimal.valueOf(10.0), BigDecimal.valueOf(11.0)),
                        new DemoPrice("A1,A2,A3", BigDecimal.valueOf(10.0), BigDecimal.valueOf(11.0)),
                        new DemoPrice("A1,A2", BigDecimal.valueOf(10.0), BigDecimal.valueOf(11.0)),
                        new DemoPrice("A2", BigDecimal.valueOf(10.0), BigDecimal.valueOf(11.0)),
                        new DemoPrice("A4", BigDecimal.valueOf(10.0), BigDecimal.valueOf(11.0))
                );

        Map<String, Set<String>> relationMap = new HashMap();
        Map<String,List<DemoPrice>> dataMap = new HashMap();

        demoPrices.forEach(item -> {

            String itemIdNo = item.getIdNo();

            String[] idNoArray = itemIdNo.split(",");

            for (String s : idNoArray) {
                for (String idNo : idNoArray) {
                    Set<String> set = relationMap.computeIfAbsent(idNo, k -> Sets.newHashSet());
                    set.add(s);
                    List<DemoPrice> list = dataMap.computeIfAbsent(idNo, k -> Lists.newArrayList());
                    list.add(item);
                }
            }

        });


        List<DemoPrice> collect2 = demoPrices.stream().map(item -> {
            Set<DemoPrice> collect = Arrays.stream(item.getIdNo().split(",")).map(idNo -> relationMap.getOrDefault(idNo,Collections.emptySet())).flatMap(Collection::stream).collect(Collectors.toSet()).stream().map(idNO -> dataMap.getOrDefault(idNO, Collections.emptyList())).flatMap(Collection::stream).collect(Collectors.toSet());
            return new DemoPrice(item.getIdNo(), collect.stream().map(DemoPrice::getPrice).reduce(BigDecimal::add).orElse(BigDecimal.ZERO), collect.stream().map(DemoPrice::getPrice2).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
        }).collect(Collectors.toList());

        collect2.forEach(System.out::println);
    }


    @Data
    static class DemoPrice{
        private String idNo;
        private BigDecimal price;
        private BigDecimal price2;
        public DemoPrice(String idNo,BigDecimal price,BigDecimal price2){
            this.idNo = idNo;
            this.price = price;
            this.price2 = price2;
        }

    }
}
