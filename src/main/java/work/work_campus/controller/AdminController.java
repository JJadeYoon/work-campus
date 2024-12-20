// controller/AdminController.java
package work.work_campus.controller;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import work.work_campus.dto.request.AdminLoginRequest;
import work.work_campus.dto.request.DepartmentCreateRequest;
import work.work_campus.dto.response.AdminResponse;
import work.work_campus.dto.response.DepartmentResponse;
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
        System.out.println("model = " + model + ", session = " + session);
        AdminResponse admin = (AdminResponse) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("workRecords", adminService.getPendingWorkRecords(admin.getId()));
        return "admin/dashboard";
    }

    @GetMapping("/departments")
    public String departments(Model model, HttpSession session) {
        AdminResponse admin = (AdminResponse) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        List<DepartmentResponse> departments = adminService.getAllDepartments();
        model.addAttribute("departments", departments);

        return "admin/departments";
    }

    // 부서 추가 페이지
    @GetMapping("/departments/add")
    public String departmentAddForm(HttpSession session) {
        AdminResponse admin = (AdminResponse) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin/login";
        }
        return "admin/department-add";
    }

    // 부서 추가 처리
    @PostMapping("/departments")
    public String addDepartment(@ModelAttribute DepartmentCreateRequest request,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        AdminResponse admin = (AdminResponse) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        try {
            adminService.createDepartment(request, admin.getId());
            redirectAttributes.addFlashAttribute("message", "부서가 성공적으로 추가되었습니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/departments";
    }

    @PostMapping("/departments/{id}/delete")
    public String deleteDepartment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            adminService.deleteDepartment(id);
            redirectAttributes.addFlashAttribute("message", "부서가 성공적으로 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/departments";
    }

    @PostMapping("/approve-work")
    public String approveWorkRecord(@RequestParam Long recordId,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        try {
            AdminResponse admin = (AdminResponse) session.getAttribute("admin");
            if (admin == null) {
                return "redirect:/admin/login";
            }

            adminService.approveWorkRecord(recordId, admin.getId());
            redirectAttributes.addFlashAttribute("message", "근무 기록이 승인되었습니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }
}