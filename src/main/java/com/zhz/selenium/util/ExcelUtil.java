package com.zhz.selenium.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.data.ImageData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.zhz.selenium.pojo.ApiChildResult;
import com.zhz.selenium.pojo.ApiOther;
import com.zhz.selenium.pojo.ApiResult;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static void fillExcel(HttpServletResponse response,List<ApiResult> list,ApiResult apiResult,Integer date,
                                 String asin,List<ApiChildResult> childList,List<ApiOther> otLists ) throws IOException {
        ServletOutputStream out = response.getOutputStream();
        response.setContentType("multipart/form-data");
        response.setCharacterEncoding("UTF-8");
        //文件名字
        String fileName = asin+"-"+date+".xlsx";
        response.setHeader("Content-disposition", "attachment;filename=" +fileName);
        //文件模板输入流
        InputStream inputStream = new ClassPathResource("templates/demo.xlsx").getInputStream();

        ExcelWriter writer = EasyExcel.write(out).withTemplate(inputStream).build();
        WriteSheet sheet = EasyExcel.writerSheet(0).build();
        //填充列表开启自动换行,自动换行表示每次写入一条list数据是都会重新生成一行空行,此选项默认是关闭的,需要提前设置为true
        FillConfig fillConfig = FillConfig.builder().forceNewRow(true).build();


        Map<String, Object> map = new HashMap<>();
        WriteCellData writeCellData =ExcelUtil.handleImage(apiResult.getImg());
        map.put("image", writeCellData);
        //填充图片
        writer.fill(map, sheet);

        //填充单一
        writer.fill(apiResult,sheet);
        //填充数据多条
        writer.fill(list,fillConfig,sheet);
        //填充数据多条
        writer.fill(childList,fillConfig,sheet);
        //填充数据多条
        writer.fill(otLists,fillConfig,sheet);
        //填充完成
        writer.finish();
        out.flush();

    }
    
    /**
     * 图片填充方法
     * @param imagePath
     * @return WriteCellData
     * @author zlw
     * @since 2023/2/17 15:46
     */
    public static WriteCellData handleImage(String imagePath) {
        WriteCellData<Void> writeCellData = new WriteCellData<>();

        try {
            // 定义图片数据
            ImageData imageData = new ImageData();

            //将图片的https地址转为数组
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            URL url = new URL(imagePath);
            byte[] bytes = new byte[1024];
            InputStream inputStream = url.openStream();
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            inputStream.close();
//            将数组放入
            imageData.setImage(outputStream.toByteArray());

//            imageData.setImageType(ImageData.ImageType.PICTURE_TYPE_PNG);//设置图片后缀类型

            // 上 右 下 左 需要留空，这个类似于 css 的 margin；这里实测 不能设置太大 超过单元格原始大小后 打开会提示修复。暂时未找到很好的解法。
            imageData.setTop(10);
            imageData.setRight(10);
            imageData.setBottom(10);
            imageData.setLeft(10);

            // * 设置图片的位置。Relative表示相对于当前的单元格index。first是左上点，last是对角线的右下点，这样确定一个图片的位置和大小。
            // 目前填充模板的图片变量是images，index：row=7,column=0。所有图片都基于此位置来设置相对位置
            // 第1张图片相对位置
            imageData.setRelativeFirstRowIndex(0);
            imageData.setRelativeFirstColumnIndex(0);
            imageData.setRelativeLastRowIndex(15);
            imageData.setRelativeLastColumnIndex(2);

            // 最后设置图片数据
            writeCellData.setImageDataList(Arrays.asList(imageData));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writeCellData;
    }



}
