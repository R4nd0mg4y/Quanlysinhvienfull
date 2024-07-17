package com.example.demo.student;

import java.time.LocalDateTime;
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
            throw new IllegalStateException("Email trùng lặp");
        }
        studentRepository.save(student);
    }

    public void deleteStudent(long studentId) {
      Student  student = studentRepository.findById(studentId).orElseThrow(() -> new IllegalStateException("Sinh viên với id là " + studentId + " không tồn tại"));
        for(SubjectInfo subjectInfo:student.getSubjects()){
            dropSubject(studentId,subjectInfo.getId());
            
        }
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(long studentId, String name, String email) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("Sinh viên với id là " + studentId + " không tồn tại"));
        
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
                throw new IllegalStateException("Email bị trùng");
            }
            student.setEmail(email);
        
        }
    }

    public void enrollSubject(long studentId, long subjectId){
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new IllegalStateException("Môn học với id là " + subjectId + " không tồn tại"));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("sinh viên với id là " + studentId + " không tồn tại"));
      
        boolean alreadyEnrolled = false;
        for (SubjectInfo enrolledSubject : student.getSubjects()) {
            if (enrolledSubject.getId() == subjectId) {
                alreadyEnrolled = true;
                break;
            }
        }
        if (alreadyEnrolled) {
            throw new IllegalStateException("Sinh viên đã đăng ký môn học này");
        }
        if(subject.getNumberOfslot()<1){
            throw new IllegalStateException("Môn học này đã hết số lượng chỗ trống");
        }
        student.add(subjectId,subject.getName(),LocalDateTime.now());
        subject.setNumberOfslot(subject.getNumberOfslot()-1);
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // String masv = authentication.getName();
        subject.add(studentId,student.getName(),student.getMasv());
        subjectRepository.save(subject);
        studentRepository.save(student);

    }
    public void dropSubject(long studentId,long subjectId){
        Subject subject = subjectRepository.findById(subjectId)
        .orElseThrow(() -> new IllegalStateException("Môn học với id là " + subjectId + " không tồn tại"));
        Student student = studentRepository.findById(studentId)
        .orElseThrow(() -> new IllegalStateException("Sinh viên với id là " + studentId + " không tồn tại"));
        boolean alreadyEnrolled = false;
        for (SubjectInfo enrolledSubject : student.getSubjects()) {
            if (enrolledSubject.getId() == subjectId) {
                alreadyEnrolled = true;
                break;
            }
        }
        if (!alreadyEnrolled) {
            throw new IllegalStateException("Sinh viên chưa đăng ký môn học này");
        }
        student.drop(subjectId,subject.getName());
        subject.drop(studentId,student.getName());
        subject.setNumberOfslot(subject.getNumberOfslot()+1);
        subjectRepository.save(subject);
        studentRepository.save(student);
    }
    
}
