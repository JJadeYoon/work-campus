package work.work_campus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import work.work_campus.domain.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}