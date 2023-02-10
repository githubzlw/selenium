package com.zhz.selenium.pojo;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.BooleanEnum;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import lombok.Data;


@Data
public class ImageExcel {

    @ExcelProperty(value = "单元格标题", index=3)
    @ColumnWidth(70)//单元格宽度为70
//    @ContentRowHeight(100)//高度为100
    @ContentStyle(
            horizontalAlignment = HorizontalAlignmentEnum.LEFT, //水平居左
            verticalAlignment = VerticalAlignmentEnum.CENTER, //垂直居中
            wrapped = BooleanEnum.TRUE)//主要是为了导出时直接换行
    private WriteCellData<Void> writeCellData;
}