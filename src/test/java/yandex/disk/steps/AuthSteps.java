package yandex.disk.steps;

import com.aleksgolds.yandex.disk.api.YandexDiskApiClient;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static com.aleksgolds.yandex.disk.helpers.CustomAssertions.*;

public class AuthSteps {

    @Step("Получение токена авторизации")
    public String getAuthToken() {
        String token = YandexDiskApiClient.getAccessToken();
        assertNotNull(token, "Токен не должен быть null");
        assertTrue(token.startsWith("OAuth "),
                "Токен должен начинаться с 'OAuth '");
        return token;
    }

    @Step("Отправка запроса информации о диске с токеном {token}")
    public Response getUserDiskMetaInfo(String token) {
        Response response = YandexDiskApiClient.getUserDiskMetaInfo(token);
        assertNotNull(response, "Ответ не должен быть null");
        return response;
    }

    @Step("Проверка успешного ответа")
    public void validateSuccessResponse(Response response) {
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, 200,
                "Статус код должен быть 200");
        assertNotNull(response.jsonPath().getString("total_space"),
                "Поле 'total_space' должно присутствовать в ответе");
        assertNotNull(response.jsonPath().getString("used_space"),
                "Поле 'used_space' должно присутствовать в ответе");
    }

    @Step("Проверка ответа с ошибкой авторизации")
    public void validateUnauthorizedResponse(Response response) {
        int statusCode = response.getStatusCode();
        assertTrue(statusCode == 401 || statusCode == 403,
                "Статус код должен быть 401 или 403, фактический: " + statusCode);
    }
}
