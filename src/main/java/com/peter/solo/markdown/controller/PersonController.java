package com.peter.solo.markdown.controller;

import com.alibaba.fastjson.JSONObject;
import com.peter.solo.markdown.entity.Person;
import com.peter.solo.markdown.manager.PersonManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description 获取用户列表
 * @Author yandong.great
 * @Date 2020/8/28 3:57 下午
 */

@Slf4j
@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private PersonManager personManager;

    @RequestMapping("/list")
    public String list(@RequestParam(name="format", defaultValue = "json") String format) {
        List<Person> persons = personManager.getPersons();

        if("str".equals(format) || "string".equals(format)) {
            StringBuilder builder = new StringBuilder();

            for(Person person: persons) {
                builder.append("| ").append(person.getName()).append("  ->  ").append(person.getAlias()).append("\n");
            }

            return builder.toString();
        }

        return JSONObject.toJSONString(persons);
    }

    @RequestMapping("/add")
    public int add(@RequestParam(name="name", required = true) String name,
                       @RequestParam(name="alias", required = true) String alias,
                       @RequestParam(name="tags", required = false, defaultValue = "") String tags)
    {
        Person person = new Person();
        person.setName(name);
        person.setAlias(alias);
        person.setTags(tags);
        return personManager.add(person);
    }
}
