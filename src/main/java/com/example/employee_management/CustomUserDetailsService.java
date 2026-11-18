package com.example.employee_management;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        User user = userRepository.findByUserName(username)
            .orElseThrow(() -> new UsernameNotFoundException("That username does not exist."));

        UserDetails userDetails = org.springframework.security.core.userdetails.User
            .withUsername(user.getUserName())
            .password(user.getPassword())
            .authorities("ROLE_" + user.getCompanyRole().name())
            .disabled(!user.isEnabled())
            .build();
        
        return userDetails;
    }    
}
