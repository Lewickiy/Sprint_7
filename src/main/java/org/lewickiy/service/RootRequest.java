package org.lewickiy.service;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static org.lewickiy.ApiEndpoints.BASE_URL;

public class RootRequest {
    public static RequestSpecification rootRequest() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL);
    }
}
