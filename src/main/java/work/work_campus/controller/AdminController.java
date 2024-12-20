// controller/AdminController.java
package work.work_campus.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import work.work_campus.dto.request.AdminLoginRequest;
import work.work_campus.dto.response.AdminResponse;
import work.work_campus.service.AdminService;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/login")
    public String loginForm() {
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute AdminLoginRequest request, HttpSession session) {
        try {
            System.out.println("request = " + request.getAdminLoginId());
            AdminResponse response = adminService.login(request);
            session.setAttribute("admin", response);
            return "redirect:/admin/dashboard";
        } catch (IllegalArgumentException e) {
            return "redirect:/admin/login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        AdminResponse admin = (AdminResponse) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("workRecords", adminService.getPendingWorkRecords(admin.getId()));
        return "admin/dashboard";
    }
}