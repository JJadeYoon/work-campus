package work.work_campus.repository;

import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import work.work_campus.domain.WorkRecord;

public interface WorkRecordRepository extends JpaRepository<WorkRecord, Long> {
    List<WorkRecord> findByApprovedByIdAndIsApprovedIsFalse(Long adminId);
}
