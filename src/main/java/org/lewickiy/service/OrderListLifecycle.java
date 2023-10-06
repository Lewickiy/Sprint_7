package org.lewickiy.service;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static org.lewickiy.ApiEndpoints.ORDERS_PATH;

public class OrderListLifecycle {
    @Step("A GET request returns a list of orders")
    public ValidatableResponse getOrders() {
        return RootRequest.rootRequest().get(ORDERS_PATH).then().log().all();
    }
}
