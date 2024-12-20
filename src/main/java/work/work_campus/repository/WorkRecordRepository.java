package work.work_campus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import work.work_campus.domain.WorkRecord;

public interface WorkRecordRepository extends JpaRepository<WorkRecord, Long> {
}
