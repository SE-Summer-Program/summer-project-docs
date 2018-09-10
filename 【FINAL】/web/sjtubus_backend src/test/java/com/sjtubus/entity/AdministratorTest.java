package com.sjtubus.entity;

import org.junit.Test;

import static org.junit.Assert.*;

public class AdministratorTest {

    private Administrator administrator = new Administrator();

    @Test
    public void setUsername() {
        administrator.setUsername("admin");
        assertEquals("admin", administrator.getUsername());
    }

    @Test
    public void setPassword() {
        administrator.setPassword("123456");
        assertEquals("123456", administrator.getPassword());
    }
}