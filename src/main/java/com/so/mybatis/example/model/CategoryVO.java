package com.so.mybatis.example.model;

import java.util.Date;

import lombok.Data;

@Data
public class CategoryVO {

    private Long categoryId;
    private String categoryName;
    private String categoryImgPath;
    private Date createdAt;
    private Date updatedAt;
    private Long createdUser;
    private Long updatedUser;
    private int postCnt;
}
