package org.lewickiy.service;

import io.restassured.response.ValidatableResponse;
import org.lewickiy.entity.Courier;

import java.net.HttpURLConnection;

import static org.hamcrest.CoreMatchers.equalTo;

public class CourierAssertions {
    CourierLifecycle courierLifecycle = new CourierLifecycle();
    public ValidatableResponse notEnoughDataCreatingCourier(Courier courier) {
        return courierLifecycle.createCourier(courier)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
