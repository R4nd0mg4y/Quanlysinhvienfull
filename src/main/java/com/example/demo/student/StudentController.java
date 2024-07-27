package com.example.demo.student;

// import java.time.LocalDate;
// import java.time.Month;
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
@RequestMapping(path ="api/v1/student")
public class StudentController {
	private final StudentService studentService;
	@Autowired // inject studentService into constructor
	public StudentController(StudentService studentService){
		this.studentService = studentService;
	}
    @GetMapping
	public List<Student> getStudent(){
		return studentService.getStudents();	
	}
	@PostMapping
	public void  RegisterNewStudent(@RequestBody Student student){
		studentService.addNewStudent(student);
	}
	@DeleteMapping(path = "{studentId}")
	public void deleteStudent(@PathVariable("studentId") long studentId){
		studentService.deleteStudent(studentId);
	}
	@PutMapping(path = "{studentId}")
	public void updateStudent(@PathVariable("studentId") long studentId,@RequestParam(required = false) String name,@RequestParam(required = false) String email){
		studentService.updateStudent(studentId,name,email);
	}
	@PutMapping(path = "{studentId}/enroll")
	public void enrollSubject(@PathVariable("studentId") long studentId,@RequestParam(required = true) long subjectId){
		studentService.enrollSubject(studentId, subjectId);
	}	
	@DeleteMapping(path="{studentId}/drop")
	public void dropSubject(@PathVariable("studentId") long studentId,@RequestParam(required = true) long subjectId){
		studentService.dropSubject(studentId, subjectId);
	}
}
