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
import work.work_campus.dto.request.LoginRequest;
import work.work_campus.dto.request.StudentSignUpRequest;
import work.work_campus.dto.response.StudentResponse;
import work.work_campus.repository.DepartmentRepository;
import work.work_campus.service.AuthService;

// controller/AuthController.java
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final DepartmentRepository departmentRepository; // 부서 목록을 위해 추가

    // 회원가입 페이지 보여주기
    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("departments", departmentRepository.findAll());
        return "auth/signup";
    }

    // 회원가입 처리
    @PostMapping("/signup")
    public String signup(@ModelAttribute StudentSignUpRequest request, RedirectAttributes redirectAttributes) {
        try {
            authService.signUp(request);
            redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다.");
            return "redirect:/auth/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/signup";
        }
    }

    // 로그인 페이지 보여주기
    @GetMapping("/login")
    public String loginForm() {
        return "auth/login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest request, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            StudentResponse response = authService.login(request);
            session.setAttribute("student", response);
            return "redirect:/student/dashboard"; // 로그인 후 근무기록 페이지로 이동
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/login";
        }
    }
}