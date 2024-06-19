package com.project.we_go_jim.service;

import com.project.we_go_jim.mapper.UserMapper;
import com.project.we_go_jim.repository.UserRepository;
import com.project.we_go_jim.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void when_get_all_users_then_return_users() {
        // ARRANGE

        // ACT

        // ASSERT
    }

    @Test
    void given_no_users_when_get_all_users_then_return_empty_collection() {
        // ARRANGE

        // ACT

        // ASSERT
    }

    @Test
    void given_userId_when_get_user_by_id_then_return_user() {
        // ARRANGE

        // ACT

        // ASSERT
    }

    @Test
    void when_get_user_by_id_then_return_error_404() {
        // ARRANGE

        // ACT

        // ASSERT
    }

    @Test
    void given_user_to_create_when_create_user_then_create() {
        // ARRANGE

        // ACT

        // ASSERT
    }

    @Test
    void given_badly_defined_user_to_create_when_create_user_then_return_error_400() {
        // ARRANGE

        // ACT

        // ASSERT
    }
}
