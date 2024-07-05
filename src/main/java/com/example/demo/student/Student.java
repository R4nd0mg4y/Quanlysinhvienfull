package com.example.demo.student;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_sequence")
    @SequenceGenerator(name = "student_sequence", sequenceName = "student_sequence", allocationSize = 1)
    private Long id;

    private String name;

    @Transient
    private int age;
    private int numberOfsubject=0;
    private LocalDate dob;

    private String email;

    @ElementCollection
    private Set<SubjectInfo> subjects = new HashSet<>();

    public Student() {
    }

    public Student(String name, String email, LocalDate dob) {
        this.name = name;
        this.email = email;
        this.dob = dob;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return Period.between(dob, LocalDate.now()).getYears();
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<SubjectInfo> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<SubjectInfo> subjects) {
        this.subjects = subjects;
    }

    public void add(long subjectId,String subjectName) {
        numberOfsubject++;
        subjects.add(new SubjectInfo(subjectId,subjectName));
    }
    public void drop(long subjectId,String subjectName){
        numberOfsubject--;
        SubjectInfo subjectinfo = new SubjectInfo(subjectId,subjectName);
        subjects.remove(subjectinfo);
    }
   

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", dob=" + dob +
                ", email='" + email + '\'' +
                ", subjects=" + subjects +
                '}';
    }

    public int getNumberOfsubject() {
        return numberOfsubject;
    }
}
