package com.example.demo.student;

// import org.hibernate.mapping.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudenConfig {
    @Bean
    CommandLineRunner commandLineRunner(StudentRepository repository){
        return args ->{
          // Student Hoa =  new Student(1L,"Hoa", "Hoa@gmail.com", LocalDate.of(2000,Month.JULY,5));
          // Student Hoang =  new Student("Hoang", "Hoang@gmail.com", LocalDate.of(2002,Month.JANUARY,12));
          // repository.saveAll(List.of(Hoa,Hoang));
        };
    }

}
