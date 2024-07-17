package com.example.demo.subject;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
import com.example.demo.student.StudentService;
import com.example.demo.student.SubjectInfo;
@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final StudentService studentService;
    @Autowired
    public SubjectService(SubjectRepository subjectRepository,StudentService studentService,StudentRepository studentRepository){
        this.subjectRepository = subjectRepository;
        this.studentService = studentService;
        this.studentRepository = studentRepository;
    }
    public List<Subject> getSubjects(){
        return subjectRepository.findAll();
    }
    public void addNewSubject(Subject subject){
        Optional<Subject> subjecOptional = subjectRepository.findSubjectByName(subject.getName().toLowerCase());
        if(subjecOptional.isPresent()){
            throw new IllegalStateException("Môn học này đã tồn tại");
        }
        subjectRepository.save(subject);
    }
    public void deleteSubject(long subjectId){
        boolean exist = subjectRepository.existsById(subjectId);
        if(!exist){
            throw new IllegalStateException("Không có môn học với id là "+subjectId);
        }
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> new IllegalStateException("Môn học với id là " + subjectId + " không tồn tại"));
        for(StudentInfo studentinfo:subject.getStudents()){
            studentService.dropSubject(studentinfo.getId(),subjectId);
        }
        subjectRepository.deleteById(subjectId);


    }
    public void updateSubject(long subjectId, String name, Integer numberOfslot) {
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> new IllegalStateException("Môn học với id là " + subjectId + " không tồn tại"));
        if(name!=null &&name.length() > 1 &&!Objects.equals(subject.getName(), name)){
			subject.setName(name);
        List<Student> students = studentRepository.findAll();
        for(Student student:students){
            for(SubjectInfo subjectInfo:student.getSubjects()){
                if(subjectInfo.getId()==subjectId){
                subjectInfo.setName(name);
                }
            }
        }
		}
    
        if(numberOfslot!=null){
        subject.setNumberOfslot(numberOfslot);
        }
        
    
        subjectRepository.save(subject);

    }

}
