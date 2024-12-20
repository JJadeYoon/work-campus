package work.work_campus.repository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import work.work_campus.domain.Student;
import work.work_campus.domain.WorkRecord;

public interface WorkRecordRepository extends JpaRepository<WorkRecord, Long> {
    List<WorkRecord> findByApprovedByIdAndIsApprovedIsFalse(Long adminId);

    List<WorkRecord> findByStudentAndWorkStartBetween(Student student, LocalDateTime localDateTime, LocalDateTime localDateTime1);

    List<WorkRecord> findByStudentIdAndWorkStartBetweenOrderByWorkStartDesc(Long studentId, LocalDateTime startOfMonth, LocalDateTime endOfMonth);
}
