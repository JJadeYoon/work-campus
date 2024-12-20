package work.work_campus.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import work.work_campus.domain.Admin;
import work.work_campus.domain.Department;
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
        System.out.println("admin = " + admin);

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
}