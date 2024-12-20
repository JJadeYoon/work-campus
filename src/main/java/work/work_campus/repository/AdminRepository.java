package work.work_campus.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import work.work_campus.domain.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByAdminLoginId(String adminLoginId);
}
