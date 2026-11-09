package com.alihamza.api.client;

import com.alihamza.api.models.Booking;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

/** Thin client over the Restful Booker API. Tests call these methods instead of building requests. */
public class BookingClient {

    private final RequestSpecification spec;

    public BookingClient() {
        spec = new RequestSpecBuilder()
                .setBaseUri(System.getProperty("baseUrl", "https://restful-booker.herokuapp.com"))
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);
    }

    public Response ping() {
        return given(spec).get("/ping");
    }

    public Response createToken(String username, String password) {
        return given(spec)
                .body(Map.of("username", username, "password", password))
                .post("/auth");
    }

    public String token() {
        return createToken(
                System.getProperty("apiUser", "admin"),
                System.getProperty("apiPassword", "password123"))
                .then().statusCode(200)
                .extract().path("token");
    }

    public Response create(Booking booking) {
        return given(spec).body(booking).post("/booking");
    }

    public Response get(int id) {
        return given(spec).get("/booking/{id}", id);
    }

    public Response findByName(String firstname, String lastname) {
        return given(spec)
                .queryParam("firstname", firstname)
                .queryParam("lastname", lastname)
                .get("/booking");
    }

    public Response update(int id, Booking booking, String token) {
        return given(spec).cookie("token", token).body(booking).put("/booking/{id}", id);
    }

    public Response partialUpdate(int id, Map<String, Object> fields, String token) {
        return given(spec).cookie("token", token).body(fields).patch("/booking/{id}", id);
    }

    public Response delete(int id, String token) {
        return given(spec).cookie("token", token).delete("/booking/{id}", id);
    }
}
