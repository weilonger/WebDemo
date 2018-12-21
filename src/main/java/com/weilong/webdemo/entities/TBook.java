package com.weilong.webdemo.entities;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class TBook implements Serializable {
    private Long id;

    private Long categoryId;

    private String title;

    private String subtitle;

    private String customizeTitle;

    private String isbn;

    private String image;

    private String largeImage;

    private String author;

    private String translator;

    private String summary;

    private String catalog;

    private BigDecimal doubanRating;

    private String publisher;

    private String pubdate;

    private Integer pages;

    private String binding;

    private BigDecimal price;

    private String altUrl;

    private Long creatorId;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}