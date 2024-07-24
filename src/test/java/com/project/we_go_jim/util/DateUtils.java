package com.project.we_go_jim.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateUtils {
    /**
     * Format date into "yyyy-MM-dd'T'HH:mm" pattern.
     *
     * @param dateTime date.
     * @return formatted date.
     */
    public LocalDateTime formatDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return LocalDateTime.parse(dateTime.format(formatter), formatter);
    }
}
