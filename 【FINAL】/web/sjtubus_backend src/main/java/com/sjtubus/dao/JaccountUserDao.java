package com.sjtubus.dao;

import com.sjtubus.entity.JaccountUser;
import com.sjtubus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JaccountUserDao extends JpaRepository<JaccountUser,Integer> {

    JaccountUser findByUsername(String username);

    JaccountUser findByPhone(String phone);
}
