package config;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;


import static io.restassured.http.ContentType.JSON;

public class BaseConfig {
    public static final String BASE_URI = "https://stellarburgers.education-services.ru";

    public static RequestSpecification baseSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setContentType(JSON)
                .addFilter(new AllureRestAssured())
                .build();
    }
}

