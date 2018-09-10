package com.sjtubus;
import com.sjtubus.controller.AppointmentController;
import com.sjtubus.controller.LineController;
import com.sjtubus.entity.User;
import com.sjtubus.service.AppointmentService;
import com.sjtubus.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SjtubusApplicationTests {

	@Before
	public void init() {
		System.out.println("开始测试-----------------");
	}

	@After
	public void after() {
		System.out.println("测试结束-----------------");
	}

	@Test
	public void contextLoads() {
	}

	@Autowired
	private UserService userService;

	@Autowired
	private AppointmentController appointmentController;

	@Autowired
	private LineController lineController;

	@Test
	public void testUser(){
	//	UserService userService = new UserService();
		//userService.addUser("user", "password", false);
		List<User> users = userService.listAllUsers();
		System.out.println(1234);
		for (User user : users){
			System.out.println(123);
			System.out.println(user.getUsername());
		}
	}

	@Test
	public void testLineController(){
		lineController.getLineInfo("NormalWorkday");
	}



	@Test
	public void testRecordSerivce(){
		appointmentController.getRecordInfos("王鑫伟","user");

	}
}
