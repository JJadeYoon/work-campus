package work.work_campus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.work_campus.dto.request.LoginRequest;
import work.work_campus.dto.request.StudentSignUpRequest;
import work.work_campus.dto.response.StudentResponse;
import work.work_campus.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Long> signUp(@RequestBody StudentSignUpRequest request) {
        Long studentId = authService.signUp(request);
        return ResponseEntity.ok(studentId);
    }

    @PostMapping("/login")
    public ResponseEntity<StudentResponse> login(@RequestBody LoginRequest request) {
        StudentResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        authService.logout();
        return ResponseEntity.ok().build();
    }
}