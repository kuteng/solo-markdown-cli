package com.peter.solo.markdown.mapper;

import com.peter.solo.markdown.entity.Person;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Description 对user表进行查询。
 * @Author yandong.great
 * @Date 2020/8/25 11:58 下午
 */
@Mapper
public interface PersonMapper {
    @Insert("INSERT INTO persons(name, alias, tags) VALUES " +
            "(#{name}, #{alias}, #{tags})")
    int insert(Person person);

    @Select("select id, name, alias, tags from persons")
    List<Person> findAll();

    @Select("select id, name, alias, tags from persons where name = #{name}")
    List<Person> findByName(@Param("name") String name);

    @Select("select id, name, alias, tags from persons where name = #{name} limit 1")
    Person findOneByName(@Param("name") String name);

    @Update("update persons set name=#{name}, alias=#{alias}, tags=#{tags} where id=#{id}")
    int update(Person person);
}
