package work.work_campus.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import work.work_campus.domain.Department;
import work.work_campus.domain.Student;
import work.work_campus.dto.request.LoginRequest;
import work.work_campus.dto.request.StudentSignUpRequest;
import work.work_campus.dto.response.StudentResponse;
import work.work_campus.repository.DepartmentRepository;
import work.work_campus.repository.StudentRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final HttpSession httpSession;

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

    @Transactional(readOnly = true)
    public StudentResponse login(LoginRequest request) {
        // 1. 학번으로 학생 조회
        Student student = studentRepository.findByStudentId(request.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학번입니다."));

        // 2. 비밀번호 확인
        if (!student.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3. 세션에 학생 정보 저장
        httpSession.setAttribute("STUDENT_ID", student.getId());

        // 4. 학생 정보 반환
        return StudentResponse.from(student);
    }

    // 로그아웃
    public void logout() {
        httpSession.invalidate();
    }
}