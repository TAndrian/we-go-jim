package com.project.we_go_jim.service;

public interface EmailVerificationService {
    /**
     * Send verification code to the recipient's email.
     *
     * @param toEmail recipient's email.
     * @return Verification code.
     */
    String sendVerificationEmail(String toEmail);
    
    /**
     * Verify verification code.
     *
     * @param toEmail recipient's email.
     * @param code    verification code.
     * @return true if the code matches.
     */
    String verifyCode(String toEmail, String code);
}
