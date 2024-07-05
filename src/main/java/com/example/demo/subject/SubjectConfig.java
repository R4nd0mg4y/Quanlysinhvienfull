package com.example.demo.subject;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class SubjectConfig {
    @Bean
    CommandLineRunner commandLineRunner2(SubjectRepository repository){
        return args ->{
          // Subject Toan = new Subject("Toan", 100);
          // Subject TiengAnh = new Subject("TiengAnh",150);
          // repository.saveAll(List.of(Toan,TiengAnh));
        };
    }
}
