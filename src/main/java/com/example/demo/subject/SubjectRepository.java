package com.example.demo.subject;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface SubjectRepository extends JpaRepository<Subject,Long>{
    @Query("SELECT s FROM Subject s WHERE LOWER(s.name) = LOWER(:name)")
    Optional<Subject> findSubjectByName(@Param("name") String name);
}
