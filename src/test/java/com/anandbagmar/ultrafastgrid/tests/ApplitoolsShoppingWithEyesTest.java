package com.anandbagmar.ultrafastgrid.tests;

import com.applitools.eyes.selenium.fluent.Target;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static com.anandbagmar.ultrafastgrid.BaseTest.myDriver;
import static com.anandbagmar.ultrafastgrid.BaseTest.myEyes;
import static com.anandbagmar.ultrafastgrid.BaseTest.isInject;
import static com.anandbagmar.ultrafastgrid.BaseTest.runAfterMethod;
import static com.anandbagmar.ultrafastgrid.BaseTest.runAfterSuite;
import static com.anandbagmar.ultrafastgrid.BaseTest.runBeforeMethod;
import static com.anandbagmar.ultrafastgrid.BaseTest.runBeforeSuite;

public class ApplitoolsShoppingWithEyesTest {

    @BeforeSuite
    public void beforeSuite() {
        runBeforeSuite();
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {
        runBeforeMethod(this.getClass().getSimpleName(), method, true);
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {
        runAfterMethod(result);
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        runAfterSuite();
    }

    @Test(description = "Add to cart")
    public void addToCartWithEyes() {
        String url = "https://demo.applitools.com/tlcHackathonMasterV1.html";
        myDriver().get(url);
        By product1Id = By.id("product_1");
        myEyes().check("onLoad-MultipleValidations", Target.window().fully()
                .strict(product1Id)
                .layout(By.id("filter_col"), By.id("A__cartbt__49")));
        myDriver().findElement(product1Id).click();

        if (isInject()) {
            ((JavascriptExecutor) myDriver()).executeScript("document.querySelector(\"input#INPUTtext____42\").setAttribute(\"placeholder\", \"Search shoes\")");
            ((JavascriptExecutor) myDriver()).executeScript("document.querySelector(\"div#DIV__btnaddtoca__113\").setAttribute(\"id\", \"foo\")");
        }
        By INPU_TTEXT____42 = By.id("INPUTtext____42");
        myEyes().check("product_1_MultipleValidations", Target.window().fully()
                .strict()
                .layout(INPU_TTEXT____42));
        myDriver().findElement(By.id("DIV__btnaddtoca__113")).click();
        myEyes().checkWindow("add to cart");
    }
}
