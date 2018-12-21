package com.weilong.webdemo.controller;

import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hwl
 */
@Controller
@Log4j2
public class JsoupController {
    @RequestMapping("/crawl")
    @ResponseBody
    /**
     * 爬取千库网图片
     */
    public List<String> crawl(){
        String path= "http://588ku.com/tuku/xingqiu.html";
        String html = path.split("/")[4];
        String dirName = StringUtils.split(html,".")[0];
        String code = null;
        code = getCodeByPath(path);
        List<String> list = new ArrayList<>();

        list = getList(code);

        for (String src: list) {
            save(src, dirName);
        }

        return list;
    }

    /**
     * 通过路径获取源代码
     * @param path
     * @return
     */
    private String getCodeByPath(String path){
        String code = null;
        try {
            code = Jsoup.connect(path).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return code;
    }

    /**
     * 图片保存
     * @param src
     */
    private void save(String src, String dirName){
        int byteRead = 0;
        int byteNum = 0;
        try {
            src = "http:" + src;
            URL url = new URL(src);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            byte[] buffer = new byte[1024];
            String dir = "E:/工作/学习/Spring-boot/WebDemo/src/main/webapps/download/img/" + dirName;
            File fileDir = new File(dir);
            if (!fileDir.exists()){
                fileDir.mkdirs();
            }
            File file = new File(dir + "/" + System.currentTimeMillis() + ".jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            while ((byteRead = inputStream.read(buffer)) != -1){
                byteNum += byteRead;
                outputStream.write(buffer,0, byteRead);
            }
        } catch (FileNotFoundException e) {
            log.error(e);
        }catch (MalformedURLException e){
            log.error(e);
        } catch (IOException e) {
            log.error(e);
        }
    }

    /**
     * 获取所有的符合列表信息
     * @param code
     * @return
     */
    private List<String> getList(String code){
        List<String> list = new ArrayList<>();
        Document document = Jsoup.parse(code);

        Elements elements = document.getElementsByClass("lazy");

        for (Element element :elements){
            list.add(element.attr("data-original"));
        }
        return list;
    }
}
