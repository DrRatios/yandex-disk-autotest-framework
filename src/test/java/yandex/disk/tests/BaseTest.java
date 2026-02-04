package yandex.disk.tests;

import com.aleksgolds.yandex.disk.api.YandexDiskApiClient;
import org.testng.annotations.BeforeTest;

import static com.aleksgolds.yandex.disk.properties.Properties.testProperties;

public class BaseTest {
    /**
     * Инициализация токена и спецификации запросов перед тестами.
     */
    @BeforeTest
    public void initSpec() {
        YandexDiskApiClient.initSpec(testProperties.yandexDiskMainUrl());
    }

}
