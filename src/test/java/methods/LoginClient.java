package methods;

import config.BaseConfig;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

import pojo.Login;


public class LoginClient {
    private static final String LOGIN_PATH = "/api/auth/login";

    @Step("Авторизация пользователя")
    public Response login(Login login) {
        return given()
                .spec(BaseConfig.baseSpec())
                .body(login)
                .when()
                .post(LOGIN_PATH);


    }

}
