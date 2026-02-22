package tests;


import methods.LoginClient;
import methods.UserClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.Login;
import pojo.User;
import static org.hamcrest.Matchers.*;


public class LoginUserTest {
    private UserClient userClient;
    private LoginClient loginClient;

    private String accessToken;
    private String email;
    private String password;
    private String name;

    @Before
    public void setup() {
        userClient = new UserClient();
        loginClient = new LoginClient();
        accessToken = null;

        email = "yuliasev" + System.currentTimeMillis() + "@yandex.ru";
        password = "12345678";
        name = "Yuliasev";

        User user = new User(email, password, name);
        accessToken = userClient.create(user)
                .then()
                .statusCode(200)
                .body("success", is(true))
                .extract()
                .path("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken)
                    .then()
                    .statusCode(202)
                    .body("success", is(true))
                    .body("message", equalTo("User successfully removed"));
        }
    }

    @Test
    public void loginWithSuccess() {
        Login login = new Login(email, password);

        loginClient.login(login)
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user.email", equalTo(email))
                .body("user.name", equalTo(name));
    }

    @Test
    public void loginWithWrongPassword() {
        Login login = new Login(email, "wrong");

        loginClient.login(login)
                .then()
                .statusCode(401)
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    public void loginWithWrongEmail() {
        Login login = new Login("yulia@yandex.ru", "12345678");

        loginClient.login(login)
                .then()
                .statusCode(401)
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}
