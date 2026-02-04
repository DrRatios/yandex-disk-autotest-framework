package yandex.disk.steps;

import com.aleksgolds.yandex.disk.api.YandexDiskApiClient;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static com.aleksgolds.yandex.disk.helpers.CustomAssertions.*;

public class FolderSteps {

    @Step("Создание папки {folderPath}")
    public Response createFolder(String folderPath) {
        Response response = YandexDiskApiClient.createFolder(folderPath);
        assertNotNull(response, "Ответ при создании папки не должен быть null");
        return response;
    }

    @Step("Удаление папки {folderPath} (permanently: {permanently})")
    public Response deleteFolder(String folderPath, boolean permanently) {
        Response response = YandexDiskApiClient.deleteFolder(folderPath, permanently);
        assertNotNull(response, "Ответ при удалении папки не должен быть null");
        return response;
    }

    @Step("Проверка успешного создания папки")
    public void validateFolderCreated(Response response) {
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, 201,
                "Статус код должен быть 201 при создании папки, фактический: " + statusCode);
    }

    @Step("Проверка конфликта при создании")
    public void validateConflictResponse(Response response) {
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, 409,
                "Статус код должен быть 409 при конфликте, фактический: " + statusCode);
    }

    @Step("Проверка отсутствия ресурса")
    public void validateNotFoundResponse(Response response) {
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, 404,
                "Статус код должен быть 404 при отсутствии ресурса, фактический: " + statusCode);
    }

    @Step("Создание уникального имени папки с префиксом {prefix}")
    public String generateUniqueFolderName(String prefix) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        return prefix + "_" + timestamp;
    }
    @Step("Проверка успешного удаления папки")

    public void validateFolderDeleted(Response response) {
        int statusCode = response.getStatusCode();
        assertTrue(statusCode == 202 || statusCode == 204,
                "Статус код должен быть 202 или 204 при удалении папки, фактический: " + statusCode);
    }
}
