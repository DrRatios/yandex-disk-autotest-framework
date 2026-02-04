package yandex.disk.steps;

import com.aleksgolds.yandex.disk.api.YandexDiskApiClient;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.UUID;

import static com.aleksgolds.yandex.disk.helpers.CustomAssertions.*;

public class TrashSteps {

    @Step("Восстановление папки {path} из корзины")
    public Response restoreFolderFromTrash(String path) {
        Response response = YandexDiskApiClient.restoreFolderFromTrash(path);
        assertNotNull(response, "Ответ при восстановлении папки не должен быть null");
        return response;
    }

    @Step("Получение информации о корзине")
    public Response getTrashHash() {
        Response response = YandexDiskApiClient.getTrashHash();
        assertNotNull(response, "Ответ при получении информации о корзине не должен быть null");
        return response;
    }

    @Step("Очистка корзины")
    public Response clearTrash() {
        Response response = YandexDiskApiClient.clearTrash();
        assertNotNull(response, "Ответ при очистке корзины не должен быть null");
        return response;
    }

    @Step("Проверка ответа информации о корзине")
    public void validateTrashResponse(Response response) {
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, 200,
                "Статус код должен быть 200, фактический: " + statusCode);

        assertNotNull(response.getBody(), "Тело ответа не должно быть null");
    }

    @Step("Проверка успешной очистки корзины")
    public void validateTrashCleared(Response response) {
        int statusCode = response.getStatusCode();
        waitUntilTrashCleared();
        assertEquals(statusCode, 204,
                "Статус код должен быть 204 при очистке корзины, фактический: " + statusCode);
    }

    @Step("Полное удаление папки (мимо корзины)")
    public Response deleteFolderPermanently(String folderPath) {
        Response response = YandexDiskApiClient.deleteFolder(folderPath, true);
        assertNotNull(response, "Ответ при полном удалении папки не должен быть null");
        return response;
    }

    @Step("Сгенерировать уникальное имя папки")
    public String generateUniqueFolderName(String prefix) {
        return prefix + "_" + System.currentTimeMillis() + "_" +
                UUID.randomUUID().toString().substring(0, 8);
    }

    @Step("Ожидание полной очистки корзины")
    public void waitUntilTrashCleared() {
        int attempts = 10;

        for (int i = 0; i < attempts; i++) {
            Response response = YandexDiskApiClient.clearTrash();

            if (response.getStatusCode() == 204) {
                return;
            }
        }

        throw new AssertionError("Корзина не была очищена");
    }
}