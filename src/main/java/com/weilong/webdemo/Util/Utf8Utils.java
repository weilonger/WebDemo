package com.weilong.webdemo.Util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

//
public class Utf8Utils {

    public static void main(String[] args) throws IOException {
        File file = new File("E:\\工作\\学习\\Spring-Boot\\WebDemo\\src\\main\\resources\\application.properties");
        File targetFile = new File("E:\\工作\\学习\\Spring-Boot\\WebDemo\\src\\main\\resources\\application.properties");
        if (!targetFile.exists()) {
            targetFile.createNewFile();
        }
        BufferedReader br = null;
        BufferedWriter bw = null;
        br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile), "UTF-8"));
        int i = 0;
        String str = "";
        while ((str = br.readLine()) != null) {
            if (i == 0) {// 读取第一行，将前三个字节去掉，重新new个String对象
                byte[] bytes = str.getBytes("UTF-8");
                str = new String(bytes, 3, bytes.length - 3);
                bw.write(str);
                i++;
            } else {
                bw.write(str);

            }
        }
        br.close();
        bw.close();
    }
}
