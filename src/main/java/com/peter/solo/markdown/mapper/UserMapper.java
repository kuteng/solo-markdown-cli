package com.peter.solo.markdown.mapper;

import com.peter.solo.markdown.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description 对user表进行查询。
 * @Author yandong.great
 * @Date 2020/8/25 11:58 下午
 */
@Mapper
public interface UserMapper {
    @Insert("INSERT INTO myuser(account_id,name,token) VALUES " +
            "(#{accountId},#{name},#{token})")
    void  insert(User user);

    @Select("select id, account_id, name, token from myuser")
    List<User> findAll();
}
