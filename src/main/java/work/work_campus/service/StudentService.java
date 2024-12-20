package work.work_campus.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import work.work_campus.domain.Student;
import work.work_campus.domain.WorkRecord;
import work.work_campus.dto.response.MonthlyWorkSummary;
import work.work_campus.repository.StudentRepository;
import work.work_campus.repository.WorkRecordRepository;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final WorkRecordRepository workRecordRepository;

    public MonthlyWorkSummary getMonthlyWorkSummary(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));

        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.now().plusMonths(1).withDayOfMonth(1).atStartOfDay();

        List<WorkRecord> monthlyRecords = workRecordRepository
                .findByStudentIdAndWorkStartBetween(studentId, startOfMonth, endOfMonth);

        int workedMinutes = monthlyRecords.stream()
                .mapToInt(WorkRecord::getWorkMinutes)
                .sum();

        int approvedMinutes = monthlyRecords.stream()
                .filter(WorkRecord::getIsApproved)
                .mapToInt(WorkRecord::getWorkMinutes)
                .sum();

        int targetMinutes = student.getMonthlyTargetHours() * 60;
        int remainingMinutes = targetMinutes - approvedMinutes;

        return MonthlyWorkSummary.builder()
                .targetHours(student.getMonthlyTargetHours())
                .workedMinutes(workedMinutes)
                .remainingMinutes(remainingMinutes)
                .approvedMinutes(approvedMinutes)
                .pendingMinutes(workedMinutes - approvedMinutes)
                .monthDisplay(String.format("%d년 %d월",
                        LocalDate.now().getYear(),
                        LocalDate.now().getMonthValue()))
                .build();
    }
}