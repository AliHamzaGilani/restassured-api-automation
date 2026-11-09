package com.alihamza.api.data;

import com.alihamza.api.models.Booking;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

/** Builds unique, valid booking payloads so tests never depend on shared data. */
public final class BookingFactory {

    private BookingFactory() {
    }

    public static Booking validBooking() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        LocalDate checkin = LocalDate.now().plusDays(random.nextInt(5, 60));
        return new Booking(
                "Ali",
                "Tester" + random.nextInt(1000, 9999),
                random.nextInt(100, 1000),
                true,
                new Booking.BookingDates(checkin.toString(), checkin.plusDays(3).toString()),
                "Breakfast");
    }
}
