package com.zhz.selenium.pojo;

import lombok.Data;

@Data
public class ApiChildResult {

    // 子ASIN
    private String childAsin;
    //Session数(自然流量 sp api)
    private Integer childAsinSession;
    //总订单数(sp api)
    private Integer childUnitOrder;
    //总广告点击数 (该ASIN所在Campaign的)
    private Integer childClicks;
    //广告花费
    private String childSpend;
    //广告订单数
    private Integer childDay7TotalOrders;
    //自然曝光
    private Integer childImpressions;
    //总Unit Sell 个数
    private Integer childDay7OtherSKUUnits;
    //广告Unit Sell -Advertised ASINsum
    private Integer childDay7AdvertisedSKUUnits;
    //广告Unit Sell -Other ASINsum
    private Integer childDay7TotalUnits;

    public ApiChildResult(){

    }
}
