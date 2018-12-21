package com.weilong.webdemo.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.UUID;

@Controller
@Log4j2
public class UploadController {

    @RequestMapping("toUpload")
    public String toUpload(){
        return "upload";
    }

    //单文件上传
    @RequestMapping("/upload")
    public String uploadFile(MultipartFile file, HttpServletRequest request){
        try {
            //创建文件存放位置
//            String method = request.getMethod();
//            String dirGet = request.getServletContext().getRealPath("/upload");
            String dir = "E:/工作/学习/Spring-Boot/WebDemo/src/main/webapps/upload";
            File fileDir = new File(dir);
            if (!fileDir.exists()){
                fileDir.mkdirs();
            }

            //上传文件扩展名
            String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String fileName = UUID.randomUUID().toString() + fileSuffix;
            File files = new File(fileDir + "/" + fileName);

            //上传
            file.transferTo(files);
            log.info("上传成功");
        } catch (Exception e) {
            log.error("上传失败" + e);
        }
        return "success";
    }

    @RequestMapping("/toUploadMulti")
    public String toUploadMulti(){
        return "uploadMulti";
    }

    @RequestMapping("/uploadMulti")
    public String uploadMulti(List<MultipartFile> files, HttpServletRequest request){
        try {
            String dir = "E:/工作/学习/Spring-Boot/WebDemo/src/main/webapps/upload";
            File fileDir = new File(dir);
            if (!fileDir.exists()){
                fileDir.mkdirs();
            }
            for (MultipartFile file:files) {
                String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                String fileName = UUID.randomUUID().toString() + fileSuffix;
                File fileUp = new File(fileDir + "/" + fileName);
                file.transferTo(fileUp);
                log.info(file.getOriginalFilename() + "上传成功");
            }
        } catch (Exception e) {
            log.error("上传失败" + e);
        }

        return "success";
    }

}
