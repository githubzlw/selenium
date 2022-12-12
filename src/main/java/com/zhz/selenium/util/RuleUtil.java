package com.zhz.selenium.util;

import com.zhz.selenium.pojo.RuleConfigDetail;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class RuleUtil {
    public StringBuffer condition(RuleConfigDetail ruleConfigDetail) {
        StringBuffer stringBuffer = new StringBuffer();
        dealCondition(stringBuffer,ruleConfigDetail.getImpressionMin(),ruleConfigDetail.getImpressionMax(),"impression");
        dealCondition(stringBuffer,ruleConfigDetail.getClicksMin(),ruleConfigDetail.getClicksMax(),"clicks");
        dealCondition(stringBuffer,ruleConfigDetail.getOrdersMin(),ruleConfigDetail.getOrdersMax(),"orders");
        dealCondition(stringBuffer,ruleConfigDetail.getUnitsMin(),ruleConfigDetail.getUnitsMax(),"units");
        dealCondition(stringBuffer,ruleConfigDetail.getCpcMin(),ruleConfigDetail.getCpcMax(),"cpc");
        dealCondition(stringBuffer,ruleConfigDetail.getCtrMin(),ruleConfigDetail.getCtrMax(),"ctr");
        dealCondition(stringBuffer,ruleConfigDetail.getConversionMin(),ruleConfigDetail.getConversionMax(),"conversion");
        dealCondition(stringBuffer,ruleConfigDetail.getAcosMin(),ruleConfigDetail.getAcosMax(),"acos");
        dealCondition(stringBuffer,ruleConfigDetail.getRoasMin(),ruleConfigDetail.getAcosMax(),"roas");
        dealCondition(stringBuffer,ruleConfigDetail.getSpendMin(),ruleConfigDetail.getSpendMax(),"spend");
        dealCondition(stringBuffer,ruleConfigDetail.getSalesMin(),ruleConfigDetail.getSalesMax(),"sales");
        return stringBuffer;
    }

    public StringBuffer viewData(RuleConfigDetail ruleConfigDetail) {
        StringBuffer stringBuffer = new StringBuffer();
        dealConditionData(stringBuffer,ruleConfigDetail.getImpressionMin(),ruleConfigDetail.getImpressionMax(),"Impressions");
        dealConditionData(stringBuffer,ruleConfigDetail.getClicksMin(),ruleConfigDetail.getClicksMax(),"Clicks");
        dealConditionData(stringBuffer,ruleConfigDetail.getOrdersMin(),ruleConfigDetail.getOrdersMax(),"Day7_TotalOrders");
        dealConditionData(stringBuffer,ruleConfigDetail.getUnitsMin(),ruleConfigDetail.getUnitsMax(),"Day7_TotalUnits");
        dealConditionData(stringBuffer,ruleConfigDetail.getSpendMin(),ruleConfigDetail.getSpendMax(),"Spend");
        dealConditionData(stringBuffer,ruleConfigDetail.getSalesMin(),ruleConfigDetail.getSalesMax(),"Day7_TotalSales");
        return stringBuffer;
    }

    public void dealConditionData(StringBuffer stringBuffer, BigDecimal min, BigDecimal max, String field){
        if (!StringUtil.isEmpty(min) && !StringUtil.isEmpty(max) && min.compareTo(max)==0){
            stringBuffer.append(" and ").append(field).append("=").append(max.stripTrailingZeros().toPlainString());
        }else if (!StringUtil.isEmpty(min) && !StringUtil.isEmpty(max) &&
                min.compareTo(BigDecimal.ZERO)>=0 && max.compareTo(BigDecimal.ZERO)>=0){
            stringBuffer.append(" and ").append(max.stripTrailingZeros().toPlainString()).append(">=").append(field).append(" and ")
                    .append(field).append(">=").append(min.stripTrailingZeros().toPlainString());
        }else if(!StringUtil.isEmpty(min) && min.compareTo(BigDecimal.ZERO)>=0){
            stringBuffer.append(" and ").append(field).append(">=").append(min.stripTrailingZeros().toPlainString());
        }else if(!StringUtil.isEmpty(max) && max.compareTo(BigDecimal.ZERO)>=0){
            stringBuffer.append(" and ").append(field).append("<=").append(max.stripTrailingZeros().toPlainString());
        }
    }

    private void dealCondition(StringBuffer stringBuffer, BigDecimal min, BigDecimal max, String field){
        if (!StringUtil.isEmpty(min) && !StringUtil.isEmpty(max) && min.compareTo(max)==0){
            stringBuffer.append(",").append(field).append("=").append(max.stripTrailingZeros().toPlainString());
        }else if (!StringUtil.isEmpty(min) && !StringUtil.isEmpty(max) &&
                min.compareTo(BigDecimal.ZERO)>=0 && max.compareTo(BigDecimal.ZERO)>=0){
            stringBuffer.append(",").append(max.stripTrailingZeros().toPlainString()).append(">=").append(field)
                    .append(">=").append(min.stripTrailingZeros().toPlainString());
        }else if(!StringUtil.isEmpty(min) && min.compareTo(BigDecimal.ZERO)>=0){
            stringBuffer.append(",").append(field).append(">=").append(min.stripTrailingZeros().toPlainString());
        }else if(!StringUtil.isEmpty(max) && max.compareTo(BigDecimal.ZERO)>=0){
            stringBuffer.append(",").append(field).append("<=").append(max.stripTrailingZeros().toPlainString());
        }
    }



}
