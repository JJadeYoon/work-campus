package work.work_campus.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import work.work_campus.domain.Student;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse {
    private Long id;
    private String studentId;
    private String name;
    private String email;
    private Integer monthlyTargetHours;
    private String departmentName;

    public static StudentResponse from(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .studentId(student.getStudentId())
                .name(student.getName())
                .email(student.getEmail())
                .monthlyTargetHours(student.getMonthlyTargetHours())
                .departmentName(student.getDepartment().getDepartmentName())
                .build();
    }
}