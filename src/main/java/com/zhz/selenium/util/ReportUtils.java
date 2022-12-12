package com.zhz.selenium.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.net.URL;

@Component
public class ReportUtils {
    public String download(String headPortrait,String fileName){
        System.out.println(headPortrait);
        System.out.println(fileName);
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
            return path.getAbsolutePath()+ "/upload/" + fileName;
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

    public StringBuilder unGzip(String path) throws InterruptedException {
        System.out.println(path);
        Thread.sleep(60000);
        StringBuilder stringBuilder = new StringBuilder();
        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            byte[] bytes = ZipUtil.unGzip(FileUtil.getInputStream(path));
            FileUtil.writeBytes(bytes, path);
            fileInputStream = new FileInputStream(path);
            inputStreamReader = new InputStreamReader(fileInputStream, "utf-8");
            bufferedReader = new BufferedReader(inputStreamReader);
            String tempString;
            while ((tempString = bufferedReader.readLine()) != null) {// 直接返回读取的字符串
                // 将字符串 添加到 stringBuilder中
                stringBuilder.append(tempString);
            }
            fileInputStream.close();
            inputStreamReader.close();
            bufferedReader.close();
            return stringBuilder;
        } catch (IOException e) {
            e.printStackTrace();
            try {
                fileInputStream.close();
                inputStreamReader.close();
                bufferedReader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
}
