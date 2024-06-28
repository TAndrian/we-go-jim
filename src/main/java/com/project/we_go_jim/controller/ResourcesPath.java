package com.project.we_go_jim.controller;

public class ResourcesPath {
    public static final String BASE_URL = "api/v1/";
    public static final String BOOKINGS = "bookings";
    public static final String BOOKING = "booking";
    public static final String USERS = "users";
    public static final String USER = "user";

    public static final String API_BOOKINGS = BASE_URL + BOOKINGS;
    public static final String API_USERS = BASE_URL + USERS;
    public static final String API_USER = BASE_URL + USER;

    private ResourcesPath() throws IllegalAccessException {
        throw new IllegalAccessException("Resource path exception");
    }
}
