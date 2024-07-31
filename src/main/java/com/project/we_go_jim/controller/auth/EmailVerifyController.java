package com.project.we_go_jim.controller.auth;

import com.project.we_go_jim.service.EmailVerificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/verify")
@AllArgsConstructor
public class EmailVerifyController {
    private EmailVerificationService emailVerificationService;

    @PostMapping("/send-code")
    @ResponseStatus(HttpStatus.OK)
    public String sendCode(@RequestParam String toEmail) {
        return emailVerificationService.sendVerificationEmail(toEmail);
    }

    @PostMapping("/verify-code")
    @ResponseStatus(HttpStatus.OK)
    public String verifyCode(@RequestParam String toEmail, @RequestParam String code) {
        return emailVerificationService.verifyCode(toEmail, code);
    }
}
