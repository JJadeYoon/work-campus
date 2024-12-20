package work.work_campus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import work.work_campus.domain.Student;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByStudentId(String studentId);
    Optional<Student> findByStudentId(String studentId); // 추가
}