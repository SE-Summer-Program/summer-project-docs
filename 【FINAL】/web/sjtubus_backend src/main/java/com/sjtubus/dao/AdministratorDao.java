package com.sjtubus.dao;

import com.sjtubus.entity.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdministratorDao extends JpaRepository<Administrator,Integer> {
     @Query(value = "select administrator from Administrator administrator where administrator.username=:username")
     List<Administrator> searchByUsername(@Param("username") String username);

     Administrator findByUsername(String username);
}
