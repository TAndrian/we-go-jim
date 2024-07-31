package com.project.we_go_jim.service.impl;

import com.project.we_go_jim.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {
    private static final long CODE_EXPIRATION_MINUTES = 10;
    private static final String VERIFICATION_CODE = "verification_code:";

    private static final Random random = new Random();

    private final RedisTemplate<String, String> redisTemplate;

    private final JavaMailSender mailSender;


    @Override
    public String sendVerificationEmail(String toEmail) {
        String verificationCode = generateVerificationCode();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Confirm email");
        message.setText("Please, confirm your email with the following verification code : " + verificationCode);
        mailSender.send(message);
        storeVerificationCode(toEmail, verificationCode);
        return "Verification code sent to  " + toEmail;
    }

    /**
     * Store the verification code corresponding to the
     *
     * @param toEmail recipient's email.
     * @param code    verification code.
     */
    private void storeVerificationCode(String toEmail, String code) {
        String key = VERIFICATION_CODE + toEmail;
        redisTemplate.opsForValue().set(key, code, CODE_EXPIRATION_MINUTES, TimeUnit.MINUTES);
    }

    @Override
    public String verifyCode(String toEmail, String code) {
        boolean isCodeValid = checkCode(toEmail, code);
        if (!isCodeValid) {
            return "Verification code not valid.";
        }
        removeCode(toEmail);
        return "Email verified.";
    }

    /**
     * Verify if the code sent by the recipient matches the verification code.
     *
     * @param toEmail recipient's email.
     * @param code    verification code.
     * @return true if the code matches.
     */
    private boolean checkCode(String toEmail, String code) {
        String key = VERIFICATION_CODE + toEmail;
        String storedCode = redisTemplate.opsForValue().get(key);
        return code.equals(storedCode);
    }


    /**
     * Remove verification code and recipient's email from redis.
     *
     * @param toEmail recipient's email.
     */
    private void removeCode(String toEmail) {
        String key = VERIFICATION_CODE + toEmail;
        redisTemplate.delete(key);
    }

    /**
     * Generate a verification code of 6 digits.
     *
     * @return verification code.
     */
    private String generateVerificationCode() {
        int code = random.nextInt(999999);
        return String.format("%06d", code);
    }
}
