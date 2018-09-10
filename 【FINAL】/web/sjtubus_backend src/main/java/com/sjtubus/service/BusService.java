package com.sjtubus.service;

import com.sjtubus.dao.BusDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusService {
    @Autowired
    private BusDao busDao;
}
