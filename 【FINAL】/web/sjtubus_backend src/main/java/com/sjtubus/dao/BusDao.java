package com.sjtubus.dao;

import com.sjtubus.entity.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusDao extends JpaRepository<Bus,Integer> {
    Bus findByBusId(int busId);
}
