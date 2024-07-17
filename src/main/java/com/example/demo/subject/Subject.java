package com.example.demo.subject;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;


@Entity
@Table
public class Subject implements Comparable<Subject>{
    @Id
    @SequenceGenerator(
        name = "subject_sequence",
        sequenceName = "subject_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "subject_sequence"
    )
    private long id;
    private String name;
    private Integer numberOfslot;
    private int numberOfStudent = 0;
    @ElementCollection
    private Set<StudentInfo> students = new HashSet<>();

    
    public void add(long id,String StudentName,String masv){
        numberOfStudent++;
        students.add(new StudentInfo(id, StudentName,masv));
    }
    public void drop(long id,String studentName){
        numberOfStudent--;
        students.remove(new StudentInfo(id, studentName));
    }
    @Override
    public int compareTo(Subject other) {
        return Integer.compare(other.numberOfStudent, this.numberOfStudent);
    }

    public Subject(){
        
    }
    public Subject(String name,  Integer numberOfslot) {
        this.name = name;
        this.numberOfslot = numberOfslot;
    }
    public String getName() {
        return name;
    }
    public long getId() {
        return id;
    }
    public int getNumberOfslot() {
        return numberOfslot;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setNumberOfslot(Integer numberOfslot) {
        this.numberOfslot = numberOfslot;
    }

    public int getNumberOfStudent() {
        return numberOfStudent;
    }
    public Set<StudentInfo> getStudents() {
        return students;
    }
    
   
}   
