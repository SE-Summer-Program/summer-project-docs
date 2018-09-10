package com.sjtubus.dao;

import com.sjtubus.entity.RideBusInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface RideBusInfoDao extends JpaRepository<RideBusInfo,Integer> {

    @Query(value = "select rideBusInfo from RideBusInfo rideBusInfo where rideBusInfo.shiftId like %:shiftId% and rideBusInfo.rideDate between :startDate and :endDate order by rideBusInfo.rideDate asc ")
    List<RideBusInfo> queryRideBusInfo(@Param("shiftId")String shiftId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
