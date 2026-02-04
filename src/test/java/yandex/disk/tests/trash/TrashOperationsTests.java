package yandex.disk.tests.trash;

import io.qameta.allure.*;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import yandex.disk.steps.FolderSteps;
import yandex.disk.steps.TrashSteps;
import yandex.disk.tests.BaseTest;

@Slf4j
@Feature("Операции с корзиной")
public class TrashOperationsTests extends BaseTest {

    private TrashSteps trashSteps;
    private FolderSteps folderSteps;
    private String testFolderPath;

    @BeforeMethod
    @Step("Подготовка тестовых данных")
    public void setupTestData() {
        trashSteps = new TrashSteps();
        folderSteps = new FolderSteps();

        testFolderPath = trashSteps.generateUniqueFolderName("test_folder");

        folderSteps.createFolder(testFolderPath);
    }

    @Test
    @Story("Получение информации о корзине")
    @Severity(SeverityLevel.NORMAL)
    @Description("Простая проверка получения информации о корзине")
    public void getTrashContents() {
        Response response = trashSteps.getTrashHash();
        trashSteps.validateTrashResponse(response);
    }

    @Test
    @Story("Очистка корзины")
    @Severity(SeverityLevel.NORMAL)
    @Description("Простая проверка очистки корзины")
    public void clearTrash() {
        Response response = trashSteps.clearTrash();
        trashSteps.validateTrashCleared(response);
    }

    @Test
    @Story("Очистка корзины")
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверка очистки пустой корзины")
    public void clearEmptyTrash() {
        trashSteps.clearTrash();

        Response clearResponse = trashSteps.clearTrash();
        trashSteps.validateTrashCleared(clearResponse);
    }

    @AfterMethod
    @Step("Очистка тестовых данных")
    public void cleanup() {
        try {
            trashSteps.deleteFolderPermanently(testFolderPath);
        } catch (Exception ignored) {
        }
    }

}