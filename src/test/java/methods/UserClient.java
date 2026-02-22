package methods;

import config.BaseConfig;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

import pojo.User;

public class UserClient {
    private static final String REGISTER_PATH = "/api/auth/register";
    private static final String DELETE_USER_PATH = "/api/auth/user";

    @Step("Создание пользователя")
    public Response create(User user) {
        return given()
                .spec(BaseConfig.baseSpec())
                .body(user)
                .when()
                .post(REGISTER_PATH);
    }

    @Step("Удаление пользователя")
    public Response delete(String accessToken) {
        return given()
                .spec(BaseConfig.baseSpec())
                .header("Authorization", accessToken)
                .when()
                .delete(DELETE_USER_PATH);


    }
}
