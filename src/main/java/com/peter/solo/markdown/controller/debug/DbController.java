package com.peter.solo.markdown.controller.debug;

import com.peter.solo.markdown.entity.User;
import com.peter.solo.markdown.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description TODO
 * @Author yandong.great
 * @Date 2020/8/26 12:02 上午
 */
@RestController
@RequestMapping("/debug/db")
public class DbController {
    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/insert")
    public String insert() {
        User user = new User();
        user.setAccountId("4");
        user.setName("yandong");
        user.setToken("siovaeiufu");
        userMapper.insert(user);
        return "OK";
    }

    @RequestMapping("/find/all")
    public List<User> findAll() {
        return userMapper.findAll();
    }
}
