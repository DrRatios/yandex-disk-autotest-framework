package com.aleksgolds.yandex.disk.properties;

import org.aeonbits.owner.ConfigFactory;

/**
 * Класс для доступа к свойствам тестовой конфигурации.
 * Предоставляет статический доступ к настройкам тестов через интерфейс TestsProperties.
 */
public class Properties {

    /**
     * Экземпляр интерфейса тестовых свойств, инициализируемый через библиотеку Owner.
     * Содержит конфигурационные параметры для выполнения тестов.
     */
    public static TestProperties testProperties = ConfigFactory.create(TestProperties.class);
}