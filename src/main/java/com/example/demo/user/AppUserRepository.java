package com.example.demo.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser,Long>{
    AppUser findByMasv(String masv);
    AppUser findByEmail(String email);
    
} 
