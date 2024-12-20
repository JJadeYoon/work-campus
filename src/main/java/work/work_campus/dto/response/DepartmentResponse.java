package work.work_campus.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import work.work_campus.domain.Department;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentResponse {
    private Long id;
    private String departmentName;
    private String location;
    private String adminName;  // 담당 관리자 이름

    public static DepartmentResponse from(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .departmentName(department.getDepartmentName())
                .location(department.getLocation())
                .adminName(department.getAdmin() != null ? department.getAdmin().getName() : null)
                .build();
    }
}