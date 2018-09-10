package com.sjtubus.dao;


import com.sjtubus.entity.Driver;
import com.sjtubus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import javax.transaction.Transactional;
import java.util.List;

import java.util.Optional;

public interface UserDao extends JpaRepository<User,Integer> {

    User findByUsername(String username);

    User findByPhone(String phone);

    User findByUserId(int userId);

    @Transactional
    @Modifying
    @Query("update User user set user.username=:username, user.phone=:phone, user.credit=:credit where user.userId =:userId")
    int modifyUser(@Param("userId") int UserId, @Param("username") String username, @Param("phone") String phone, @Param("credit") int credit);

    @Query(value = "select user from User user where user.username like %:content% or user.phone like %:content% or user.realname like %:content% or user.studentNumber like %:content%")
    List<User> queryByRelatedContent(@Param("content") String content);
}
