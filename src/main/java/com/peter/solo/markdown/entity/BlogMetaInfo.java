package com.peter.solo.markdown.entity;

import lombok.Data;

import java.util.List;

@Data
public class BlogMetaInfo {
    private String id;
    private String date;
    private String title;
    private List<String> tags;
    private String password;
    /**
     * 该文件是否是一个blog。
     */
    private boolean blog;

    public BlogMetaInfo() {
        blog = true;
    }
}
