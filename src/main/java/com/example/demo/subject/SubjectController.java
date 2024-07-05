package com.example.demo.subject;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "api/v1/subject")
public class SubjectController {
    private final SubjectService subjectService;

    @Autowired
    public SubjectController(SubjectService subjectService){
        this.subjectService = subjectService;
    }
    @GetMapping
    public List<Subject> getSubject(){
        return subjectService.getSubjects();
    }
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    
    @PostMapping
    public void addNewSubject(@RequestBody Subject subject) {
        subjectService.addNewSubject(subject);
    }
    @DeleteMapping(path = "/{subjectId}")
    public void deleteSubject(@PathVariable("subjectId") long subjectId){
        subjectService.deleteSubject(subjectId);
    }
    @PutMapping(path ="/{subjectId}")
    public void updateSubject(@PathVariable("subjectId") long subjectId,@RequestParam(required = false) String name,@RequestParam(required = false) Integer numberOfslot) {
        subjectService.updateSubject(subjectId,name,numberOfslot);
    }
}
