package com.peter.solo.markdown.entity;

import lombok.Data;

/**
 * @Description 用户
 * @Author yandong.great
 * @Date 2020/8/26 12:00 上午
 */
@Data
public class User {
    private String accountId;
    private String name;
    private String token;
}
