package work.work_campus.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import work.work_campus.dto.response.MonthlyWorkSummary;
import work.work_campus.dto.response.StudentResponse;
import work.work_campus.service.StudentService;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        StudentResponse student = (StudentResponse) session.getAttribute("student");
        if (student == null) {
            return "redirect:/auth/login";
        }

        // 이번 달 근무 시간 정보 조회
        MonthlyWorkSummary monthlyWorkSummary = studentService.getMonthlyWorkSummary(student.getId());
        model.addAttribute("workSummary", monthlyWorkSummary);

        return "student/dashboard";
    }
}