package config;

import io.restassured.response.Response;
import methods.UserClient;
import org.junit.After;
import org.junit.Before;
import pojo.User;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.apache.http.HttpStatus.*;

public abstract class BaseAuthTest {

    protected UserClient userClient;

    protected String accessToken;
    protected String email;
    protected String password;
    protected String name;

    @Before
    public void createUser() {
        userClient = new UserClient();

        email = "yulia_order" + System.currentTimeMillis() + "@yandex.ru";
        password = "123456";
        name = "Yulia";

        User user = new User(email, password, name);

        Response registerResponse = userClient.create(user);

        accessToken = registerResponse.then()
                .extract()
                .path("accessToken");
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            userClient.delete(accessToken)
                    .then()
                    .statusCode(SC_ACCEPTED)
                    .body("success", is(true))
                    .body("message", equalTo("User successfully removed"));
        }
    }
}
