package com.sjtubus;

import com.sjtubus.dao.AdministratorDao;
import com.sjtubus.dao.DriverDao;
import com.sjtubus.entity.Administrator;
import com.sjtubus.entity.Driver;
import com.sjtubus.entity.JaccountUser;
import com.sjtubus.entity.User;
import com.sjtubus.service.AdministratorService;
import com.sjtubus.service.DriverService;
import com.sjtubus.service.UserService;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceTests extends SjtubusApplicationTests {
    @Autowired
    private UserService userService;
    @Autowired
    private AdministratorService administratorService;
    @Autowired
    private AdministratorDao administratorDao;
    @Autowired
    private DriverService driverService;
    @Autowired
    private DriverDao driverDao;


    @Test
    public void a_testFindByUsername() {
        User user = userService.findByUserName("ly");
        int judgeId = user.getUserId();
        Assert.assertEquals("fail",3,judgeId);
        user = userService.findByUserName("hello");
        Assert.assertNull(user);
    }


    @Test
    public void b_testFindByJaccountUserName() {
        JaccountUser user = userService.findByJaccountUserName("ly");
        int judgeId = user.getUserId();
        Assert.assertEquals("fail",3,judgeId);
        user = userService.findByJaccountUserName("hello");
        Assert.assertNull(user);
    }


    @Test
    public void c_testFindUserByPhone(){
        User user = userService.findUserByPhone("12345678900");
        int judgeId = user.getUserId();
        Assert.assertEquals("fail",1,judgeId);
        user = userService.findUserByPhone("00000000000");
        Assert.assertNull(user);
    }


    @Test
    public void e_testListAllUsers(){
        List<User> result = userService.listAllUsers();
        Assert.assertEquals(62,result.size());
    }

    @Test
    public void g_testModifyUser(){
        User user = userService.findByUserName("yzh");
        int old_credit = user.getCredit();
        int result = userService.modifyUser(user.getUserId(), "yzh", user.getPhone(), old_credit-5);
        Assert.assertEquals(1, result);
        user = userService.findByUserName("yzh");
        int new_credit = user.getCredit();
        Assert.assertEquals(old_credit-5, new_credit);
    }


    @Test
    public void i_testGetUserInfo(){
        List<User> result = userService.getUserInfo("ly");
        for (User user: result){
            Assert.assertTrue(user.getUsername().contains("ly") || user.getPhone().contains("ly"));
        }
        result = userService.getUserInfo("1326");
        for (User user: result){
            Assert.assertTrue(user.getUsername().contains("1326") || user.getPhone().contains("1326"));
        }
    }


    @Test
    public void j_testSearchAdministrator(){
        List<Administrator> administratorList = administratorService.searchAdministrator("admin");
        int judgeId = administratorList.get(0).getAdministratorId();
        Assert.assertEquals("fail",1,judgeId);
        administratorList = administratorService.searchAdministrator("abc");
        Assert.assertEquals("fail",0,administratorList.size());
    }


    @Test
    public void k_testSaveAdministrator(){
        List<Administrator> administratorList = administratorDao.findAll();
        int old_size = administratorList.size();
        administratorService.saveAdministrator("abc","abc");
        administratorList = administratorDao.findAll();
        int new_size = administratorList.size();
        Assert.assertEquals(old_size+1, new_size);  //增加后个数应比原来多一个
        administratorList = administratorService.searchAdministrator("abc");
        Assert.assertEquals(1, administratorList.size());  //search刚添加进去的这个管理员，个数为1
        administratorDao.delete(administratorList.get(0));
        administratorList = administratorService.searchAdministrator("abc");  //删除后个数为0
        Assert.assertEquals(0,administratorList.size());
    }


    @Test
    public void l_testGetDriverInfo(){
        List<Driver> result = driverService.getDriverInfo("driver");
        for (Driver driver: result){
            Assert.assertTrue(driver.getUsername().contains("driver") || driver.getPhone().contains("driver"));
        }
        result = driverService.getDriverInfo("1326");
        for (Driver driver: result){
            Assert.assertTrue(driver.getUsername().contains("1326") || driver.getPhone().contains("1326"));
        }
    }


    @Test
    public void m_testAddDriver(){
        int size = driverDao.findAll().size();
        driverService.addDriver("driver111","driver111","13262600000");
        int new_size = driverDao.findAll().size();
        Assert.assertEquals(size+1, new_size);
        Driver driver = driverDao.queryDriverByUsername("driver111");
        Assert.assertNotNull(driver);
    }


    @Test
    public void n_testModifyDriver(){
        Driver driver = driverDao.queryDriverByUsername("driver3");
        String old_phone = driver.getPhone();
        int result = driverService.modifyDriver(driver.getDriverId(), driver.getUsername(),"12345678900");
        Assert.assertEquals(1, result);
        driver = driverDao.queryDriverByUsername("driver3");
        String new_phone = driver.getPhone();
        Assert.assertEquals("12345678900", new_phone);
    }


    @Test
    public void o_testDeleteDriver(){
        int size = driverDao.findAll().size();
        Driver driver = driverDao.queryDriverByUsername("driver3");
        driverService.deleteDriver(driver.getDriverId());
        int new_size = driverDao.findAll().size();
        Assert.assertEquals(size-1, new_size);
        driver = driverDao.queryDriverByUsername("driver3");
        Assert.assertNull(driver);
        driverService.addDriver("driver3","password","18767600000");
    }

}
