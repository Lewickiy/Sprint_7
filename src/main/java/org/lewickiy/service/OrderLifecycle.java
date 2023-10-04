package org.lewickiy.service;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.lewickiy.entity.Order;

import static io.restassured.RestAssured.given;
import static org.lewickiy.ApiEndpoints.CANCEL_ORDER;
import static org.lewickiy.ApiEndpoints.ORDERS_PATH;

public class OrderLifecycle {
    @Step("Create new order")
    public ValidatableResponse createOrder(Order order) {
        return RootRequest.rootRequest().body(order)
                .when().post(ORDERS_PATH)
                .then().log().all();
    }

    @Step("Cancel order")
    public ValidatableResponse cancelOrder(Integer id) {
        return given().spec(RootRequest.rootRequest())
                .queryParam("track", id)
                .when()
                .put(CANCEL_ORDER)
                .then()
                .log()
                .all();
    }
}
