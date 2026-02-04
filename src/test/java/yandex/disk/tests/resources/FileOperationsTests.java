package yandex.disk.tests.resources;

import com.aleksgolds.yandex.disk.data.json.CopyResourceRequestDto;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import yandex.disk.steps.FileSteps;
import yandex.disk.steps.FolderSteps;
import yandex.disk.tests.BaseTest;

import java.io.File;

import static yandex.disk.tests.Groups.NEGATIVE;
import static yandex.disk.tests.Groups.POSITIVE;

@Feature("Операции с файлами")
public class FileOperationsTests extends BaseTest {

    private FileSteps fileSteps;
    private FolderSteps folderSteps;
    private String testFolderPath;
    private String testFilePath;
    private File testFile;

    @DataProvider(name = "invalidFilePaths")
    public Object[][] invalidFilePaths() {
        return new Object[][]{
                {""},
                {"!№;%:?*()_++"},
                {"///"},
                {null}
        };
    }

    @BeforeMethod
    @Step("Подготовка тестовых данных")
    public void setupTestData() throws Exception {
        fileSteps = new FileSteps();
        folderSteps = new FolderSteps();

        testFolderPath = fileSteps.generateUniqueFolderName("test_folder");
        String fileName = fileSteps.generateUniqueFileName("test_file");
        testFilePath = testFolderPath + "/" + fileName;

        String testContent = fileSteps.generateTestContent();
        testFile = fileSteps.createTestFile(testContent);

        folderSteps.createFolder(testFolderPath);
    }

    @Test(groups = POSITIVE)
    @Story("Загрузка файлов")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка получения ссылки для загрузки файла")
    public void getUploadUrlForFile() {
        Response response = fileSteps.getUploadUrl(testFilePath);
        fileSteps.validateUploadUrlResponse(response);
    }

    @Test(groups = POSITIVE)
    @Story("Скачивание файлов")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка получения ссылки для скачивания файла")
    public void getDownloadUrlForFile() {
        uploadTestFile();

        Response response = fileSteps.getDownloadUrl(testFilePath);
        fileSteps.validateDownloadUrlResponse(response);
    }

    @Test(groups = POSITIVE)
    @Story("Копирование файлов")
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверка копирования файла")
    public void copyFile() {
        uploadTestFile();

        String destinationPath = testFolderPath + "/" +
                fileSteps.generateUniqueFileName("copied_file");
        var copyRequest = fileSteps.createCopyRequest(testFilePath, destinationPath, true);

        Response response = fileSteps.copyFile(copyRequest);
        fileSteps.validateFileCopied(response);
    }

    @Test(groups = POSITIVE)
    @Story("Копирование файлов")
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверка копирования файла с перезаписью")
    public void copyFileWithOverwrite() {
        uploadTestFile();

        String copyPath = testFolderPath + "/" +
                fileSteps.generateUniqueFileName("copy");
        var copyRequest1 = fileSteps.createCopyRequest(testFilePath, copyPath, true);
        fileSteps.copyFile(copyRequest1);

        var copyRequest2 = fileSteps.createCopyRequest(testFilePath, copyPath, true);
        Response response = fileSteps.copyFile(copyRequest2);
        fileSteps.validateFileCopied(response);
    }

    @Test(groups = POSITIVE)
    @Story("Загрузка файлов")
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверка загрузки файла с последующим скачиванием")
    public void uploadAndDownloadFile() {
        Response uploadUrlResponse = fileSteps.getUploadUrl(testFilePath);
        fileSteps.validateUploadUrlResponse(uploadUrlResponse);

        String uploadUrl = fileSteps.extractUploadHref(uploadUrlResponse);
        fileSteps.uploadFile(uploadUrl, testFile.getAbsolutePath());

        Response downloadUrlResponse = fileSteps.getDownloadUrl(testFilePath);
        fileSteps.validateDownloadUrlResponse(downloadUrlResponse);
    }

    @Test(groups = NEGATIVE)
    @Story("Обработка ошибок")
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверка получения ссылки для несуществующего файла")
    public void getDownloadUrlForNonExistentFile() {
        String nonExistentPath = testFolderPath + "/non_existent_file.txt";
        Response response = fileSteps.getDownloadUrl(nonExistentPath);

        fileSteps.verifyStatusCode(response, 404);
    }


    @Test(groups = NEGATIVE, dataProvider = "invalidFilePaths")
    @Story("Загрузка файлов")
    @Severity(SeverityLevel.NORMAL)
    @Description("Получение upload URL с невалидным path")
    public void getUploadUrlWithInvalidPath(String path) {
        Response response = fileSteps.getUploadUrl(path);
        fileSteps.verifyStatusCode(response, 400);
    }

    @Test(groups = NEGATIVE)
    @Story("Копирование файлов")
    @Severity(SeverityLevel.NORMAL)
    @Description("Копирование несуществующего файла")
    public void copyNonExistentFile() {
        String from = testFolderPath + "/missing.txt";
        String to = testFolderPath + "/copy.txt";

        CopyResourceRequestDto request = fileSteps.createCopyRequest(from, to, true);
        Response response = fileSteps.copyFile(request);

        fileSteps.verifyStatusCode(response, 404);
    }

    @Step("Загрузка тестового файла на сервер")
    private void uploadTestFile() {
        Response uploadUrlResponse = fileSteps.getUploadUrl(testFilePath);
        fileSteps.validateUploadUrlResponse(uploadUrlResponse);

        String uploadUrl = fileSteps.extractUploadHref(uploadUrlResponse);
        fileSteps.uploadFile(uploadUrl, testFile.getAbsolutePath());

    }

    @AfterMethod(alwaysRun = true)
    @Step("Очистка тестовых данных")
    public void cleanup() {
        try {
            folderSteps.deleteFolder(testFolderPath, true);
        } catch (Exception ignored) {
        }
        fileSteps.deleteLocalFile(testFile);
    }

}
