package tests;

import config.BaseAuthTest;
import jdk.jfr.Description;
import methods.LoginClient;
import org.junit.Before;
import org.junit.Test;
import pojo.Login;

import static org.hamcrest.Matchers.*;
import static org.apache.http.HttpStatus.*;


public class LoginUserTest extends BaseAuthTest {

    private LoginClient loginClient;

    @Before
    public void setup() {
        loginClient = new LoginClient();
    }

    @Description("Успешная авторизация")
    @Test
    public void loginWithSuccessTest() {
        Login login = new Login(email, password);

        loginClient.login(login)
                .then()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user.email", equalTo(email))
                .body("user.name", equalTo(name));
    }

    @Description("Попытка авторизации с неверным паролем")
    @Test
    public void loginWithWrongPasswordTest() {
        Login login = new Login(email, "wrong");

        loginClient.login(login)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Description("Попытка авторизации с неверным email")
    @Test
    public void loginWithWrongEmailTest() {
        Login login = new Login("yulia@yandex.ru", "12345678");

        loginClient.login(login)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}
