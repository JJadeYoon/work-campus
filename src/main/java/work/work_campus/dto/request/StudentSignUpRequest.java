package work.work_campus.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StudentSignUpRequest {
    private String studentId;      // 학번
    private String password;
    private String name;
    private String email;
    private Integer monthlyTargetHours;
    private Long departmentId;     // 소속 부서 ID
}