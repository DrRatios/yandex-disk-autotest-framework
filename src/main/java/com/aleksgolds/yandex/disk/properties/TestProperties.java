package com.aleksgolds.yandex.disk.properties;

import org.aeonbits.owner.Config;

/**
 * Интерфейс для доступа к конфигурационным параметрам тестов.
 * Определяет источники конфигурации и методы для получения значений свойств.
 * @version 1.0
 */
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "system:env",
        "file:src/main/resources/test.properties",
        "file:src/main/resources/secrets.properties"
})
public interface TestProperties extends Config {

    @Key("yandex.disk.main.url")
    String yandexDiskMainUrl();

    @Key("clientId")
    String clientId();

    @Key("login")
    String login();

    @Key("token")
    String token();


}