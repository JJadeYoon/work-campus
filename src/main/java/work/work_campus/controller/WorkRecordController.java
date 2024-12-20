package work.work_campus.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import work.work_campus.dto.request.WorkRecordCreateRequest;
import work.work_campus.dto.response.StudentResponse;
import work.work_campus.service.WorkRecordService;

@Controller
@RequestMapping("/work-records")
@RequiredArgsConstructor
public class WorkRecordController {

    private final WorkRecordService workRecordService;

    @GetMapping
    public String workRecordForm(Model model, HttpSession session) {
        StudentResponse student = (StudentResponse) session.getAttribute("student");
        if (student == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("workRecords", workRecordService.getWorkRecordsForCurrentMonth(student.getId()));
        return "work-record/form";
    }

    @PostMapping
    public String createWorkRecord(@ModelAttribute WorkRecordCreateRequest request,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        try {
            StudentResponse student = (StudentResponse) session.getAttribute("student");
            if (student == null) {
                return "redirect:/auth/login";
            }

            workRecordService.createWorkRecord(request, student.getId());
            redirectAttributes.addFlashAttribute("message", "근무 기록이 등록되었습니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/work-records";
    }
}