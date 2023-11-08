package com.anandbagmar.ultrafastgrid.tests;

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
import static com.anandbagmar.ultrafastgrid.BaseTest.isInject;
import static com.anandbagmar.ultrafastgrid.BaseTest.runAfterMethod;
import static com.anandbagmar.ultrafastgrid.BaseTest.runAfterSuite;
import static com.anandbagmar.ultrafastgrid.BaseTest.runBeforeMethod;
import static com.anandbagmar.ultrafastgrid.BaseTest.runBeforeSuite;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ApplitoolsShoppingTest {

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
    public void addToCart() {
        String url = "https://demo.applitools.com/tlcHackathonMasterV1.html";
        myDriver().get(url);
        String product1Name = myDriver().findElement(By.id("DIV__colcolmd__210")).getText();
        String expectedShoeName = "Appli Air x Night";
        boolean productNameFound = product1Name.contains(expectedShoeName);
        assertTrue(productNameFound, "Product 1 name is incorrect");
        By product1Id = By.id("product_1");
        myDriver().findElement(product1Id).click();
        String shoeName = myDriver().findElement(By.id("shoe_name")).getText();
        assertEquals(shoeName, expectedShoeName, "Product page has incorrect product name");
        if (isInject()) {
            ((JavascriptExecutor) myDriver()).executeScript("document.querySelector(\"input#INPUTtext____42\").setAttribute(\"placeholder\", \"Search shoes\")");
            ((JavascriptExecutor) myDriver()).executeScript("document.querySelector(\"div#DIV__btnaddtoca__113\").setAttribute(\"id\", \"foo\")");
        }
        int numberOfProductsInCartBeforeUpdating = Integer.parseInt(myDriver().findElement(By.id("STRONG____50")).getText());
        myDriver().findElement(By.id("DIV__btnaddtoca__113")).click();
        int numberOfProductsInCartAfterUpdating = Integer.parseInt(myDriver().findElement(By.id("STRONG____50")).getText());
        assertEquals(numberOfProductsInCartAfterUpdating - numberOfProductsInCartBeforeUpdating, 0, "Number of products added to cart didn't update");
    }
}
