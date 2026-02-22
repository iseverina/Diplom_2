package tests;

import io.restassured.response.Response;
import methods.IngredientsClient;
import methods.OrderClient;
import methods.UserClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.Order;
import pojo.User;

import static org.hamcrest.Matchers.*;

public class CreateOrderTest {
    private IngredientsClient ingredientsClient;
    private OrderClient orderClient;
    private UserClient userClient;

    private String accessToken;
    private String email;
    private String password;
    private String name;

    @Before
    public void setup() {
        ingredientsClient = new IngredientsClient();
        orderClient = new OrderClient();
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
    public void createOrderWithAuthAndIngredients() {
        email = "yulia_order" + System.currentTimeMillis()+ "@yandex.ru";
        password = "123456";
        name = "Yulia";

        User user = new User(email, password, name);

        Response registerResponse = userClient.create(user);
        accessToken = registerResponse.then().extract().path("accessToken");

        String[] ingredientIds = ingredientsClient.getIngredientIds();
        Order order = new Order(ingredientIds);

        orderClient.orderWithAuth(order, accessToken)
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("order.number", greaterThan(0))
                .body("order._id", notNullValue())
                .body("order.owner.email", equalTo(email))
                .body("order.owner.name", equalTo(name))
                .body("order.ingredients.size()", greaterThan(0))
                .body("order.ingredients[0]._id", notNullValue());
    }

    @Test
    public void createOrderWithoutAuthAndWithIngredients() {
        String[] ingredientIds = ingredientsClient.getIngredientIds();
        Order order = new Order(ingredientIds);

        orderClient.orderWithoutAuth(order)
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("order.number", greaterThan(0))
                .body("order.owner", nullValue())
                .body("order._id", nullValue());
    }

    @Test
    public void createOrderWithoutIngredients() {
        Order order = new Order(new String[]{});
        orderClient.orderWithoutAuth(order)
                .then()
                .statusCode(400)
                .body("success", is(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    public void createOrderWithWrongIngredientsId() {
        Order order = new Order(new String[]{"61c0c5a71d1f82001wrong"});
        orderClient.orderWithoutAuth(order)
                .then()
                .statusCode(500)
                .body(containsString("Internal Server Error"));
    }

}
