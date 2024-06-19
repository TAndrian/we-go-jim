package com.project.we_go_jim;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WeGoJimApplicationTests {

    @LocalServerPort
    private Integer port;

    @Test
    void contextLoads() {
    }

    @Test
    void should_get_all_bookings() {

    }

    @Test
    void should_create_all_bookings() {

    }

    @Test
    void should_get_all_users() {

    }

    @Test
    void should_get_user_by_id() {

    }
}
