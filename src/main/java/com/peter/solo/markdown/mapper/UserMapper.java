package com.peter.solo.markdown.mapper;

import com.peter.solo.markdown.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description TODO
 * @Author yandong.great
 * @Date 2020/8/25 11:58 下午
 */
@Mapper
public interface UserMapper {
    @Insert("INSERT INTO USER(account_id,name,token) VALUES " +
            "(#{accountId},#{name},#{token})")
    void  insert(User user);
}
