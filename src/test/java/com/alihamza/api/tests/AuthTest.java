package com.alihamza.api.tests;

import com.alihamza.api.client.BookingClient;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.lessThan;

public class AuthTest {

    private final BookingClient client = new BookingClient();

    @Test(groups = {"smoke", "regression"}, description = "Health check endpoint is up")
    public void pingReturnsCreated() {
        client.ping()
                .then()
                .statusCode(201)
                .time(lessThan(5000L));
    }

    @Test(groups = {"smoke", "regression"}, description = "Valid credentials return a token")
    public void validCredentialsReturnToken() {
        client.createToken("admin", "password123")
                .then()
                .statusCode(200)
                .body("token", not(emptyOrNullString()));
    }

    @Test(groups = "regression", description = "Invalid credentials are rejected")
    public void invalidCredentialsAreRejected() {
        client.createToken("admin", "wrong")
                .then()
                .statusCode(200)
                .body("reason", equalTo("Bad credentials"));
    }
}
