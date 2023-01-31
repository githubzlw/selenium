package com.zhz.selenium.util;

//import cn.hutool.core.io.resource.ClassPathResource;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.zhz.selenium.pojo.ApiResult;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author zlw
 */
public class ExcelUtil {
    /**
     * EasyExcel正常数据填充
     * @param response 返回对象
     * @param list excel表中记录
     */
    public static void writeExcel(HttpServletResponse response, List<ApiResult> list) throws IOException {

        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();
        //定义工作表对象
        WriteSheet sheet = EasyExcel.writerSheet(0,"sheet").head(ApiResult.class).build();
        //往excel文件中写入数据
        excelWriter.write(list,sheet);
        //关闭输出流
        excelWriter.finish();
    }

    /**
     * EasyExcel模板数据填充
     * @param response 返回对象
     * @param list excel表中记录多条
     * @param apiResult excel表中记录单条
     * @throws IOException
     */
    public static void fillExcel(HttpServletResponse response,List<ApiResult> list,ApiResult apiResult,Integer date,String asin) throws IOException {
        ServletOutputStream out = response.getOutputStream();
        response.setContentType("multipart/form-data");
        response.setCharacterEncoding("UTF-8");
        //文件名字
        String fileName = asin+"_"+date+".xlsx";
        response.setHeader("Content-disposition", "attachment;filename=" +fileName);
        //文件模板输入流
        InputStream inputStream = new ClassPathResource("templates/demo.xlsx").getInputStream();

        ExcelWriter writer = EasyExcel.write(out).withTemplate(inputStream).build();
        WriteSheet sheet = EasyExcel.writerSheet(0).build();
        //填充列表开启自动换行,自动换行表示每次写入一条list数据是都会重新生成一行空行,此选项默认是关闭的,需要提前设置为true
        FillConfig fillConfig = FillConfig.builder().forceNewRow(true).build();
        //填充单一
        writer.fill(apiResult,sheet);
        //填充数据多条
        writer.fill(list,fillConfig,sheet);
        //填充完成
        writer.finish();
        out.flush();

    }

}
