package work.work_campus.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WorkRecordCreateRequest {
    private String workStart;    // "2024-12-21T14:30" 형식
    private String workEnd;      // "2024-12-21T18:30" 형식
    private String workDescription;
}