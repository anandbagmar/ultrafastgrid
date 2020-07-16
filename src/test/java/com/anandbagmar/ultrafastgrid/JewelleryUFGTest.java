package com.anandbagmar.ultrafastgrid;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.selenium.Eyes;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class JewelleryUFGTest extends BaseTest {
    private final String siteName = "jewellery-eyes-ufg-test";
    RectangleSize viewportSizeWeb = new RectangleSize(1024, 768);
    RectangleSize viewportSizeMWeb = new RectangleSize(360, 480);
    private static BatchInfo batch;

    @BeforeClass
    public void beforeClass() {
        batch = new BatchInfo(siteName);
        System.out.println("JewelleryUFGTest: BeforeClass: Batch name: '" + siteName + "'");
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        if (method.getName().toLowerCase().contains("mweb")) {
            setupBeforeMethod(siteName, method, viewportSizeMWeb, true, batch);
        } else {
            setupBeforeMethod(siteName, method, viewportSizeWeb, true, batch);
        }
    }

    @Test(description = "Zales Necklaces, Web")
    public void zalesNecklacesWeb() {
        runNecklaceTest("zalesNecklacesWeb", viewportSizeWeb);
    }

    @Test(description = "Zales Necklaces, MWeb")
    public void zalesNecklacesMWeb() {
        runNecklaceTest("zalesNecklacesMWeb", viewportSizeMWeb);
    }

    private void runNecklaceTest(String testName, RectangleSize viewportSize) {
        System.out.println("Running test: " + testName);
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
}
