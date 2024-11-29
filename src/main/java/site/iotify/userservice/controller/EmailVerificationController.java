package site.iotify.userservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.iotify.userservice.service.EmailVerificationService;

@Controller
@Slf4j
@CrossOrigin(origins = "http://localhost:8080")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    public EmailVerificationController(EmailVerificationService emailVerificationService) {
        this.emailVerificationService = emailVerificationService;
    }

    @PostMapping("/email-verify")
    public ResponseEntity<?> sendVerificationEmail(@RequestBody String email) {
        log.info("Email verification request received: {}", email);
        emailVerificationService.generateToken(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        log.info("Email verification request received: {}", token);
        boolean isVerified = emailVerificationService.verifyToken(token);
        if (isVerified) {
            log.info("Email verification success");
            return ResponseEntity.ok().build();
        } else {
            log.warn("Email verification failed");
            return ResponseEntity.badRequest().body("Email verification failed");
        }
    }

    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmailVerification(@RequestParam String email) {
        log.info("이메일 인증 확인 요청: {}", email);
        boolean isVerified = emailVerificationService.isEmailVerified(email);
        if (isVerified) {
            log.info("Email verification success");
            return ResponseEntity.ok().build();
        } else {
            log.warn("Email verification failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
