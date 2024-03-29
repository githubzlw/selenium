package com.zhz.selenium.pojo;

import lombok.Data;

@Data
public class ApiResult {
    private String amzAsin;
    private Integer normalCount;
    private Integer notNormalCount;

    // 自然排名中间结果
    private String zrPm;
    // 广告排名中间结果
    private String ggPm;
//    //Session数(自然流量 sp api)
//    private Integer asinSession;
    //总订单数(sp api)
    private Integer unitOrder;
    //总广告点击数 (该ASIN所在Campaign的)
    private Integer clicks;
//    //广告花费
//    private String spend;
    //广告订单数
    private Integer day7TotalOrders;
    //搜索词
    private String customerSearchTerm;
    //cpc
    private String cpc;
    //曝光量
    private Integer impressions;
    //标题
    private String title;
    //主图
    private String img;
    //标题是否含Search Term
    private String bhFlag;
    //开始时间
    private String startDate;
    //结束时间
    private String newDate;
    //开始时间-结束时间
    private String seDate;
    //spApi最新更新时间
    private String spDate;

    public ApiResult(String amzAsin, String zrPm, String ggPm) {
        this.amzAsin=amzAsin;
        this.zrPm=zrPm;
        this.ggPm=ggPm;
    }
}
