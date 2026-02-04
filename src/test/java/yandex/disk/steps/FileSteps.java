package yandex.disk.steps;

import com.aleksgolds.yandex.disk.api.YandexDiskApiClient;
import com.aleksgolds.yandex.disk.data.json.CopyResourceRequestDto;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import static com.aleksgolds.yandex.disk.helpers.CustomAssertions.*;

public class FileSteps {

    @Step("Получение ссылки для загрузки файла {path}")
    public Response getUploadUrl(String path) {
        Response response = YandexDiskApiClient.getUploadUrl(path);
        assertNotNull(response, "Ответ при получении ссылки для загрузки не должен быть null");
        return response;
    }

    @Step("Получение ссылки для скачивания файла {path}")
    public Response getDownloadUrl(String path) {
        Response response = YandexDiskApiClient.getDownloadUrl(path);
        assertNotNull(response, "Ответ при получении ссылки для скачивания не должен быть null");
        return response;
    }

    @Step("Загрузка файла по ссылке")
    public void uploadFile(String uploadUrl, String filePath) {
        Response response = YandexDiskApiClient.uploadFile(uploadUrl, filePath);

        int statusCode = response.getStatusCode();
        assertTrue(statusCode == 201 || statusCode == 202,
                "Файл не был загружен, статус: " + statusCode);
    }

    @Step("Копирование файла")
    public Response copyFile(CopyResourceRequestDto request) {
        Response response = YandexDiskApiClient.copyFile(request);
        assertNotNull(response, "Ответ при копировании файла не должен быть null");
        return response;
    }

    @Step("Создание запроса на копирование")
    public CopyResourceRequestDto createCopyRequest(String from, String to, boolean overwrite) {
        return CopyResourceRequestDto.builder()
                .from(from)
                .path(to)
                .overwrite(overwrite)
                .fields("")
                .build();
    }

    @Step("Проверка ответа с ссылкой для загрузки")
    public void validateUploadUrlResponse(Response response) {
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, 200,
                "Статус код должен быть 200, фактический: " + statusCode);

        String href = response.jsonPath().getString("href");
        assertNotNull(href, "Ссылка для загрузки должна присутствовать в ответе");
        assertTrue(href.startsWith("https://"), "Ссылка должна быть HTTPS URL: " + href);
    }

    @Step("Проверка ответа с ссылкой для скачивания")
    public void validateDownloadUrlResponse(Response response) {
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, 200,
                "Статус код должен быть 200, фактический: " + statusCode);

        String href = response.jsonPath().getString("href");
        assertNotNull(href, "Ссылка для скачивания должна присутствовать в ответе");
        assertTrue(href.startsWith("https://"), "Ссылка должна быть HTTPS URL: " + href);
    }

    @Step("Проверка успешного копирования файла")
    public void validateFileCopied(Response response) {
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, 201,
                "Статус код должен быть 201 при копировании файла, фактический: " + statusCode);
    }

    @Step("Создание тестового файла")
    public File createTestFile(String content) throws IOException {
        String fileName = "test_file_" + System.currentTimeMillis() + "_" +
                UUID.randomUUID().toString().substring(0, 8) + ".txt";
        File testFile = File.createTempFile(fileName, ".txt");
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write(content);
        }
        return testFile;
    }

    @Step("Удаление локального файла")
    public void deleteLocalFile(File file) {
        if (file != null && file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                System.out.println("Не удалось удалить файл: " + file.getAbsolutePath());
            }
        }
    }

    @Step("Извлечение ссылки для загрузки из ответа")
    public String extractUploadHref(Response response) {
        return response.jsonPath().getString("href");
    }

    @Step("Проверить статус код {expectedStatusCode}")
    public void verifyStatusCode(Response response, int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        assertEquals(actualStatusCode, expectedStatusCode,
                String.format("Ожидался статус код %d, но получен %d",
                        expectedStatusCode, actualStatusCode));
    }

    @Step("Сгенерировать уникальное имя файла")
    public String generateUniqueFileName(String prefix) {
        return prefix + "_" + System.currentTimeMillis() + "_" +
                UUID.randomUUID().toString().substring(0, 8) + ".txt";
    }

    @Step("Сгенерировать уникальное имя папки")
    public String generateUniqueFolderName(String prefix) {
        return prefix + "_" + System.currentTimeMillis() + "_" +
                UUID.randomUUID().toString().substring(0, 8);
    }

    @Step("Создать тестовый контент")
    public String generateTestContent() {
        return "Test content generated at: " + System.currentTimeMillis() +
                "\nRandom UUID: " + UUID.randomUUID().toString();
    }
}