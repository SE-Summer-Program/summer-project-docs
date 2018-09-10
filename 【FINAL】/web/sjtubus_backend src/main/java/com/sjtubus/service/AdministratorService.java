package com.sjtubus.service;

import com.sjtubus.dao.AdministratorDao;
import com.sjtubus.entity.Administrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.List;

@Service
public class AdministratorService implements UserDetailsService {
    @Autowired
    private AdministratorDao administratorDao;


    public List<Administrator> searchAdministrator(String username){
        return administratorDao.searchByUsername(username);
    }

    public Administrator saveAdministrator(String username, String password){
        Administrator administrator = new Administrator();
        administrator.setPassword(password);
        administrator.setUsername(username);
        return administratorDao.save(administrator);
    }

    public Administrator findAdminByUsername(String username){
        return administratorDao.findByUsername(username);
    }

    @Override
    public Administrator loadUserByUsername(String username) throws UsernameNotFoundException {
        Administrator admin = administratorDao.findByUsername(username);
        if(admin == null)
        {
            throw new UsernameNotFoundException("Admin："+username+" Not Found！");
        }
        return admin;
    }
}
