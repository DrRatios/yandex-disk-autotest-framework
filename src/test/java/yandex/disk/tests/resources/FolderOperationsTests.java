package yandex.disk.tests.resources;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import yandex.disk.steps.FolderSteps;

public class FolderOperationsTests {

    private FolderSteps folderSteps;
    private String testFolderPath;

    @BeforeMethod
    @Step("Подготовка тестовых данных")
    public void setupTestData() {
        folderSteps = new FolderSteps();
        testFolderPath = folderSteps.generateUniqueFolderName("test_folder");
    }

    @Test
    @Story("Создание папки")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка создания новой папки")
    public void createNewFolder() {
        Response response = folderSteps.createFolder(testFolderPath);
        folderSteps.validateFolderCreated(response);
    }

    @Test
    @Story("Создание папки")
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверка создания существующей папки")
    public void createExistingFolder() {
        folderSteps.createFolder(testFolderPath);
        Response response = folderSteps.createFolder(testFolderPath);
        folderSteps.validateConflictResponse(response);
    }

    @Test
    @Story("Удаление папки")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка удаления папки в корзину")
    public void deleteFolderToTrash() {
        folderSteps.createFolder(testFolderPath);
        Response response = folderSteps.deleteFolder(testFolderPath, false);
        folderSteps.validateFolderDeleted(response);
    }

    @Test
    @Story("Удаление папки")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка полного удаления папки")
    public void deleteFolderPermanently() {
        folderSteps.createFolder(testFolderPath);
        Response response = folderSteps.deleteFolder(testFolderPath, true);
        folderSteps.validateFolderDeleted(response);
    }

    @Test
    @Story("Удаление папки")
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверка удаления несуществующей папки")
    public void deleteNonExistentFolder() {
        String nonExistentFolder = folderSteps.generateUniqueFolderName("non_existent_folder");
        Response response = folderSteps.deleteFolder(nonExistentFolder, false);
        folderSteps.validateNotFoundResponse(response);
    }

    @AfterMethod
    @Step("Очистка тестовых данных")
    public void cleanup() {
        try {
            folderSteps.deleteFolder(testFolderPath, true);
        } catch (Exception ignored) {
        }
    }
}
