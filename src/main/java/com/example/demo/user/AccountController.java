package com.example.demo.user;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.models.RegisterDTO;
import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
import com.example.demo.student.SubjectInfo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class AccountController {

    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private StudentRepository studentRepository;



    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        model.addAttribute("success", false);
        return "register";
    }

    @PostMapping("/register")
    public String register(Model model, @Valid @ModelAttribute("registerDTO") RegisterDTO registerDTO,
                           BindingResult result, HttpServletRequest request, RestTemplate restTemplate) {
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            result.addError(new FieldError("registerDTO", "confirmPassword", "Passwords do not match"));
        }

        AppUser existingUser = appUserRepository.findByMasv(registerDTO.getMasv());
        if (existingUser != null) {
            result.addError(new FieldError("registerDTO", "masv", "masv is already in use"));
        }
        AppUser existingUser2 = appUserRepository.findByEmail(registerDTO.getEmail());
        if (existingUser2 != null) {
            result.addError(new FieldError("registerDTO", "email", "email is already in use"));
        }

        if (result.hasErrors()) {
            return "register";
        }

        try {
            BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();
            AppUser newUser = new AppUser();
            newUser.setName(registerDTO.getName());
            newUser.setMasv(registerDTO.getMasv());
            newUser.setEmail(registerDTO.getEmail());
            newUser.setDob(registerDTO.getDob());
            newUser.setRole("User");
            newUser.setPassword(bCryptEncoder.encode(registerDTO.getPassword()));
            appUserRepository.save(newUser);
            request.login(registerDTO.getMasv(), registerDTO.getPassword());
            Student student = new Student(newUser.getMasv(),newUser.getName(), newUser.getEmail(), newUser.getDob());


            String url = "http://localhost:8080/api/v1/student";
            restTemplate = new RestTemplate();
            restTemplate.postForObject(url,student,Student.class);
            
            model.addAttribute("registerDTO", new RegisterDTO());
            model.addAttribute("success", true);
            
        } catch (Exception e) {
            result.addError(new FieldError("registerDTO", "name", "Registration failed. Please try again."));
            return "register";
        }

        return "redirect:/";
    }
    @GetMapping("/studentEnroll")
    public String studentEnrollForm() {
        return "studentEnroll";
    }

    @PostMapping("/studentEnroll")
    public String studentEnrollSubject(
                                @RequestParam("subjectId") long subjectId,
                                RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String masv = authentication.getName();
        AppUser user = appUserRepository.findByMasv(masv);
        long id = user.getId();
        // long id = 

        String enrollUrl = "http://localhost:8080/api/v1/student/" + id + "/enroll?subjectId=" + subjectId;
  
        try {
            // Thực hiện yêu cầu HTTP PUT để đăng ký môn học
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.put(enrollUrl, null);
            redirectAttributes.addFlashAttribute("message", "Đăng ký môn học thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Lỗi khi đăng ký môn học: " + e.getMessage());
            return "redirect:/";
        }
        
        return "redirect:/"; // Trả về trang HTML sau khi xử lý
    }


    @GetMapping("/listSubjectOfStudent")
    public String listSubjects(Model model) {
        // String listSubjectsUrl = "http://localhost:8080/api/v1/subject";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String masv = authentication.getName();
        AppUser user = appUserRepository.findByMasv(masv);
        long id = user.getId();
        try {
            // RestTemplate restTemplate = new RestTemplate();
            Student student = studentRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("student with id " + id + " does not exist"));
            Set<SubjectInfo> subjectsArray = student.getSubjects();
            List<SubjectInfo> subjects = new ArrayList<>();
            if (subjectsArray != null) {
                for (SubjectInfo subject : subjectsArray) {
                    subjects.add(subject);
                }
            }
            model.addAttribute("subjects", subjects);
        } catch (Exception e) {
            model.addAttribute("message", "Lỗi khi lấy danh sách môn học: " + e.getMessage());
        }
        
        return "listSubjectOfStudent";
    }

    @GetMapping("/studentDrop")
    public String studentDropform() {
        return "studentDrop";
    }

    @PostMapping("/studentDrop")
public String studentDrop(
                          @RequestParam("subjectId") long subjectId,
                          RedirectAttributes redirectAttributes) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String masv = authentication.getName();
    AppUser user = appUserRepository.findByMasv(masv);
    long id = user.getId();
    String dropUrl = "http://localhost:8080/api/v1/student/" + id + "/drop?subjectId=" + subjectId;
  
    try {
     
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(dropUrl);
        redirectAttributes.addFlashAttribute("message", "Xóa môn học thành công!");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("message", "Lỗi khi xóa môn học: " + e.getMessage());
        return "redirect:/listSubjectOfStudent";
    }
    
    return "redirect:/listSubjectOfStudent"; 
}


    @GetMapping("/update")
    public String updateForm() {
        return "update";
    }
    @PostMapping("/update")
    public String update(@RequestParam(required = false) String studentName,
                         @RequestParam(required = false) String password,
                         @RequestParam(required = false) String confirmpassword,
                         @RequestParam(required = false) String studentEmail,
                         RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String masv = authentication.getName();
        AppUser user = appUserRepository.findByMasv(masv);
        // AppUser update = new AppUser();
        long id = user.getId();
        String url = "http://localhost:8080/api/v1/student/" + id + "?name=" + studentName + "&email=" + studentEmail;
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.put(url, null);
            redirectAttributes.addAttribute("message", "Cập nhật học sinh thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Lỗi khi cập nhật học sinh: " + e.getMessage());
            return "redirect:/update";
        }
        if (studentName != null && studentName.length() > 0 && !Objects.equals(user.getName(), studentName)) {
            user.setName(studentName);  
        }
        if (studentEmail!= null && studentEmail.length() > 0 && !Objects.equals(user.getEmail(), studentEmail)) {
            user.setEmail(studentEmail);
        }
        if (password != null && !password.equals(confirmpassword)) {
            redirectAttributes.addFlashAttribute("message", "Mật khẩu và xác nhận mật khẩu không khớp");
            return "redirect:/update";
        }
    
        if (password != null && password.length()>=8) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(password));
        }
        appUserRepository.save(user);
       
       
        return "redirect:/";
    }
    

    
}
