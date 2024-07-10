package com.example.demo.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser,Integer>{
    AppUser findByMasv(String masv);
    AppUser findByEmail(String email);
    
} 
