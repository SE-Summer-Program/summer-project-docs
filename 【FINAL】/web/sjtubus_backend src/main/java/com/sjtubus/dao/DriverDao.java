package com.sjtubus.dao;

import com.sjtubus.entity.Appointment;
import com.sjtubus.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface DriverDao extends JpaRepository<Driver,Integer> {

    Driver findByDriverId(int driverId);

    @Query(value = "select driver from Driver driver where driver.username like %:content% or driver.phone like %:content%")
    List<Driver> queryByRelatedContent(@Param("content") String content);

    Driver queryDriverByUsername(String username);

    Driver queryDriverByDriverId(int userId);

    @Transactional
    @Modifying
    @Query("update Driver driver set driver.phone=:phone, driver.username=:username where driver.driverId =:driverId")
    int modifyDriver(@Param("driverId") int driverId, @Param("username") String username, @Param("phone") String phone);

    Driver findByUsername(String username);
}
