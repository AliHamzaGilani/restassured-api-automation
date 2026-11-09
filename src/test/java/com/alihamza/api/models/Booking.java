package com.alihamza.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Booking(
        String firstname,
        String lastname,
        int totalprice,
        boolean depositpaid,
        BookingDates bookingdates,
        String additionalneeds) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BookingDates(String checkin, String checkout) {
    }
}
