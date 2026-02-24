package tests;

import jdk.jfr.Description;
import methods.IngredientsClient;
import methods.OrderClient;

import org.junit.Before;
import org.junit.Test;
import pojo.Order;

import static org.hamcrest.Matchers.*;
import static org.apache.http.HttpStatus.*;

public class CreateOrderTest {
    private IngredientsClient ingredientsClient;
    private OrderClient orderClient;

    @Before
    public void setup() {
        ingredientsClient = new IngredientsClient();
        orderClient = new OrderClient();

    }

    @Description("Создание заказа c ингредиентами без авторизации пользователя")
    @Test
    public void createOrderWithoutAuthAndWithIngredientsTest() {
        String[] ingredientIds = ingredientsClient.getIngredientIds();
        Order order = new Order(ingredientIds);

        orderClient.orderWithoutAuth(order)
                .then()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("order.number", greaterThan(0))
                .body("order.owner", nullValue())
                .body("order._id", nullValue());
    }

    @Description("Создание заказа без ингредиентов")
    @Test
    public void createOrderWithoutIngredientsTest() {
        Order order = new Order(new String[]{});
        orderClient.orderWithoutAuth(order)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("success", is(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Description("Создание заказа с несуществующим ингредиентом")
    @Test
    public void createOrderWithWrongIngredientsIdTest() {
        Order order = new Order(new String[]{"61c0c5a71d1f82001wrong"});
        orderClient.orderWithoutAuth(order)
                .then()
                .statusCode(SC_INTERNAL_SERVER_ERROR)
                .body(containsString("Internal Server Error"));
    }

}
