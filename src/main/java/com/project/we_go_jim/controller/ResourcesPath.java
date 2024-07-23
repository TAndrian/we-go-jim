package com.project.we_go_jim.controller;

public class ResourcesPath {
    public static final String BASE_URL = "api/v1/";
    public static final String BOOKINGS = "bookings";
    public static final String BOOKING = "booking";
    public static final String USERS = "users";
    public static final String USER = "user";

    public static final String UUID_PATTERN = "^[0-9a-fA-F\\-]{36}$";
    public static final String PATH_USER_ID = "/{userId:" + UUID_PATTERN + "}";
    public static final String PATH_BOOKING_ID = "/{bookingId:" + UUID_PATTERN + "}";

    public static final String API_BOOKINGS = BASE_URL + BOOKINGS;
    public static final String API_USERS = BASE_URL + USERS;
    public static final String API_USER = BASE_URL + USER;
    public static final String API_BOOKING = BASE_URL + BOOKING;

    public static final String API_USER_BY_ID = API_USER + PATH_USER_ID;
    public static final String API_BOOKING_BY_ID = API_BOOKING + PATH_BOOKING_ID;
    public static final String API_BOOKINGS_USER_BY_ID = API_BOOKINGS + "/" + USER + PATH_USER_ID;
    public static final String API_ADD_BOOKING_TO_USER = API_BOOKING_BY_ID + "/" + USER + PATH_USER_ID;
    public static final String API_CREATE_BOOKING_FOR_USER = API_BOOKING + "/" + USER + PATH_USER_ID;


    private ResourcesPath() throws IllegalAccessException {
        throw new IllegalAccessException("Resource path exception");
    }
}
