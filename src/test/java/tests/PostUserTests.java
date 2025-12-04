package tests;

import endpoints.UserEndpoints;
import io.qameta.allure.Description;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.is;

public class PostUserTests extends TestBase {

    public static final String validRegisterData = "{\"email\": \"charles.morris@reqres.in\", \"password\": \"testest\"}";
    public static final String validCreateData = "{\"name\": \"Mark\", \"job\": \"QA\"}";
    public static final String inValidRegisterData = "{\"email\": \"charles.morris@reqres.in\", \"password\": \"\"}";

    @Test
    @Description("Отправка POST запроса и создание пользователя")
    @DisplayName("Проверка успешного создания нового пользователя")
    void createUserTestPositive() {
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(validCreateData)
                .when()
                .post(UserEndpoints.CREATE_USER.getEndpoint())
                .then()
                .log().all()
                .statusCode(201)
                .body("name", equalTo("Mark"))
                .body("job", equalTo("QA"));
    }

    @Test
    @Description("Отправка POST запроса и регистрация нового пользователя")
    @DisplayName("Проверка успешной регистрации нового пользователя")
    void registerNewUserSuccess() {
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(validRegisterData)
                .when()
                .post(UserEndpoints.REGISTER_USER.getEndpoint())
                .then()
                .log().all()
                .statusCode(200)
                .body("id", notNullValue())
                .body("token", Matchers.is(Matchers.notNullValue()))
                .body("token.length()", Matchers.is(Matchers.greaterThanOrEqualTo(17)))
                .body("token", Matchers.is(Matchers.matchesRegex("\\S[a-zA-Z0-9]*\\S")));
    }

    @Test
    @Description("Отправка POST запроса и регистрация нового пользователя")
    @DisplayName("Проверка ошибки при попытке зарегистрировать пользователя без пароля")
    void registerNewUserNegative() {
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(inValidRegisterData)
                .when()
                .post(UserEndpoints.REGISTER_USER.getEndpoint())
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

}