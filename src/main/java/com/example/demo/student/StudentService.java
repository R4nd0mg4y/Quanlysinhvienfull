package com.example.demo.student;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.subject.StudentInfo;
import com.example.demo.subject.Subject;
import com.example.demo.subject.SubjectRepository;

import jakarta.transaction.Transactional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, SubjectRepository subjectRepository) {
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
    }
    
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail().toLowerCase());
        if (studentOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }
        studentRepository.save(student);
    }

    public void deleteStudent(long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if (!exists) {
            throw new IllegalStateException("student with id " + studentId + " does not exist");
        }
       
      Student  student = studentRepository.findById(studentId).orElseThrow(() -> new IllegalStateException("student with id " + studentId + " does not exist"));
        for(SubjectInfo subjectInfo:student.getSubjects()){
            dropSubject(studentId,subjectInfo.getId());
            
        }
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(long studentId, String name, String email) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("student with id " + studentId + " does not exist"));
        
        if (name != null && name.length() > 0 && !Objects.equals(student.getName(), name)) {
            student.setName(name);
            List<Subject> subjects = subjectRepository.findAll();
            for(Subject s:subjects){
                for(StudentInfo studentinfo:s.getStudents()){
                    if(studentinfo.getId()==studentId){
                        studentinfo.setName(name);
                    }
                }
            }
        
        }
        
        if (email != null && email.length() > 0 && !Objects.equals(student.getEmail(), email)) {
            Optional<Student> studentOptional = studentRepository.findStudentByEmail(email.toLowerCase());
            if (studentOptional.isPresent()) {
                throw new IllegalStateException("email taken");
            }
            student.setEmail(email);
        
        }
    }

    public void enrollSubject(long studentId, long subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new IllegalStateException("subject with id " + subjectId + " does not exist"));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("student with id " + studentId + " does not exist"));
      
        boolean alreadyEnrolled = false;
        for (SubjectInfo enrolledSubject : student.getSubjects()) {
            if (enrolledSubject.getId() == subjectId) {
                alreadyEnrolled = true;
                break;
            }
        }
        if (alreadyEnrolled) {
            throw new IllegalStateException("Student already enrolled in this subject");
        }
        if(subject.getNumberOfslot()<1){
            throw new IllegalStateException("Out of slot for this course");
        }
        student.add(subjectId,subject.getName());
        subject.setNumberOfslot(subject.getNumberOfslot()-1);
        subject.add(studentId,student.getName());
        subjectRepository.save(subject);
        studentRepository.save(student);

    }
    public void dropSubject(long studentId,long subjectId){
        Subject subject = subjectRepository.findById(subjectId)
        .orElseThrow(() -> new IllegalStateException("subject with id " + subjectId + " does not exist"));
        Student student = studentRepository.findById(studentId)
        .orElseThrow(() -> new IllegalStateException("student with id " + studentId + " does not exist"));
        boolean alreadyEnrolled = false;
        for (SubjectInfo enrolledSubject : student.getSubjects()) {
            if (enrolledSubject.getId() == subjectId) {
                alreadyEnrolled = true;
                break;
            }
        }
        if (!alreadyEnrolled) {
            throw new IllegalStateException("Student have not enrolled in this subject");
        }
        student.drop(subjectId,subject.getName());
        subject.drop(studentId,student.getName());
        subject.setNumberOfslot(subject.getNumberOfslot()+1);
        subjectRepository.save(subject);
        studentRepository.save(student);
    }
    
}
