package work.work_campus.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import work.work_campus.domain.Student;
import work.work_campus.domain.WorkRecord;
import work.work_campus.dto.request.WorkRecordCreateRequest;
import work.work_campus.dto.response.WorkRecordResponse;
import work.work_campus.repository.StudentRepository;
import work.work_campus.repository.WorkRecordRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkRecordService {

    private final WorkRecordRepository workRecordRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public void createWorkRecord(WorkRecordCreateRequest request, Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));

        // 시작 시간과 종료 시간 파싱
        LocalDateTime startTime = LocalDateTime.parse(request.getWorkStart());
        LocalDateTime endTime = LocalDateTime.parse(request.getWorkEnd());

        // 유효성 검사
        validateWorkTime(startTime, endTime, student);

        // 근무 시간 계산 (분 단위)
        long workMinutes = ChronoUnit.MINUTES.between(startTime, endTime);

        WorkRecord workRecord = WorkRecord.builder()
                .student(student)
                .workStart(startTime)
                .workEnd(endTime)
                .workMinutes((int) workMinutes)
                .workDescription(request.getWorkDescription())
                .isApproved(false)
                .build();

        workRecordRepository.save(workRecord);
    }

    private void validateWorkTime(LocalDateTime startTime, LocalDateTime endTime, Student student) {
        // 시작 시간이 종료 시간보다 늦은 경우
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("시작 시간이 종료 시간보다 늦을 수 없습니다.");
        }

        // 하루 최대 근무시간(8시간) 체크
        LocalDate workDate = startTime.toLocalDate();
        int existingMinutesForDay = workRecordRepository
                .findByStudentAndWorkStartBetween(
                        student,
                        workDate.atStartOfDay(),
                        workDate.plusDays(1).atStartOfDay()
                )
                .stream()
                .mapToInt(WorkRecord::getWorkMinutes)
                .sum();

        int newWorkMinutes = (int) ChronoUnit.MINUTES.between(startTime, endTime);
        if (existingMinutesForDay + newWorkMinutes > 480) { // 8시간 = 480분
            throw new IllegalArgumentException("하루 근무시간이 8시간을 초과할 수 없습니다.");
        }
    }

    public List<WorkRecordResponse> getWorkRecordsForCurrentMonth(Long studentId) {
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.now().plusMonths(1).withDayOfMonth(1).atStartOfDay();

        return workRecordRepository
                .findByStudentIdAndWorkStartBetweenOrderByWorkStartDesc(
                        studentId, startOfMonth, endOfMonth)
                .stream()
                .map(WorkRecordResponse::from)
                .collect(Collectors.toList());
    }
}