package work.work_campus.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import work.work_campus.domain.Admin;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminResponse {
    private Long id;
    private String adminLoginId;
    private String name;
    private String position;
    private String email;
    private String phone;

    public static AdminResponse from(Admin admin) {
        return AdminResponse.builder()
                .id(admin.getId())
                .adminLoginId(admin.getAdminLoginId())
                .name(admin.getName())
                .position(admin.getPosition())
                .email(admin.getEmail())
                .phone(admin.getPhone())
                .build();
    }
}