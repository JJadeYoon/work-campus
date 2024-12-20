package work.work_campus.dto.response;

import lombok.Builder;
import lombok.Getter;
import work.work_campus.domain.Admin;

@Getter
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