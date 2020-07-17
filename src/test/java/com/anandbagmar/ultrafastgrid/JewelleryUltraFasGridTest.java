package com.anandbagmar.ultrafastgrid;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.selenium.Eyes;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class JewelleryUltraFasGridTest extends BaseTest {
    private final String appName = "jewellery-eyes-ufg-test";
    RectangleSize viewportSizeWeb = new RectangleSize(1024, 768);
    RectangleSize viewportSizeMWeb = new RectangleSize(360, 480);
    private static BatchInfo batch;

    @BeforeClass
    public void beforeClass() {
        batch = new BatchInfo(appName);
        batch.setNotifyOnCompletion(false);
        System.out.println("JewelleryUltraFasGridTest: BeforeClass: App name: '" + appName + "', Batch name: '" + batch.getName() + "', BatchID: " + batch.getId());
    }

    @AfterClass
    public void afterClass() {
//        batch.setCompleted(true);
        System.out.println("JewelleryUltraFasGridTest: AfterClass: App name: '" + appName + "', Batch name: '" + batch.getName() + "', BatchID: " + batch.getId());
    }

    @BeforeMethod (alwaysRun = true)
    public void beforeMethod(Method method) {
        if (method.getName().toLowerCase().contains("mweb")) {
            setupBeforeMethod(appName, method, viewportSizeMWeb, true, batch);
        } else {
            setupBeforeMethod(appName, method, viewportSizeWeb, true, batch);
        }
    }

    @Test(description = "Zales Necklaces, Web")
    public void zalesNecklacesWeb() {
//        runNecklaceTest("zalesNecklacesWeb", viewportSizeWeb);
        System.out.println("Running test: zalesNecklacesWeb");
        Eyes eyes = getEyes();
        WebDriver driver = getDriver();

        String url = "https://www.zales.com/necklaces";
        driver.get(url);
        waitFor(20);
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"h1.text-center.content-hdr\").style.color=\"red\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"h1.text-center.content-hdr\").innerText = \"Necklace\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.section-break.row.subsection.text-center\").querySelector(\"h3\").innerText = \"STYLISH DESIGNS FOR EVERY 1\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.exclusions.dark-text\").style = \"display: none;\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.exclusions.dark-text\").style = \"display: compact;\"");
//        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.owl-item\").querySelector(\"div.promo-text_description\").textContent = \"Discount Applied Automatically\"");

        eyes.checkWindow("Necklaces");
    }

    @Test(description = "Zales Necklaces, MWeb")
    public void zalesNecklacesMWeb() {
//        runNecklaceTest("zalesNecklacesMWeb", viewportSizeMWeb);
        System.out.println("Running test: zalesNecklacesMWeb");
        Eyes eyes = getEyes();
        WebDriver driver = getDriver();

        String url = "https://www.zales.com/necklaces";
        driver.get(url);
        waitFor(20);
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"h1.text-center.content-hdr\").style.color=\"red\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"h1.text-center.content-hdr\").innerText = \"Necklace\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.section-break.row.subsection.text-center\").querySelector(\"h3\").innerText = \"STYLISH DESIGNS FOR EVERY 1\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.exclusions.dark-text\").style = \"display: none;\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.exclusions.dark-text\").style = \"display: compact;\"");
//        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.owl-item\").querySelector(\"div.promo-text_description\").textContent = \"Discount Applied Automatically\"");

        eyes.checkWindow("Necklaces");
    }

//    private void runNecklaceTest(String testName, RectangleSize viewportSize) {
//        System.out.println("Running test: " + testName);
//        Eyes eyes = getEyes();
//        WebDriver driver = getDriver();
//
//        String url = "https://www.zales.com/necklaces";
//        driver.get(url);
//        waitFor(20);
//        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"h1.text-center.content-hdr\").style.color=\"red\"");
//        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"h1.text-center.content-hdr\").innerText = \"Necklace\"");
//        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.section-break.row.subsection.text-center\").querySelector(\"h3\").innerText = \"STYLISH DESIGNS FOR EVERY 1\"");
//        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.exclusions.dark-text\").style = \"display: none;\"");
//        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.exclusions.dark-text\").style = \"display: compact;\"");
////        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.owl-item\").querySelector(\"div.promo-text_description\").textContent = \"Discount Applied Automatically\"");
//
//        eyes.checkWindow("Necklaces");
//    }
}
