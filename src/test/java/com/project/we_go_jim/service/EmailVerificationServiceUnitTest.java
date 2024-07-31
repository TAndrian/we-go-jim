package com.project.we_go_jim.service;

import com.project.we_go_jim.config.RedisConfig;
import com.project.we_go_jim.service.impl.EmailVerificationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Import({RedisConfig.class})
@ExtendWith(MockitoExtension.class)
public class EmailVerificationServiceUnitTest {

    public static final String EMAIL_BODY = "Please, confirm your email with the following verification code : ";
    public static final String VERIFICATION_CODE = "verification_code:";
    public static final String EMAIL_SENT_SUCCESSFULLY = "Verification code sent to  ";
    public static final String EMAIL_SUBJECT = "Confirm email";
    public static final String EMAIL_VERIFIED = "Email verified.";
    public static final String VERIFICATION_CODE_NOT_VALID = "Verification code not valid.";
    public static final String MOCK_CODE = "123456";
    public static final String TO_EMAIL = "test@example.com";
    @InjectMocks
    @Spy
    private EmailVerificationServiceImpl emailVerificationService;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private ValueOperations<String, String> valueOps;


    @Test
    void sendVerificationEmail() {
        // ARRANGE

        when(redisTemplate.opsForValue()).thenReturn(valueOps);

        // Mocking the value operations for setting the verification code
        doNothing().when(valueOps).set(anyString(), anyString(), anyLong(), any(TimeUnit.class));

        // Capturing the SimpleMailMessage object
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        doNothing().when(mailSender).send(messageCaptor.capture());

        // Act
        String expected = emailVerificationService.sendVerificationEmail(TO_EMAIL);

        SimpleMailMessage sentMessage = messageCaptor.getValue();

        // ASSERT
        assertAll(
                // Verify the interactions and capture the values
                () -> verify(mailSender, times(1)).send(any(SimpleMailMessage.class)),
                () -> assertEquals(TO_EMAIL, Objects.requireNonNull(sentMessage.getTo())[0]),
                () -> assertEquals(EMAIL_SUBJECT, sentMessage.getSubject()),
                () -> assertTrue(Objects.requireNonNull(sentMessage.getText()).contains(EMAIL_BODY)),

                // Verifying the code storage
                () -> verify(valueOps, times(1))
                        .set(eq(VERIFICATION_CODE + TO_EMAIL), anyString(), eq(10L), eq(TimeUnit.MINUTES)),

                () -> assertEquals(EMAIL_SENT_SUCCESSFULLY + TO_EMAIL, expected)
        );
    }

    @Test
    void verifyCode_validCode() {

        // Mocking the stored code
        when(redisTemplate.opsForValue()).thenReturn(valueOps);

        when(valueOps.get(VERIFICATION_CODE + TO_EMAIL)).thenReturn(MOCK_CODE);

        // Execute the method
        String response = emailVerificationService.verifyCode(TO_EMAIL, MOCK_CODE);

        // Verify the interactions
        verify(redisTemplate, times(1)).delete(VERIFICATION_CODE + TO_EMAIL);

        // Assert the response
        assertEquals(EMAIL_VERIFIED, response);
    }

    @Test
    void verifyCode_invalidCode() {
        String invalidCode = "654321";

        when(redisTemplate.opsForValue()).thenReturn(valueOps);

        // Mocking the stored code
        when(valueOps.get(VERIFICATION_CODE + TO_EMAIL)).thenReturn(MOCK_CODE);

        // Execute the method with an invalid code
        String response = emailVerificationService.verifyCode(TO_EMAIL, invalidCode);

        // Assert the response
        assertEquals(VERIFICATION_CODE_NOT_VALID, response);
    }
}
