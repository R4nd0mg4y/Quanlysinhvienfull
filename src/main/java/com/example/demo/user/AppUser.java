package com.example.demo.user;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class AppUser{
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long id;

@Column(unique=true,nullable=false)
private String masv;    


private String name;

@Column(unique=true,nullable=false)
private String email;




// @Size(min =6,message="Minium length is 6 characters")
private String password;


private String role;
public String getMasv() {
    return masv;
}
public void setMasv(String masv) {
    this.masv = masv;
}
private LocalDate dob;
public String getName() {
    return name;
}
public void setName(String name) {
    this.name = name;
}
public String getEmail() {
    return email;
}
public void setEmail(String email) {
    this.email = email;
}
public String getPassword() {
    return password;
}
public void setPassword(String password) {
    this.password = password;
}
public String getRole() {
    return role;
}
public void setRole(String role) {
    this.role = role;
}
public LocalDate getDob() {
    return dob;
}
public void setDob(LocalDate dob) {
    this.dob = dob;
}
public long getId() {
    return id;
}


}
