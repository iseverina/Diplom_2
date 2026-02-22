package methods;

import config.BaseConfig;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class IngredientsClient {

    private static final String INGREDIENTS_PATH = "/api/ingredients";

    @Step("Получить список ингредиентов")
    public Response getIngredients() {
        return given()
                .spec(BaseConfig.baseSpec())
                .when()
                .get(INGREDIENTS_PATH);
    }

    @Step("Получить id ингредиентов")
    public String[] getIngredientIds() {
        Response response = getIngredients();

        String id1 = response.then().extract().path("data[0]._id");
        String id2 = response.then().extract().path("data[1]._id");

        return new String[]{id1, id2};

    }

}
