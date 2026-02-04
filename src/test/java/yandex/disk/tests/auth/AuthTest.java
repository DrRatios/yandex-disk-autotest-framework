package yandex.disk.tests.auth;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import yandex.disk.steps.AuthSteps;
import yandex.disk.tests.BaseTest;

import static yandex.disk.tests.Groups.NEGATIVE;
import static yandex.disk.tests.Groups.POSITIVE;


@Feature("Авторизация YandexDisk API")
public class AuthTest extends BaseTest {

    private AuthSteps authSteps;

    @BeforeMethod
    public void setUp() {
        authSteps = new AuthSteps();
    }

    @Test(groups = POSITIVE)
    @Story("Успешная авторизация")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Проверка авторизации с валидным токеном")
    public void successfulAuthWithValidToken() {
        String token = authSteps.getAuthToken();
        Response response = authSteps.getUserDiskMetaInfo(token);
        authSteps.validateSuccessResponse(response);
    }

    @Test(groups = NEGATIVE)
    @Story("Неуспешная авторизация")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка авторизации с пустым токеном")
    public void failedAuthWithEmptyToken() {
        Response response = authSteps.getUserDiskMetaInfo("");
        authSteps.validateUnauthorizedResponse(response);
    }

    @Test(groups = NEGATIVE)
    @Story("Неуспешная авторизация")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка авторизации с невалидным токеном")
    public void failedAuthWithInvalidToken() {
        Response response = authSteps.getUserDiskMetaInfo("invalid_token_12345");
        authSteps.validateUnauthorizedResponse(response);
    }
}
