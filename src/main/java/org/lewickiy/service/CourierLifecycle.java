package org.lewickiy.service;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.lewickiy.entity.Courier;

import static io.restassured.RestAssured.given;
import static org.lewickiy.ApiEndpoints.*;

public class CourierLifecycle {
    public RequestSpecification rootRequest() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL);
    }

    @Step("Courier registration")
    public ValidatableResponse createCourier(Courier courier) {
        return rootRequest().body(courier).when().post(CREATE_COURIER_PATH).then().log().all();
    }

    @Step("Courier authorization")
    public ValidatableResponse registeredCourierLogin(Courier courier) {
        return rootRequest().body(courier).when().post(LOGIN_COURIER_PATH).then().log().all();
    }

    @Step("Removing a courier")
    public ValidatableResponse deleteCourierById(Integer id) {
        return rootRequest().when().delete(COURIER_DELETE + id).then().log().all();
    }
}
