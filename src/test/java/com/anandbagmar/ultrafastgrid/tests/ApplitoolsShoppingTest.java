package com.anandbagmar.ultrafastgrid.tests;

import com.anandbagmar.ultrafastgrid.BaseTest;
import com.applitools.eyes.selenium.fluent.Target;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class ApplitoolsShoppingTest extends BaseTest {

    private final String appName = "applitools-shopping";

    @BeforeSuite
    public void beforeSuite() {
        setupBeforeSuite(appName);
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {
        setupBeforeMethod(method, true);
    }

    @Test(description = "Add to cart")
    public void addToCart11() {
        String url = "https://demo.applitools.com/tlcHackathonMasterV1.html";
        driver.get(url);
        eyes.checkWindow("onLoad");
        By product1Id = By.id("product_1");
        eyes.check("onLoad-MultipleValidations", Target.window().fully()
                .strict(product1Id)
                .layout(By.id("filter_col"), By.id("A__cartbt__49")));
        driver.findElement(product1Id).click();

        if (isInject()) {
            ((JavascriptExecutor) driver).executeScript("document.querySelector(\"input#INPUTtext____42\").setAttribute(\"placeholder\", \"Search shoes\")");
            ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div#DIV__btnaddtoca__113\").setAttribute(\"id\", \"foo\")");
        }
        eyes.checkWindow("product_1");
        eyes.check("product_1_Strict", Target.window().fully()
                .strict());
        eyes.check("product_1_Layout", Target.window().fully()
                .layout());
        By INPU_TTEXT____42 = By.id("INPUTtext____42");
        eyes.check("product_1_MultipleValidations", Target.window().fully()
                .strict()
                .layout(INPU_TTEXT____42));
        eyes.checkElement(INPU_TTEXT____42, "INPUTtext____42-checkElement");
        driver.findElement(By.id("DIV__btnaddtoca__113")).click();
        eyes.checkWindow("add to cart");
    }
}
