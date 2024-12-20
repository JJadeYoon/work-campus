package work.work_campus.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import work.work_campus.domain.Admin;
import work.work_campus.domain.Department;
import work.work_campus.domain.WorkRecord;
import work.work_campus.dto.request.AdminLoginRequest;
import work.work_campus.dto.request.DepartmentCreateRequest;
import work.work_campus.dto.response.AdminResponse;
import work.work_campus.dto.response.DepartmentResponse;
import work.work_campus.dto.response.WorkRecordResponse;
import work.work_campus.repository.AdminRepository;
import work.work_campus.repository.DepartmentRepository;
import work.work_campus.repository.WorkRecordRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {
    private final AdminRepository adminRepository;
    private final WorkRecordRepository workRecordRepository;
    private final DepartmentRepository departmentRepository;

    public AdminResponse login(AdminLoginRequest request) {
        Admin admin = adminRepository.findByAdminLoginId(request.getAdminLoginId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자입니다."));

        if (!admin.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return AdminResponse.from(admin);
    }

    public List<WorkRecordResponse> getPendingWorkRecords(Long adminId) {
        return workRecordRepository.findByApprovedByIdAndIsApprovedIsFalse(adminId).stream()
                .map(WorkRecordResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void createDepartment(DepartmentCreateRequest request, Long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("관리자를 찾을 수 없습니다."));

        Department department = Department.builder()
                .departmentName(request.getDepartmentName())
                .location(request.getLocation())
                .admin(admin)
                .build();

        departmentRepository.save(department);
    }

    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(DepartmentResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부서입니다."));

        if (!department.getStudents().isEmpty()) {
            throw new IllegalArgumentException("소속된 학생이 있는 부서는 삭제할 수 없습니다.");
        }

        departmentRepository.delete(department);
    }


    @Transactional
    public void approveWorkRecord(Long recordId, Long adminId) {
        WorkRecord workRecord = workRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 근무 기록입니다."));

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("관리자 정보를 찾을 수 없습니다."));

        // 이미 승인된 기록인지 확인
        if (workRecord.getIsApproved()) {
            throw new IllegalArgumentException("이미 승인된 근무 기록입니다.");
        }

        // 해당 부서의 관리자인지 확인
        if (!workRecord.getStudent().getDepartment().getAdmin().getId().equals(adminId)) {
            throw new IllegalArgumentException("해당 부서의 관리자만 승인할 수 있습니다.");
        }

        // 승인 처리
        workRecord.approve(admin);
        workRecordRepository.save(workRecord);
    }

//    // 대시보드에 표시할 승인 대기 중인 근무 기록 조회
//    public List<WorkRecordResponse> getPendingWorkRecords(Long adminId) {
//        return workRecordRepository.findByStudentDepartmentAdminIdAndIsApprovedIsFalseOrderByWorkStartDesc(adminId)
//                .stream()
//                .map(WorkRecordResponse::from)
//                .collect(Collectors.toList());
//    }
}