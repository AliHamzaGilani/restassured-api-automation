package com.alihamza.api.tests;

import com.alihamza.api.client.BookingClient;
import com.alihamza.api.data.BookingFactory;
import com.alihamza.api.models.Booking;
import com.alihamza.api.models.CreatedBooking;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;

public class BookingTest {

    private final BookingClient client = new BookingClient();
    private String token;

    @BeforeClass(alwaysRun = true)
    public void authenticate() {
        token = client.token();
    }

    private CreatedBooking createBooking(Booking booking) {
        return client.create(booking)
                .then().statusCode(200)
                .extract().as(CreatedBooking.class);
    }

    @Test(groups = {"smoke", "regression"}, description = "Create a booking and read it back")
    public void createAndGetBooking() {
        Booking booking = BookingFactory.validBooking();

        CreatedBooking created = createBooking(booking);
        assertThat(created.bookingid()).isPositive();
        assertThat(created.booking()).isEqualTo(booking);

        Booking fetched = client.get(created.bookingid())
                .then().statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/booking-schema.json"))
                .extract().as(Booking.class);
        assertThat(fetched).isEqualTo(booking);
    }

    @Test(groups = "regression", description = "New booking can be found by guest name")
    public void bookingIsSearchableByName() {
        Booking booking = BookingFactory.validBooking();
        int id = createBooking(booking).bookingid();

        List<Integer> ids = client.findByName(booking.firstname(), booking.lastname())
                .then().statusCode(200)
                .extract().jsonPath().getList("bookingid", Integer.class);

        assertThat(ids).contains(id);
    }

    @Test(groups = "regression", description = "Full update replaces every field")
    public void updateBooking() {
        int id = createBooking(BookingFactory.validBooking()).bookingid();
        Booking changed = BookingFactory.validBooking();

        Booking updated = client.update(id, changed, token)
                .then().statusCode(200)
                .extract().as(Booking.class);

        assertThat(updated).isEqualTo(changed);
    }

    @Test(groups = "regression", description = "Partial update changes only the given fields")
    public void partiallyUpdateBooking() {
        Booking original = BookingFactory.validBooking();
        int id = createBooking(original).bookingid();

        Booking patched = client.partialUpdate(id, Map.of("firstname", "Hamza", "totalprice", 999), token)
                .then().statusCode(200)
                .extract().as(Booking.class);

        assertThat(patched.firstname()).isEqualTo("Hamza");
        assertThat(patched.totalprice()).isEqualTo(999);
        assertThat(patched.lastname()).isEqualTo(original.lastname());
    }

    @Test(groups = "regression", description = "Update without a token is forbidden")
    public void updateWithoutTokenIsForbidden() {
        int id = createBooking(BookingFactory.validBooking()).bookingid();

        client.update(id, BookingFactory.validBooking(), "invalid-token")
                .then().statusCode(403);
    }

    @Test(groups = {"smoke", "regression"}, description = "Deleted booking can no longer be fetched")
    public void deleteBooking() {
        int id = createBooking(BookingFactory.validBooking()).bookingid();

        client.delete(id, token).then().statusCode(201);
        client.get(id).then().statusCode(404);
    }
}
