package com.example.demo.student;

import java.time.LocalDateTime; // Import the LocalDateTime class
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class SubjectInfo {
    private Long id;
  

    private String name;
    private LocalDateTime date;
    public SubjectInfo(){

    }
    public SubjectInfo(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    public SubjectInfo(Long id, String name,LocalDateTime date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }
    public LocalDateTime getDate() {

        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubjectInfo)) return false;
        SubjectInfo that = (SubjectInfo) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    
   

    // Constructors, getters và setters
}