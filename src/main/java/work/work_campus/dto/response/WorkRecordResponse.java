package work.work_campus.dto.response;

import lombok.Builder;
import lombok.Getter;
import work.work_campus.domain.WorkRecord;

import java.time.LocalDateTime;

@Getter
@Builder
public class WorkRecordResponse {
    private Long id;
    private String studentName;    // 학생 이름
    private String studentId;      // 학번
    private LocalDateTime workStart;
    private LocalDateTime workEnd;
    private Integer workMinutes;
    private String workDescription;
    private Boolean isApproved;
    private String approvedByName; // 승인자 이름

    public static WorkRecordResponse from(WorkRecord workRecord) {
        return WorkRecordResponse.builder()
                .id(workRecord.getId())
                .studentName(workRecord.getStudent().getName())
                .studentId(workRecord.getStudent().getStudentId())
                .workStart(workRecord.getWorkStart())
                .workEnd(workRecord.getWorkEnd())
                .workMinutes(workRecord.getWorkMinutes())
                .workDescription(workRecord.getWorkDescription())
                .isApproved(workRecord.getIsApproved())
                .approvedByName(workRecord.getApprovedBy() != null ?
                        workRecord.getApprovedBy().getName() : null)
                .build();
    }
}