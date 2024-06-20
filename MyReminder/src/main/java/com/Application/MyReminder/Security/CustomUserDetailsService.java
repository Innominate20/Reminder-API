package com.Application.MyReminder.Security;

import com.Application.MyReminder.Service.ServiceDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    ServiceDAO serviceDAO;

    @Autowired
    public CustomUserDetailsService(ServiceDAO serviceDAO) {
        this.serviceDAO = serviceDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.Application.MyReminder.entities.User user = serviceDAO.get(username);
        if (user == null){
            throw new UsernameNotFoundException("User not found!");
        }
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), new ArrayList<>());

    }
}
