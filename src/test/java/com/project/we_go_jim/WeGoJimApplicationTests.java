package com.project.we_go_jim;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.we_go_jim.dto.BookingDTO;
import com.project.we_go_jim.dto.CreateBookingDTO;
import com.project.we_go_jim.dto.UserBookingHistoryDTO;
import com.project.we_go_jim.dto.UserDTO;
import com.project.we_go_jim.mapper.BookingMapper;
import com.project.we_go_jim.mapper.UserMapper;
import com.project.we_go_jim.repository.BookingRepository;
import com.project.we_go_jim.repository.UserRepository;
import com.project.we_go_jim.util.BookingMock;
import com.project.we_go_jim.util.DateUtils;
import com.project.we_go_jim.util.DbCommonOperation;
import com.project.we_go_jim.util.UserMock;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static com.project.we_go_jim.controller.ResourcesPath.API_BOOKING;
import static com.project.we_go_jim.controller.ResourcesPath.API_BOOKINGS;
import static com.project.we_go_jim.controller.ResourcesPath.API_USER;
import static com.project.we_go_jim.controller.ResourcesPath.API_USERS;
import static com.project.we_go_jim.controller.ResourcesPath.USER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class WeGoJimApplicationTests {

    public static final UUID JOHN_ID = UserMock.JOHN_ID;
    public static final UUID BOOKING_ID = UUID.fromString("3fa0e077-690e-4847-89b5-b3881534af3f");
    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static HttpHeaders headers;
    private static RestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private DbCommonOperation dbCommonOperation;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @BeforeEach
    public void setUp() {
        dbCommonOperation.initializeTestData();
    }

    @AfterEach
    public void cleanup() {
        dbCommonOperation.cleanUp();
    }

    @Test
    void should_get_all_bookings() {
        // ARRANGE
        baseUrl = baseUrl.concat(":").concat(port + "/").concat(API_BOOKINGS);

        // ACT
        HttpEntity<BookingDTO[]> entity = new HttpEntity<>(headers);
        ResponseEntity<BookingDTO[]> response =
                restTemplate.exchange(baseUrl, HttpMethod.GET, entity, BookingDTO[].class);

        Set<BookingDTO> expected = Set.of(Objects.requireNonNull(response.getBody()));
        Set<BookingDTO> bookings = bookingMapper.toDTOs(bookingRepository.findAll());

        // ASSERT
        assertAll(
                () -> assertEquals(bookings, expected),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode())
        );
    }

    @Test
    void should_get_all_users() {
        // ARRANGE
        baseUrl = baseUrl.concat(":").concat(port + "/").concat(API_USERS);

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
        baseUrl = baseUrl.concat(":")
                .concat(port + "/")
                .concat(API_USER)
                .concat("/")
                .concat(JOHN_ID.toString());

        // ACT
        HttpEntity<UserDTO> entity = new HttpEntity<>(headers);
        ResponseEntity<UserDTO> response =
                restTemplate.exchange(baseUrl, HttpMethod.GET, entity, UserDTO.class);

        UserDTO expectedUser = response.getBody();
        UserDTO userDTO = userMapper.toDTO(
                userRepository.findById(JOHN_ID)
                        .orElseThrow(() -> new EntityNotFoundException("UserNotFound")));

        // ASSERT
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(expectedUser, userDTO)
        );
    }

    @Test
    void should_get_bookings_by_user_id() {
        // ARRANGE

        baseUrl = baseUrl.concat(":")
                .concat(port + "/")
                .concat(API_BOOKINGS + "/")
                .concat(USER + "/" + JOHN_ID);

        // ACT
        HttpEntity<UserBookingHistoryDTO[]> entity = new HttpEntity<>(headers);
        ResponseEntity<UserBookingHistoryDTO[]> response =
                restTemplate.exchange(baseUrl, HttpMethod.GET, entity, UserBookingHistoryDTO[].class);

        Set<UserBookingHistoryDTO> responseBody = Set.of(Objects.requireNonNull(response.getBody()));
        Set<UserBookingHistoryDTO> userBookingHistoryDTOs =
                bookingMapper.toUserBookingHistoryDTOs(bookingRepository.findByUsers_Id(JOHN_ID));

        // ASSERT
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertThat(userBookingHistoryDTOs)
                        .isEqualTo(responseBody)
        );
    }

    @Test
    void should_get_booking_by_bookingId() {
        // ARRANGE

        baseUrl = baseUrl.concat(":")
                .concat(port + "/")
                .concat(API_BOOKING + "/")
                .concat(BOOKING_ID.toString());

        // ACT
        HttpEntity<BookingDTO> entity = new HttpEntity<>(headers);
        ResponseEntity<BookingDTO> response =
                restTemplate.exchange(baseUrl, HttpMethod.GET, entity, BookingDTO.class);

        BookingDTO bookingDTO = bookingMapper.toDTO(bookingRepository.findById(BOOKING_ID).orElseThrow());
        BookingDTO expected = response.getBody();

        // ASSERT
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertThat(bookingDTO)
                        .isEqualTo(expected)
        );
    }

    @Test
    void should_create_booking_for_user() {
        // ARRANGE

        baseUrl = baseUrl.concat(":")
                .concat(port + "/")
                .concat(API_BOOKING + "/")
                .concat(USER + "/" + JOHN_ID);

        CreateBookingDTO createBookingDTO = BookingMock.createBookingDTO();

        // ACT
        ResponseEntity<BookingDTO> response =
                restTemplate.postForEntity(baseUrl, createBookingDTO, BookingDTO.class);

        BookingDTO expected = response.getBody();
        assert expected != null;
        BookingDTO bookingDTO = bookingMapper.toDTO(bookingRepository.findById(expected.getId()).orElseThrow());

        bookingDTO.setStartTime(DateUtils.formatDate(bookingDTO.getStartTime()));
        bookingDTO.setEndTime(DateUtils.formatDate(bookingDTO.getEndTime()));

        expected.setStartTime(DateUtils.formatDate(expected.getStartTime()));
        expected.setEndTime(DateUtils.formatDate(expected.getEndTime()));

        // ASSERT
        assertAll(
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertThat(bookingDTO).isEqualTo(expected)
        );
    }

    @Test
    void should_add_user_to_booking() {
        // ARRANGE

        baseUrl = baseUrl.concat(":")
                .concat(port + "/")
                .concat(API_BOOKING + "/" + BOOKING_ID + "/")
                .concat(USER + "/" + JOHN_ID);

        // ACT
        HttpEntity<BookingDTO> entity = new HttpEntity<>(headers);
        ResponseEntity<BookingDTO> response =
                restTemplate.postForEntity(baseUrl, entity, BookingDTO.class);

        BookingDTO expected = response.getBody();
        BookingDTO bookingDTO = bookingMapper.toDTO(bookingRepository.findById(BOOKING_ID).orElseThrow());
        UserDTO userDTO = userMapper.toDTO(userRepository.findById(JOHN_ID).orElseThrow());

        // Format date.
        bookingDTO.setStartTime(DateUtils.formatDate(bookingDTO.getStartTime()));
        bookingDTO.setEndTime(DateUtils.formatDate(bookingDTO.getEndTime()));

        assert expected != null;
        expected.setStartTime(DateUtils.formatDate(expected.getStartTime()));
        expected.setEndTime(DateUtils.formatDate(expected.getEndTime()));

        Set<UserDTO> expectedUsers = expected.getUsers();

        // ASSERT
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertThat(bookingDTO).isEqualTo(expected),
                () -> assertTrue(expectedUsers.contains(userDTO))
        );
    }
}
