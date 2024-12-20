package work.work_campus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import work.work_campus.domain.Department;
import work.work_campus.domain.Student;
import work.work_campus.dto.request.StudentSignUpRequest;
import work.work_campus.repository.DepartmentRepository;
import work.work_campus.repository.StudentRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public Long signUp(StudentSignUpRequest request) {
        // 1. 학번 중복 체크
        if (studentRepository.existsByStudentId(request.getStudentId())) {
            throw new IllegalArgumentException("이미 존재하는 학번입니다.");
        }

        // 2. 부서 조회
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부서입니다."));

        // 3. 학생 엔티티 생성 및 저장
        Student student = Student.builder()
                .studentId(request.getStudentId())
                .password(request.getPassword())  // 실제 서비스에서는 암호화 필요
                .name(request.getName())
                .email(request.getEmail())
                .monthlyTargetHours(request.getMonthlyTargetHours())
                .department(department)
                .build();

        Student savedStudent = studentRepository.save(student);
        return savedStudent.getId();
    }
}