package com.anandbagmar.ultrafastgrid.tests;

import com.anandbagmar.ultrafastgrid.BaseTest;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.selenium.Eyes;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class ApplitoolsShoppingTest extends BaseTest {

    private final String appName = "applitools-shopping-eyes-ufg-test";
    private RectangleSize viewportSize = new RectangleSize(1024, 768);
    private boolean isDisabled = false;
    private String browser = "self_healing";
//    private String browser = "chrome";

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {
        setupBeforeMethod(appName, method, viewportSize, true, isDisabled, browser);
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
//        System.out.println("before change: " + driver.findElement(By.id("DIV__colxlcollg__112")).getAttribute("innerHTML"));
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div#DIV__btnaddtoca__113\").setAttribute(\"id\", \"foo\")");
//        System.out.println("after change: " + driver.findElement(By.id("DIV__colxlcollg__112")).getAttribute("innerHTML"));
        driver.findElement(By.id("DIV__btnaddtoca__113")).click();
        eyes.checkWindow("add to cart");
    }
}
