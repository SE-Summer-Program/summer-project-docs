package com.sjtubus.service;

import com.sjtubus.dao.DriverDao;
import com.sjtubus.dao.JaccountUserDao;

import com.sjtubus.dao.UserDao;
import com.sjtubus.entity.JaccountUser;
import com.sjtubus.entity.Shift;
import com.sjtubus.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private JaccountUserDao jaccountUserDao;

    @Autowired
    private DriverDao driverDao;

    /**
     * @description: 根据content比对数据库中的user，找出username或者phone包含content字段的user
     * @date: 2018/7/18 20:14
     * @params: 字段content
     * @return: 包含字段content的用户列表
     */
    public List<User> getUserInfo(String content){
        return  userDao.queryByRelatedContent(content);
    }


    public String addUser(String username, String password, boolean isTeacher, String phone, int credit) {
        User olduser = userDao.findByUsername(username);
        if (olduser == null) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setCredit(credit);
            user.setTeacher(isTeacher);
            user.setPhone(phone);
            userDao.save(user);
            return "success";
        } else {
            return "existed";
        }
    }

    public String deleteUser(int userId){
        User olduser = userDao.findByUserId(userId);
        if( olduser != null ){
            userDao.delete(olduser);
            return "success";
        }
        else {
            return "fail";
        }
    }

    public int modifyUser(int userId, String username, String phone, int credit){
        return userDao.modifyUser(userId, username, phone, credit);
    }

    public boolean updatePersonInfos(int userId, String phone, String studentnum, String realname){
        User user = userDao.findByUserId(userId);
        if (user == null)
            return false;
        if (phone != null && !phone.isEmpty())
            user.setPhone(phone);
        if (studentnum != null && !studentnum.isEmpty())
            user.setStudentNumber(studentnum);
        if (realname != null && !realname.isEmpty())
            user.setRealname(realname);
        userDao.save(user);
        return true;
    }

    /**
     * @description: 添加jaccount用户
     * @date: 2018/7/16 12:54
     * @params: 用户名、是否教师、电话
     * @return: 所添加用户
     */
    public JaccountUser addJaccountUser(String username, boolean isTeacher, String phone,String realname,String student_num){
        JaccountUser user = new JaccountUser();
        user.setUsername(username);
        user.setCredit(100);
        user.setTeacher(isTeacher);
        user.setPhone(phone);
        user.setRealname(realname);
        user.setStudentNumber(student_num);
        return jaccountUserDao.save(user);
    }

    /**
     * @description: 通过电话号码找到user
     * @date: 2018/7/13 11:12
     * @params: phone - 电话号码
     * @return: User
    */
    @Transactional
    public User findUserByPhone(String phone){
        return userDao.findByPhone(phone);
    }

    @Transactional
    public User findByUserName(String username){ return userDao.findByUsername(username); }

    @Transactional
    public List<User> listAllUsers(){
        return userDao.findAll();
    }

    @Transactional
    public void saveUser(User user){
        userDao.save(user);
    }

    @Transactional
    public JaccountUser findJaccountUserByPhone(String phone){
        return jaccountUserDao.findByPhone(phone);
    }

    @Transactional
    public JaccountUser findByJaccountUserName(String username){ return jaccountUserDao.findByUsername(username); }

    @Transactional
    public List<JaccountUser> listAllJaccountUsers(){
        return jaccountUserDao.findAll();
    }

    @Transactional
    public void saveJaccountUser(JaccountUser user){ jaccountUserDao.save(user); }
}
