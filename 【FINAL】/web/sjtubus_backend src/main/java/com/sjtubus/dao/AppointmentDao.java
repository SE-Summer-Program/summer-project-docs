package com.sjtubus.dao;

import com.sjtubus.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Date;
import java.time.Month;
import java.util.List;

import java.util.List;

public interface AppointmentDao extends JpaRepository<Appointment,Integer> {

    List<Appointment> findByShiftIdAndAppointDate(String shiftId, java.sql.Date appointDate);

    List<Appointment> findByShiftIdAndAppointDateAndIsNormal(String shiftId,java.sql.Date appointDate,boolean isNormal);

    List<Appointment> findByUserNameAndUserRole(String username,String user_role);

    Appointment findByUserNameAndUserRoleAndShiftIdAndAppointDate(String username,String user_role, String shiftid, java.sql.Date appoint_date);

    @Query(value = "select appointment from Appointment appointment where appointment.shiftId like %:shiftId% and appointment.appointDate=:appointDate")
    List<Appointment> queryAppointmentByShiftIdAndAppointDate(@Param("shiftId") String shiftId, @Param("appointDate") java.sql.Date appointDate);

    @Query(value = "select appointment from Appointment appointment where appointment.shiftId like %:shiftId% and appointment.appointDate=:appointDate and appointment.userName=:userName")
    Appointment queryAppointmentByShiftIdAndAppointDateAndUserName(@Param("shiftId") String shiftId, @Param("appointDate") java.sql.Date appointDate,@Param("userName") String userName);

    @Query(value = "select appointment from Appointment appointment where appointment.shiftId like %:shiftId% and appointment.appointDate between :startDate and :endDate")
    List<Appointment> queryAppointmentByShiftIdAndPeriod(@Param("shiftId") String shiftId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "select appointment.appointDate, count(appointment) from Appointment appointment where appointment.shiftId like %:shiftId% and appointment.appointDate between :startDate and :endDate group by appointment.appointDate order by appointment.appointDate asc ")
    List<Object[]> queryAppointmentGroupByDate(@Param("shiftId")String shiftId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Transactional
    @Modifying
    @Query("update Appointment appointment set appointment.isNormal=true where appointment.shiftId like %:shiftId% and appointment.appointDate=:appointDate and appointment.userName=:userName ")
    int verifyNormal(@Param("shiftId") String shiftId, @Param("appointDate") java.sql.Date appointDate,@Param("userName") String userName);
}
