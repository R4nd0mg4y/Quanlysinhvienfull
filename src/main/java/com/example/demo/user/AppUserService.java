package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserService implements UserDetailsService {
    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String masv) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByMasv(masv);
        if (appUser != null) {
            return User.withUsername(appUser.getMasv())
                       .password(appUser.getPassword())
                       .roles(appUser.getRole())
                       .build();
        }
        throw new UsernameNotFoundException("Not found");
    }
}
