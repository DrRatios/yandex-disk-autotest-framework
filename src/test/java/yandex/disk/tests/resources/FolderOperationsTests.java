package yandex.disk.tests.resources;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import yandex.disk.steps.FolderSteps;
import yandex.disk.tests.BaseTest;

import static yandex.disk.tests.Groups.NEGATIVE;
import static yandex.disk.tests.Groups.POSITIVE;

public class FolderOperationsTests extends BaseTest {
    private FolderSteps folderSteps;
    private String testFolderPath;

    @DataProvider(name = "invalidFolderPaths")
    public Object[][] invalidFolderPaths() {
        return new Object[][]{
                {""},
                {" "},
                {"///"},
                {"//invalid//path"},
                {"../folder"},
                {null}
        };
    }

    @BeforeMethod
    @Step("Подготовка тестовых данных")
    public void setupTestData() {
        folderSteps = new FolderSteps();
        testFolderPath = folderSteps.generateUniqueFolderName("test_folder");
    }

    @Test(groups = POSITIVE)
    @Story("Создание папки")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка создания новой папки")
    public void createNewFolder() {
        Response response = folderSteps.createFolder(testFolderPath);
        folderSteps.validateFolderCreated(response);
    }

    @Test(groups = NEGATIVE)
    @Story("Создание папки")
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверка создания существующей папки")
    public void createExistingFolder() {
        folderSteps.createFolder(testFolderPath);
        Response response = folderSteps.createFolder(testFolderPath);
        folderSteps.validateConflictResponse(response);
    }

    @Test(groups = POSITIVE)
    @Story("Удаление папки")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка удаления папки в корзину")
    public void deleteFolderToTrash() {
        folderSteps.createFolder(testFolderPath);
        Response response = folderSteps.deleteFolder(testFolderPath, false);
        folderSteps.validateFolderDeleted(response);
    }

    @Test(groups = POSITIVE)
    @Story("Удаление папки")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка полного удаления папки")
    public void deleteFolderPermanently() {
        folderSteps.createFolder(testFolderPath);
        Response response = folderSteps.deleteFolder(testFolderPath, true);
        folderSteps.validateFolderDeleted(response);
    }

    @Test(groups = NEGATIVE)
    @Story("Удаление папки")
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверка удаления несуществующей папки")
    public void deleteNonExistentFolder() {
        String nonExistentFolder = folderSteps.generateUniqueFolderName("non_existent_folder");
        Response response = folderSteps.deleteFolder(nonExistentFolder, false);
        folderSteps.validateNotFoundResponse(response);
    }

    @Test(groups = NEGATIVE, dataProvider = "invalidFolderPaths")
    @Story("Создание папки")
    @Severity(SeverityLevel.NORMAL)
    @Description("Попытка создания папки с невалидным path")
    public void createFolderWithInvalidPath(String path) {
        Response response = folderSteps.createFolder(path);
        folderSteps.validateClientError(response);
    }

    @Test(groups = NEGATIVE, dataProvider = "invalidFolderPaths")
    @Story("Удаление папки")
    @Severity(SeverityLevel.NORMAL)
    @Description("Попытка удаления папки с невалидным path")
    public void deleteFolderWithInvalidPath(String path) {
        Response response = folderSteps.deleteFolder(path, false);
        folderSteps.validateClientError(response);
    }

    @AfterMethod(alwaysRun = true)
    @Step("Очистка тестовых данных")
    public void cleanup() {
        try {
            folderSteps.deleteFolder(testFolderPath, true);
        } catch (Exception ignored) {
        }
    }
}
