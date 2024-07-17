package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.student.Student;
import com.example.demo.subject.Subject;

@Controller
public class HTMLController {

    // @GetMapping("/home")
    // public String showRegisterForm(Model model) {
    //     model.addAttribute("message", "Xin chào, đây là thông báo từ controller!");
    //     return "home"; 
    // }
    @GetMapping("/enroll")
    public String enrollForm() {
        return "enroll";
    }

    @PostMapping("/enroll")
    public String enrollSubject(@RequestParam("studentId") long studentId,
                                @RequestParam("subjectId") long subjectId,
                                RedirectAttributes redirectAttributes) {
        // URL API để đăng ký môn học cho học sinh
        String enrollUrl = "http://localhost:8080/api/v1/student/" + studentId + "/enroll?subjectId=" + subjectId;
        
        try {
            // Thực hiện yêu cầu HTTP PUT để đăng ký môn học
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.put(enrollUrl, null);
            redirectAttributes.addFlashAttribute("message", "Đăng ký môn học thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Lỗi khi đăng ký môn học: " + e.getMessage());
            return "redirect:/enroll";
        }
        
        return "redirect:/"; // Trả về trang HTML sau khi xử lý
    }

    @GetMapping("/drop")
    public String dropForm() {
        return "drop";
    }

    @PostMapping("/drop")
    public String dropSubject(@RequestParam("studentId") long studentId,
                              @RequestParam("subjectId") long subjectId,
                              RedirectAttributes redirectAttributes) {
        // URL API để xóa môn học của học sinh
        String dropUrl = "http://localhost:8080/api/v1/student/" + studentId + "/drop?subjectId=" + subjectId;
        
        try {
            // Thực hiện yêu cầu HTTP DELETE để xóa môn học
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.delete(dropUrl);
            redirectAttributes.addFlashAttribute("message", "Xóa môn học thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Lỗi khi xóa môn học: " + e.getMessage());
            return "redirect:/drop";
        }
        
        return "redirect:/"; 
    }
    @GetMapping("/listStudents")
    public String listStudents(Model model) {
        String listStudentsUrl = "http://localhost:8080/api/v1/student";
        
        try {
            RestTemplate restTemplate = new RestTemplate();
            Student[] studentsArray = restTemplate.getForObject(listStudentsUrl, Student[].class);
            List<Student> students = new ArrayList<>();
            if (studentsArray != null) {
                for (Student student : studentsArray) {
                    students.add(student);
                }
            }
            
            model.addAttribute("students", students);
        } catch (Exception e) {
            model.addAttribute("message", "Lỗi khi lấy danh sách học sinh: " + e.getMessage());
        }
        
        return "listStudents";
        
        
    }
    // @GetMapping("/addStudent")
    // public String addStudentForm() {
    //     return "addStudent";
    // }

    // @PostMapping("/addStudent")
    // public String addStudent( RedirectAttributes redirectAttributes,@RequestParam(required = true) String name,
    // @RequestParam(required = true) String email,@RequestParam(required = true) LocalDate dob){
    //     String url = "http://localhost:8080/api/v1/student";
    //     try{
    //         Student student = new Student(name,email,dob);
    //         RestTemplate restTemplate = new RestTemplate();
    //         restTemplate.postForObject(url,student,Student.class);
    //         redirectAttributes.addFlashAttribute("message", "Thêm học sinh thành công!");
    //     } catch (Exception e) {
    //         redirectAttributes.addFlashAttribute("message", "Không thêm được sinh viên: " + e.getMessage());
    //         return "redirect:/addStudent";
    //     }
    //     return "redirect:/listStudents";
    // }
    @GetMapping("/updateStudent")
    public String updateStudentForm() {
        return "updateStudent";
    }
    @PostMapping("/updateStudent")
    public String updateStudent(@RequestParam("studentId") long studentId,@RequestParam(required = false) String studentName,
    @RequestParam(required = false) String studentEmail, RedirectAttributes redirectAttributes) {
        String url = "http://localhost:8080/api/v1/student/" + studentId+"?name="+studentName+"&email="+studentEmail;
        try {
          
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.put(url," ");

            redirectAttributes.addFlashAttribute("message", "Cập nhật học sinh thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Lỗi khi cập nhật học sinh: " + e.getMessage());
            return "redirect:/updateStudent";
        }

        return "redirect:/listStudents";
    }
    @GetMapping("/listSubjects")
    public String listSubjects(Model model) {
        String listSubjectsUrl = "http://localhost:8080/api/v1/subject";
        
        try {
            RestTemplate restTemplate = new RestTemplate();
            Subject[] subjectsArray = restTemplate.getForObject(listSubjectsUrl, Subject[].class);
            List<Subject> subjects = new ArrayList<>();
            if (subjectsArray != null) {
                for (Subject subject : subjectsArray) {
                    subjects.add(subject);
                }
            }
            Collections.sort(subjects);
            model.addAttribute("subjects", subjects);
        } catch (Exception e) {
            model.addAttribute("message", "Lỗi khi lấy danh sách môn học: " + e.getMessage());
        }
        
        return "listSubjects";
    }
    @GetMapping("/addSubject")
    public String addSubjectForm() {
        return "addSubject";
    }
    
    @PostMapping("/addSubject")
    public String addSubject(RedirectAttributes redirectAttributes,@RequestParam(required = true) String name,
    @RequestParam(required = true) Integer numberOfslot){
        String url = "http://localhost:8080/api/v1/subject";
        try{
            Subject subject = new Subject(name,numberOfslot);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForObject(url,subject,Subject.class);
            redirectAttributes.addFlashAttribute("message", "Thêm môn học thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Không thêm được môn học: " + e.getMessage());
            return "redirect:/addSubject";
        }
        return "redirect:/listSubjects";
    }
    @GetMapping("/updateSubject")
    public String updateSubjectForm() {
        return "updateSubject";
    }
    @PostMapping("/updateSubject")
    public String updateSubject(@RequestParam(required = true) long subjectId,@RequestParam(required = false) String subjectName,
    @RequestParam(required = false) String numberOfslot, RedirectAttributes redirectAttributes) {
        String url = "http://localhost:8080/api/v1/subject/" + subjectId+"?name="+subjectName+"&numberOfslot="+numberOfslot;
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.put(url," ");
            redirectAttributes.addFlashAttribute("message", "Cập nhật môn học thành công!");
        }  catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Lỗi khi cập nhật môn học: " + e.getMessage());
            return "redirect:/updateStudent";
}

        return "redirect:/listSubjects";
    }
    @GetMapping("/deleteSubject")
    public String deleteSubjectForm() {
        return "deleteSubject";
    }

    @PostMapping("/deleteSubject")
    public String deleteSubject(@RequestParam(required = true) long subjectId, RedirectAttributes redirectAttributes) {
        String url = "http://localhost:8080/api/v1/subject/" + subjectId;
        try {
            
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.delete(url);
            redirectAttributes.addFlashAttribute("message", "Xóa môn học thành công!");
        }  catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Lỗi khi xóa môn học: " + e.getMessage());
            return "redirect:/deleteSubject";
        }

        return "redirect:/listSubjects";
    }
    @GetMapping("/deleteStudent")
    public String deleteStudentForm() {
        return "deleteStudent";
    }
    @PostMapping("/deleteStudent")
    public String deleteStudent(@RequestParam("studentId") long studentId, RedirectAttributes redirectAttributes) {
        String url = "http://localhost:8080/api/v1/student/" + studentId;
        try {
            
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.delete(url);

            redirectAttributes.addFlashAttribute("message", "Xóa học sinh thành công!");
        }  catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Lỗi khi xóa học sinh: " + e.getMessage());
            return "redirect:/deleteStudent";
        }

        return "redirect:/listStudents";
    }


}