package com.aleksgolds.yandex.disk.api;

import com.aleksgolds.yandex.disk.data.json.CopyResourceRequestDto;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import lombok.Getter;

import java.io.File;

import static com.aleksgolds.yandex.disk.properties.Properties.testProperties;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;

/**
 * Класс запросов Yandex Disc API.
 */
public class YandexDiskApiClient {

    /**
     * API для получения метаинформации.
     */
    private static final String YD_AUTH = "v1/disk";

    /**
     * API для взаимодействия с ресурсами.
     */
    private static final String YD_RESOURCES = "v1/disk/resources";

    /**
     * API для копирования файлов.
     */
    private static final String YD_COPY = "v1/disk/resources/copy";

    /**
     * API для загрузки файлов на диск.
     */
    private static final String YD_UPLOAD = "v1/disk/resources/upload";

    /**
     * API для скачивания файлов с диска.
     */
    private static final String YD_DOWNLOAD = "v1/disk/resources/download";

    /**
     * API для взаимодействия с корзиной.
     */
    private static final String YD_TRASH = "v1/disk/trash/resources";

    /**
     * API для восстановления из корзины.
     */
    private static final String YD_TRASH_RESTORE = "v1/disk/trash/resources/restore";

    /**
     * Oauth токен для авторизации.
     */
    @Getter
    private static String accessToken;

    private YandexDiskApiClient() {
    }

    // ============================
    // 1. Конфигурация и инициализация
    // ============================

    /**
     * Инициализация спецификации запросов.
     */
    public static void initSpec(String baseUrl) {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder
                .setContentType(ContentType.JSON)
                .setBaseUri(baseUrl)
                .setAccept(ContentType.JSON);
        requestSpecification = requestSpecBuilder.build();
        accessToken = "OAuth " + testProperties.token();
    }

    // ============================
    // 2. Методы работы с диском (информация)
    // ============================

    /**
     * Получение метаинформации о диске пользователя.
     */
    public static Response getUserDiskMetaInfo(String token) {
        return given()
                .spec(requestSpecification)
                .header(new Header("Authorization", token))
                .when()
                .get(YD_AUTH);
    }

    // ============================
    // 3. Методы работы с ресурсами (папки/файлы)
    // ============================

    /**
     * Создать папку.
     */
    public static Response createFolder(Object folderPath) {
        return given()
                .spec(requestSpecification)
                .header(new Header("Authorization", accessToken))
                .param("path", folderPath)
                .when()
                .put(YD_RESOURCES);
    }

    /**
     * Удалить папку.
     */
    public static Response deleteFolder(String folderPath, boolean permanently) {
        return given()
                .spec(requestSpecification)
                .header(new Header("Authorization", accessToken))
                .param("path", folderPath)
                .param("permanently", permanently)
                .when()
                .delete(YD_RESOURCES);
    }

    /**
     * Копировать файл.
     */
    public static Response copyFile(CopyResourceRequestDto request) {
        return given()
                .spec(requestSpecification)
                .header(new Header("Authorization", accessToken))
                .queryParam("from", request.getFrom())
                .queryParam("path", request.getPath())
                .queryParam("overwrite", request.getOverwrite())
                .queryParam("fields", request.getFields())
                .when()
                .post(YD_COPY);
    }

    // ============================
    // 4. Методы загрузки файлов
    // ============================

    /**
     * Получить ссылку для загрузки файла.
     */
    public static Response getUploadUrl(String path) {
        return given()
                .spec(requestSpecification)
                .header(new Header("Authorization", accessToken))
                .param("path", path)
                .when()
                .get(YD_UPLOAD);
    }

    /**
     * Загрузить файл на диск.
     */
    public static Response uploadFile(String uploadUrl, String filePath) {
        return given()
                .contentType(ContentType.BINARY)
                .body(new File(filePath))
                .when()
                .put(uploadUrl);
    }

    // ============================
    // 5. Методы скачивания файлов
    // ============================

    /**
     * Получить ссылку на скачивание файла.
     */
    public static Response getDownloadUrl(String path) {
        return given()
                .spec(requestSpecification)
                .header(new Header("Authorization", accessToken))
                .param("path", path)
                .when()
                .get(YD_DOWNLOAD);
    }

    // ============================
    // 6. Методы работы с корзиной
    // ============================

    /**
     * Получить содержимое корзины.
     */
    public static Response getTrashHash() {
        return given()
                .spec(requestSpecification)
                .header(new Header("Authorization", accessToken))
                .when()
                .get(YD_TRASH);
    }

    /**
     * Восстановить папку из корзины.
     */
    public static Response restoreFolderFromTrash(String path) {
        return given()
                .spec(requestSpecification)
                .header(new Header("Authorization", accessToken))
                .param("path", path)
                .when()
                .put(YD_TRASH_RESTORE);
    }

    /**
     * Очистить корзину.
     */
    public static Response clearTrash() {
        return given()
                .spec(requestSpecification)
                .header(new Header("Authorization", accessToken))
                .param("force_async","false")
                .when()
                .delete(YD_TRASH);
    }
}