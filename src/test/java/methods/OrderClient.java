package methods;

import config.BaseConfig;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

import pojo.Order;

public class OrderClient {
    private static final String ORDER_PATH = "/api/orders";

    @Step("Создание заказа с авторизацией")
    public Response orderWithAuth(Order order, String accessToken) {
        return given()
                .spec(BaseConfig.baseSpec())
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(ORDER_PATH);
    }

    @Step("Создание без авторизации")
    public Response orderWithoutAuth(Order order) {
        return given()
                .spec(BaseConfig.baseSpec())
                .body(order)
                .when()
                .post(ORDER_PATH);
    }

}
