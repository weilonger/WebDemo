package com.weilong.webdemo.entities;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TBookComment implements Serializable {
    private Long id;

    private Long bookId;

    private Long customerId;

    private String customerName;

    private String customerImage;

    private String content;

    private Byte star;

    private Byte status;

    private Byte source;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}