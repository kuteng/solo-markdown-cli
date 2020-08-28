package com.peter.solo.markdown.manager;

import com.peter.solo.markdown.entity.Person;
import com.peter.solo.markdown.mapper.PersonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description 管理人名。
 * @Author yandong.great
 * @Date 2020/8/27 12:26 上午
 */
@Slf4j
@Component
public class PersonManager {
    @Autowired
    private PersonMapper personMapper;

    private List<Person> persons;
    private boolean needRefresh;

    public List<Person> getPersons() {
        if (needRefresh || null == persons) {
            persons = personMapper.findAll();
        }

        return persons;
    }

    public int add(Person person) {
        needRefresh = true;
        return personMapper.insert(person);
    }
}
