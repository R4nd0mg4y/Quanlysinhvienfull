package com.example.demo.home;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.example.demo.subject.Subject;

@Controller
public class HomeController {
    @GetMapping({"","/"})
    public String home(Model model){
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
                model.addAttribute("subjects", subjects);
            } catch (Exception e) {
                model.addAttribute("message", "Lỗi khi lấy danh sách môn học: " + e.getMessage());
            }
            
            return "home";
        }
    
    @GetMapping("/contact")
    public String contact(){
        return "contact";
    }
}
