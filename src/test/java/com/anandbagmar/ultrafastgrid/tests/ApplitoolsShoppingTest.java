package com.anandbagmar.ultrafastgrid.tests;

import com.anandbagmar.ultrafastgrid.BaseTest;
import com.applitools.eyes.selenium.Eyes;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class ApplitoolsShoppingTest extends BaseTest {

    private final String appName = "applitools-shopping";

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {
        setupBeforeMethod(appName, method, true);
    }

    @Test(description = "Add to cart")
    public void addToCart() {
        Eyes eyes = getEyes();
        WebDriver driver = getDriver();

        String url = "https://demo.applitools.com/tlchackathonmasterv1";
        driver.get(url);
        eyes.checkWindow("onLoad");
        driver.findElement(By.id("product_1")).click();

        eyes.checkWindow("product_1");
        if (isInject()) {
            ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div#DIV__btnaddtoca__113\").setAttribute(\"id\", \"foo\")");
        }
        driver.findElement(By.id("DIV__btnaddtoca__113")).click();
        eyes.checkWindow("add to cart");
        waitFor(5);
    }
}
