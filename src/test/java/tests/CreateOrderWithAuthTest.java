package tests;

import config.BaseAuthTest;
import jdk.jfr.Description;
import methods.IngredientsClient;
import methods.OrderClient;
import org.junit.Before;
import org.junit.Test;
import pojo.Order;


import static org.hamcrest.Matchers.*;
import static org.apache.http.HttpStatus.*;

public class CreateOrderWithAuthTest extends BaseAuthTest {
    private IngredientsClient ingredientsClient;
    private OrderClient orderClient;

    @Before
    public void setup() {
        ingredientsClient = new IngredientsClient();
        orderClient = new OrderClient();
    }

    @Description("Создание заказа с авторизованным пользователем")
    @Test
    public void createOrderWithAuthAndIngredientsTest() {
        String[] ingredientIds = ingredientsClient.getIngredientIds();
        Order order = new Order(ingredientIds);

        orderClient.orderWithAuth(order, accessToken)
                .then()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("order.number", greaterThan(0))
                .body("order._id", notNullValue())
                .body("order.owner.email", equalTo(email))
                .body("order.owner.name", equalTo(name))
                .body("order.ingredients.size()", greaterThan(0))
                .body("order.ingredients[0]._id", notNullValue());
    }
}

