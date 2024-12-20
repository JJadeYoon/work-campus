package work.work_campus.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MonthlyWorkSummary {
    private int targetHours;           // 목표 시간
    private int workedMinutes;         // 현재까지 근무한 시간(분)
    private int remainingMinutes;      // 남은 시간(분)
    private int approvedMinutes;       // 승인된 시간(분)
    private int pendingMinutes;        // 승인 대기 시간(분)
    private String monthDisplay;        // "2024년 12월" 형식

    public int getWorkedHours() {
        return workedMinutes / 60;
    }

    public int getWorkedMinutesRemainder() {
        return workedMinutes % 60;
    }

    public int getRemainingHours() {
        return remainingMinutes / 60;
    }

    public int getRemainingMinutesRemainder() {
        return remainingMinutes % 60;
    }
}