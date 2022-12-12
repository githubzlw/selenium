package com.zhz.selenium.test;

import cn.hutool.Hutool;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.extra.compress.CompressUtil;
import cn.hutool.extra.compress.extractor.Extractor;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.zhz.selenium.util.ReportUtils;
import com.zhz.selenium.util.StringUtil;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.net.URL;
import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class test3 {

    public static void main(String[] args) throws Exception {
//////        String filePath = download(url);
////        String path = "F:\\IdeaCode\\selenium\\target\\classes\\upload\\2e2082be-2611-4a00-a5aa-6d424efcbe9d.json.gz";
////        byte[] bytes = ZipUtil.unGzip(FileUtil.getInputStream(path));
////        FileUtil.writeBytes(bytes,"F:\\IdeaCode\\selenium\\target\\classes\\upload\\2e2082be-2611-4a00-a5aa-6d424efcbe9d.json");
////
////        StringBuilder stringBuilder = new StringBuilder();
////        FileInputStream fileInputStream = new FileInputStream("F:\\IdeaCode\\selenium\\target\\classes\\upload\\2e2082be-2611-4a00-a5aa-6d424efcbe9d.json");
////        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "utf-8");
////        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
////        String tempString;
////        while ((tempString = bufferedReader.readLine()) != null) {// 直接返回读取的字符串
////            // 将字符串 添加到 stringBuilder中
////            stringBuilder.append(tempString);
////        }
////        bufferedReader.close();
////        System.out.println(stringBuilder);
//
//        StringBuilder s = new StringBuilder();
//        List<Map<String,Object>> list = JSONObject.parseObject(s.toString(),List.class);
//        for (Map<String,Object> m:list) {
//            System.out.println(m.get("adGroupName"));
//        }
//        Double aDouble1 = Double.valueOf(StringUtil.toString(0));
//        Double aDouble = Double.valueOf("1");
//        System.out.println(aDouble/aDouble1);

    }

    public static String download(String headPortrait) {
        String fileName = UUID.randomUUID().toString() + ".json.gz";
        URL url = null;
        DataInputStream dataInputStream = null;
        FileOutputStream fileOutputStream = null;
        File localFile = null;
        try {
            //根据url下载图片到本地
            //getPath()得到的是构造file的时候的路径。 getAbsolutePath()得到的是全路径
            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            if (!path.exists()) {
                path = new File("");
            }
            File dir = new File(path.getAbsolutePath(), "upload/");
            if (!dir.exists()) {
                dir.mkdir();
            }
            localFile = new File(path.getAbsolutePath(), "upload/" + fileName);
            String localFilePath = localFile.getPath();
            url = new URL(headPortrait);
            dataInputStream = new DataInputStream(url.openStream());
            fileOutputStream = new FileOutputStream(localFilePath);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 10];
            int length;
            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
            return path.getAbsolutePath()+ "upload/" + fileName;
        } catch (Exception e) {
            try {
                if (dataInputStream != null) {
                    dataInputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                return null;
            }
            localFile.delete();
            return null;
        }

    }
}