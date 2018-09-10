package com.sjtubus.dao;

import com.sjtubus.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.sql.Time;
import java.util.Collection;
import java.util.List;

public interface ShiftDao extends JpaRepository<Shift,String> {
    List<Shift> findByLineTypeAndLineNameOrderByDepartureTime(String type,String line_name);

    Shift findByShiftId(String shiftId);

    @Query(value = "select shift from Shift shift where shift.lineName=:lineName and shift.lineType=:lineType")
    List<Shift> queryByLinetypeAndLinename(@Param("lineType")String lineType,@Param("lineName")String lineName);

    @Query(value = "select shift from Shift shift where shift.lineName=:lineName and shift.lineType=:lineType and shift.departureTime=:departureTime")
    List<Shift> queryByLinetypeAndLinenameAndDepartureTime(@Param("lineType")String lineType, @Param("lineName")String lineName,@Param("departureTime") Time departureTime);

    @Query(value = "select shift from Shift shift where shift.shiftId=:shiftId")
    Shift queryShiftByShiftId(@Param("shiftId") String shiftId);

    @Query(value = "select departureTime from Shift where lineType=:lineType and lineNameCn=:lineNameCn")
    List<Time> getTimeListByLineNameCnAndLineType(@Param("lineType") String lineType, @Param("lineNameCn") String lineNameCn);

    @Transactional
    @Modifying
    @Query("update Shift shift set shift.reserveSeat = :reserveSeat where shift.shiftId =:shiftId")
    int updateReserveSeat(@Param("reserveSeat") int reserveSeat,@Param("shiftId") String shiftId);

    @Query(value = "select shift from Shift shift where shift.lineName like %:content% or shift.lineNameCn like %:content%" +
            " or shift.lineType like %:content% or shift.comment like %:content%")
    List<Shift> queryByRelatedContent(@Param("content") String content);
}
