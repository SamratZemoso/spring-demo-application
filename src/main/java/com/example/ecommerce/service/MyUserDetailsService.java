package com.example.ecommerce.service;

import com.example.ecommerce.model.MyUser;
import com.example.ecommerce.model.MyUserDetail;
import com.example.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyUser> user = userRepository.findByUsername(username);

        user.orElseThrow(() -> new UsernameNotFoundException(username + " not found"));

        return user.map(MyUserDetail::new).get();
    }

    public MyUser findByUsername(String username) {
        Optional<MyUser> user = userRepository.findByUsername(username);
        return user.orElse(null);
    }

}
