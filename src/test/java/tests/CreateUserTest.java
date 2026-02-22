package tests;

import methods.UserClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.User;

import static org.hamcrest.Matchers.*;


public class CreateUserTest {
    private UserClient userClient;
    private String accessToken;

    @Before
    public void setup() {
        userClient = new UserClient();
        accessToken = null;
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
    public void userCanBeCreated() {
        String email = "muxamor" + System.currentTimeMillis() + "@yandex.ru";
        String password = "12345678";
        String name = "muxamor";

        User user = new User(email, password, name);

        accessToken = userClient.create(user)
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user.email", equalTo(email))
                .body("user.name", equalTo(name))
                .extract()
                .path("accessToken");

    }

    @Test
    public void userDuplicateEmail() {
        String email = "muxamorik@yandex.ru" + System.currentTimeMillis();
        String password = "123456";
        String name = "muxamorik";

        User user = new User(email, password, name);

        accessToken = userClient.create(user)
                .then()
                .statusCode(200)
                .body("success", is(true))
                .extract()
                .path("accessToken");


        userClient.create(user)
                .then()
                .statusCode(403)
                .body("success", is(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    public void userCannotBeCreatedWithMissingField() {
        String email = "muxamora@yandex.ru" + System.currentTimeMillis();
        String password = "123456";

        User user = new User(email, password, null);
        userClient.create(user)
                .then()
                .statusCode(403)
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

}
