package com.peter.solo.markdown.controller.debug;

import com.peter.solo.markdown.entity.Person;
import com.peter.solo.markdown.mapper.PersonMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description TODO
 * @Author yandong.great
 * @Date 2020/8/26 12:02 上午
 */
@Slf4j
@RestController
@RequestMapping("/debug/db")
public class DbController {
    @Autowired
    private PersonMapper personMapper;

    @PostMapping("/insert")
    public Integer insert(@RequestBody Person person) {
        if(null == person) {
            log.warn("person对象为空");
            return -1;
        }

        if(StringUtils.isEmpty(person.getName())) {
            log.warn("person.name为空");
            return -1;
        }

        if(StringUtils.isEmpty(person.getAlias())) {
            log.warn("person.alias为空");
            return -1;
        }

        return personMapper.insert(person);
    }

    @RequestMapping("/find/all")
    public List<Person> findAll() {
        return personMapper.findAll();
    }

    @RequestMapping("/find/one")
    public Person findOne(@RequestParam(name = "name") String name) {
        return personMapper.findOneByName(name);
    }

    @RequestMapping("/find/multi")
    public List<Person> findMulti(@RequestParam(name = "name") String name) {
        return personMapper.findByName(name);
    }
}
