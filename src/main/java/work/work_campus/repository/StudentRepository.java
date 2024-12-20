package work.work_campus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import work.work_campus.domain.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByStudentId(String studentId);
}