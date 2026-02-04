package com.aleksgolds.yandex.disk.helpers;

import io.qameta.allure.Step;
import org.testng.Assert;

import java.util.Collection;

/**
 * Класс кастомных проверок (assertions) с поддержкой Allure-отчетов.
 * Обеспечивает добавление информативных шагов в отчеты Allure при выполнении проверок.
 * Все методы являются потокобезопасными.
 */
public class CustomAssertions {

    /**
     * Проверяет истинность условия с добавлением шага в Allure-отчет.
     */
    @Step("Проверяем истинность условия: {message}")
    public static void assertTrue(boolean condition, String message) {
        Assert.assertTrue(condition, message);
    }

    /**
     * Проверяет равенство двух объектов с добавлением шага в Allure-отчет.
     */
    @Step("Проверяем соответствие объектов друг другу: {message}")
    public static void assertEquals(Object var0, Object var1, String message) {
        Assert.assertEquals(var0, var1, message);
    }

    /**
     * Проверяет равенство двух объектов с добавлением шага в Allure-отчет.
     * Используется стандартное сообщение при неравенстве объектов.
     */
    @Step("Проверяем соответствие объектов друг другу")
    public static void assertEquals(Object var0, Object var1) {
        Assert.assertEquals(var0, var1);
    }

    /**
     * Проверяет, что объект не является null, с добавлением шага в Allure-отчет.
     */
    @Step("Проверяем, что объект не null: {message}")
    public static void assertNotNull(Object object, String message) {
        Assert.assertNotNull(object, message);
    }

    /**
     * Проверяет, что объект не является null, с добавлением шага в Allure-отчет.
     */
    @Step("Проверяем, что объект не null")
    public static void assertNotNull(Object object) {
        Assert.assertNotNull(object);
    }

    /**
     * Проверяет, что коллекция не является null и не пуста, с добавлением шага в Allure-отчет.
     */
    @Step("Проверяем, что коллекция не пуста: {message}")
    public static void assertNotEmpty(Collection<?> collection, String message) {
        Assert.assertNotNull(collection, message);
        Assert.assertFalse(collection.isEmpty(), message);
    }
}
