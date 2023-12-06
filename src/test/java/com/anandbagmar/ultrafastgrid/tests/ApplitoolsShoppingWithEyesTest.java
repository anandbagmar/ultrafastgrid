package com.anandbagmar.ultrafastgrid.tests;

import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static com.anandbagmar.ultrafastgrid.BaseTest.*;

public class ApplitoolsShoppingWithEyesTest {

    @BeforeSuite
    public void beforeSuite() {
        IS_EYES_ENABLED = true;
        runBeforeSuite();
    }

    @AfterSuite(alwaysRun = true)
    public static void afterSuite() {
        runAfterSuite();
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {
        createRunner();
        createDriver(method, BROWSER_NAME);
        createEyes(getDriver(), this.getClass().getSimpleName(), method, true);
    }

    @AfterMethod(alwaysRun = true)
    public synchronized void afterMethod(ITestResult result) {
        if (null != getEyes()) {
            getEyes().closeAsync();
        }
        boolean visualValidationPassed = isVisualValidationPassed(result, getEyes());
        quitDriver(getDriver());
        Assert.assertTrue(visualValidationPassed, "Visual differences found");
    }

    @Test(description = "Add to cart")
    public void addToCartWithEyes_2() {
        String url = "https://demo.applitools.com/tlcHackathonMasterV1.html";
        WebDriver driver = getDriver();
        Eyes eyes = getEyes();
        driver.get(url);
        By product1Id = By.id("product_1");
        eyes.check("onLoad-MultipleValidations", Target.window().fully()
                .strict(product1Id)
                .layout(By.id("filter_col"), By.id("A__cartbt__49")));
        driver.findElement(product1Id).click();

        if (isInject()) {
            ((JavascriptExecutor) driver).executeScript("document.querySelector(\"input#INPUTtext____42\").setAttribute(\"placeholder\", \"Search shoes\")");
            ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div#DIV__btnaddtoca__113\").setAttribute(\"id\", \"foo\")");
        }
        By INPU_TTEXT____42 = By.id("INPUTtext____42");
        eyes.check("product_1_MultipleValidations", Target.window().fully()
                .strict()
                .layout(INPU_TTEXT____42));
        driver.findElement(By.id("DIV__btnaddtoca__113")).click();
        eyes.checkWindow("add to cart");
    }

    @Test(description = "Add to cart1")
    public void addToCartWithEyes_1() {
        String url = "https://demo.applitools.com/tlcHackathonMasterV1.html";
        WebDriver driver = getDriver();
        Eyes eyes = getEyes();
        driver.get(url);
        By product1Id = By.id("product_1");
        eyes.check("onLoad-MultipleValidations-1", Target.window().fully()
                .strict(product1Id)
                .layout(By.id("filter_col"), By.id("A__cartbt__49")));
        driver.findElement(product1Id).click();

        if (isInject()) {
            ((JavascriptExecutor) driver).executeScript("document.querySelector(\"input#INPUTtext____42\").setAttribute(\"placeholder\", \"Search shoes\")");
            ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div#DIV__btnaddtoca__113\").setAttribute(\"id\", \"foo\")");
        }
        By INPU_TTEXT____42 = By.id("INPUTtext____42");
        eyes.check("product_1_MultipleValidations-1", Target.window().fully()
                .strict()
                .layout(INPU_TTEXT____42));
        driver.findElement(By.id("DIV__btnaddtoca__113")).click();
        eyes.checkWindow("add to cart-1");
    }
}
