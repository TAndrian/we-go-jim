package com.project.we_go_jim.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.we_go_jim.config.JwtAuthenticationFilter;
import com.project.we_go_jim.service.EmailVerificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.project.we_go_jim.util.EmailVerificationMock.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(EmailVerifyController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EmailVerifyControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailVerificationService emailVerificationService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void when_sendVerificationCode_then_success() throws Exception {
        // ARRANGE
        final String url = "/api/v1/verify/send-code";

        when(emailVerificationService.sendVerificationEmail(TO_EMAIL))
                .thenReturn(EMAIL_SENT_SUCCESSFULLY + TO_EMAIL);

        // ACT
        mockMvc.perform(post(url)
                        .param("toEmail", TO_EMAIL))
                .andExpect(status().isOk())
                .andExpect(content().string(EMAIL_SENT_SUCCESSFULLY + TO_EMAIL));

        // ASSERT
        verify(emailVerificationService, times(1)).sendVerificationEmail(TO_EMAIL);
    }

    @Test
    void when_verify_then_return_invalid_code_message() throws Exception {
        // ARRANGE
        final String url = "/api/v1/verify/verify-code";
        String invalidCode = "123";

        when(emailVerificationService.verifyCode(TO_EMAIL, invalidCode))
                .thenReturn(VERIFICATION_CODE_NOT_VALID);

        // ACT
        mockMvc.perform(post(url)
                        .param("toEmail", TO_EMAIL)
                        .param("code", invalidCode))
                .andExpect(status().isOk())
                .andExpect(content().string(VERIFICATION_CODE_NOT_VALID));

        // ASSERT
        verify(emailVerificationService, times(1)).verifyCode(TO_EMAIL, invalidCode);
    }

    @Test
    void when_verify_then_return_code_message() throws Exception {
        // ARRANGE
        final String url = "/api/v1/verify/verify-code";

        when(emailVerificationService.verifyCode(TO_EMAIL, MOCK_CODE))
                .thenReturn(EMAIL_VERIFIED);

        // ACT
        mockMvc.perform(post(url)
                        .param("toEmail", TO_EMAIL)
                        .param("code", MOCK_CODE))
                .andExpect(status().isOk())
                .andExpect(content().string(EMAIL_VERIFIED));

        // ASSERT
        verify(emailVerificationService, times(1)).verifyCode(TO_EMAIL, MOCK_CODE);
    }
}
