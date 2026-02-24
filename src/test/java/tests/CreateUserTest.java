package tests;

import jdk.jfr.Description;
import methods.UserClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.User;

import static org.hamcrest.Matchers.*;
import static org.apache.http.HttpStatus.*;


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
                    .statusCode(SC_ACCEPTED)
                    .body("success", is(true))
                    .body("message", equalTo("User successfully removed"));
        }
    }

    @Description("Успешное создание пользователя")
    @Test
    public void userCanBeCreatedTest() {
        String email = "muxamor" + System.currentTimeMillis() + "@yandex.ru";
        String password = "12345678";
        String name = "muxamor";

        User user = new User(email, password, name);

        accessToken = userClient.create(user)
                .then()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user.email", equalTo(email))
                .body("user.name", equalTo(name))
                .extract()
                .path("accessToken");

    }

    @Description("Попытка создания уже существующего пользователя")
    @Test
    public void userDuplicateEmailTest() {
        String email = "muxamorik@yandex.ru" + System.currentTimeMillis();
        String password = "123456";
        String name = "muxamorik";

        User user = new User(email, password, name);

        accessToken = userClient.create(user)
                .then()
                .statusCode(SC_OK)
                .body("success", is(true))
                .extract()
                .path("accessToken");


        userClient.create(user)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", equalTo("User already exists"));
    }

    @Description("Попытка создать пользователя с пустым полем name")
    @Test
    public void userCannotBeCreatedWithMissingNameFieldTest() {
        String email = "muxamora@yandex.ru" + System.currentTimeMillis();
        String password = "123456";

        User user = new User(email, password, null);
        userClient.create(user)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Description("Попытка создать пользователя с пустым полем email")
    @Test
    public void userCannotBeCreatedWithMissingEmailFieldTest() {

        String password = "123456";
        String name = "Mishutka";

        User user = new User(null, password, name);
        userClient.create(user)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Description("Попытка создать пользователя с пустым полем password")
    @Test
    public void userCannotBeCreatedWithMissingPasswordFieldTest() {

        String email = "muxamora@yandex.ru" + System.currentTimeMillis();
        String name = "Mishutka";

        User user = new User(email, null, name);
        userClient.create(user)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
