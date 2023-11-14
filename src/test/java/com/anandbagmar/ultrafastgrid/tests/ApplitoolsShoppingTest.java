package com.anandbagmar.ultrafastgrid.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

import static com.anandbagmar.ultrafastgrid.BaseTest.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ApplitoolsShoppingTest {
    @BeforeSuite
    public void beforeSuite() {
        IS_EYES_ENABLED = false;
        runBeforeSuite();
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {
        createDriver(method, "chrome");
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {
        quitDriver(getDriver());
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        runAfterSuite();
    }

    @Test(description = "Add to cart")
    public void addToCart() {
        WebDriver driver = getDriver();
        String url = "https://demo.applitools.com/tlcHackathonMasterV1.html";
        driver.get(url);
        String product1Name = driver.findElement(By.id("DIV__colcolmd__210")).getText();
        String expectedShoeName = "Appli Air x Night";
        boolean productNameFound = product1Name.contains(expectedShoeName);
        assertTrue(productNameFound, "Product 1 name is incorrect");
        By product1Id = By.id("product_1");
        driver.findElement(product1Id).click();
        String shoeName = driver.findElement(By.id("shoe_name")).getText();
        assertEquals(shoeName, expectedShoeName, "Product page has incorrect product name");
        if (isInject()) {
            ((JavascriptExecutor) driver).executeScript("document.querySelector(\"input#INPUTtext____42\").setAttribute(\"placeholder\", \"Search shoes\")");
            ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div#DIV__btnaddtoca__113\").setAttribute(\"id\", \"foo\")");
        }
        int numberOfProductsInCartBeforeUpdating = Integer.parseInt(driver.findElement(By.id("STRONG____50")).getText());
        driver.findElement(By.id("DIV__btnaddtoca__113")).click();
        int numberOfProductsInCartAfterUpdating = Integer.parseInt(driver.findElement(By.id("STRONG____50")).getText());
        assertEquals(numberOfProductsInCartAfterUpdating - numberOfProductsInCartBeforeUpdating, 0, "Number of products added to cart didn't update");
    }
}
