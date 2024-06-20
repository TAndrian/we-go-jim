package com.project.we_go_jim;

import com.project.we_go_jim.dto.UserDTO;
import com.project.we_go_jim.mapper.UserMapper;
import com.project.we_go_jim.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.project.we_go_jim.controller.ResourcesPath.API_USER;
import static com.project.we_go_jim.controller.ResourcesPath.API_USERS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WeGoJimApplicationTests {

    @LocalServerPort
    private Integer port;

    private String baseUrl = "http://localhost";

    private static HttpHeaders headers;
    private static RestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void contextLoads() {
    }

    @Test
    void should_get_all_bookings() {
        // ARRANGE

        // ACT

        // ASSERT
    }

    @Test
    void should_create_all_bookings() {
        // ARRANGE

        // ACT

        // ASSERT
    }

    @Test
    void should_get_all_users() {
        // ARRANGE
        baseUrl = baseUrl.concat(":").concat(port + "").concat(API_USERS);

        // ACT
        HttpEntity<UserDTO[]> entity = new HttpEntity<>(headers);
        ResponseEntity<UserDTO[]> response =
                restTemplate.exchange(baseUrl, HttpMethod.GET, entity, UserDTO[].class);

        List<UserDTO> expected = List.of(Objects.requireNonNull(response.getBody()));
        List<UserDTO> userDTOS = userMapper.toDTOs(userRepository.findAll());

        // ASSERT
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertThat(userDTOS).isEqualTo(expected)
        );
    }

    @Test
    void should_get_user_by_id() {
        // ARRANGE
        UUID userId = UUID.randomUUID();
        baseUrl = baseUrl.concat(":")
                .concat(port + "")
                .concat(API_USER)
                .concat("/" + userId);

        // ACT
        HttpEntity<UserDTO> entity = new HttpEntity<>(headers);
        ResponseEntity<UserDTO> response =
                restTemplate.exchange(baseUrl, HttpMethod.GET, entity, UserDTO.class);

        UserDTO expectedUser = response.getBody();
        UserDTO userDTO = userMapper.toDTO(
                userRepository.findById(userId)
                        .orElseThrow(() -> new EntityNotFoundException("UserNotFound")));

        // ASSERT
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(expectedUser, userDTO)
        );
    }
}
