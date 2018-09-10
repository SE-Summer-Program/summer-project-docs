package com.sjtubus.entity;

import org.junit.Test;

import static org.junit.Assert.*;

public class BusTest {

    private Bus bus = new Bus();

    @Test
    public void setSeatNum() {
        bus.setSeatNum(100);
        assertEquals(100, bus.getSeatNum());
    }

    @Test
    public void setPlateNum() {
        bus.setPlateNum("沪D52210");
        assertEquals("沪D52210", bus.getPlateNum());
    }
}