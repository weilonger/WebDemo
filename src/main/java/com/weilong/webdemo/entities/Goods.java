package com.weilong.webdemo.entities;

import lombok.Data;

import java.util.List;

@Data
public class Goods {
    private String id;
    private String img;
    private List<String> imgs;
    private String url;
    private String name;
    private String price;
    private String bookStore;
}
