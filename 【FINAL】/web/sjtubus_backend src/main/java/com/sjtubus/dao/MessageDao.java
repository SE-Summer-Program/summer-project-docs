package com.sjtubus.dao;

import com.sjtubus.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageDao extends JpaRepository<Message, Integer> {
}
