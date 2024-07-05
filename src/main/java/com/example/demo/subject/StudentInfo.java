package com.example.demo.subject;

import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class StudentInfo {
    private long id;
    private String name;
    public StudentInfo(){

    }
    public StudentInfo(long id, String name) {
        this.id = id;
        this.name = name;
    }
    public long getId() {
        return id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentInfo)) return false;
        StudentInfo that = (StudentInfo) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
   
    
}
