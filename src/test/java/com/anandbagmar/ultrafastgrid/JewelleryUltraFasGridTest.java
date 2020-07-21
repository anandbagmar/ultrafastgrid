package com.anandbagmar.ultrafastgrid;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.selenium.Eyes;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class JewelleryUltraFasGridTest extends BaseTest {
    private final String appName = "jewellery-eyes-ufg-test";
    RectangleSize viewportSizeWeb = new RectangleSize(1024, 768);
    private static BatchInfo batch;

    @BeforeClass
    public void beforeClass() {
        batch = new BatchInfo(appName);
        batch.setNotifyOnCompletion(false);
        System.out.println("JewelleryUltraFasGridTest: BeforeClass: App name: '" + appName + "', Batch name: '" + batch.getName() + "', BatchID: " + batch.getId());
    }

    @AfterClass
    public void afterClass() {
        System.out.println("JewelleryUltraFasGridTest: AfterClass: App name: '" + appName + "', Batch name: '" + batch.getName() + "', BatchID: " + batch.getId());
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {
        setupBeforeMethod(appName, method, viewportSizeWeb, true, batch);
    }

    @Test(description = "Zales Necklaces, Web")
    public void zalesNecklacesWeb() {
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

        eyes.checkWindow("Necklaces");
    }

    @Test(description = "Zales - Contact-Us")
    public void zalesContactUsWeb() {
        System.out.println("Running test: zalesNecklacesWeb");
        Eyes eyes = getEyes();
        WebDriver driver = getDriver();

        String url = "https://www.zales.com/contact-us";
        driver.get(url);
        eyes.checkWindow("Contact-Us");

        waitFor(30);
        String popup1 = "//button[@class = 'con-x']";
        closePopup(driver, popup1);

        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.syte-tooltip\").querySelector(\"span.syte-header\").textContent=\"VISUAL SEARCH\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.syte-tooltip\").querySelector(\"span.syte-header\").color=\"red\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"h1\").textContent=\"Contact Z@les\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.col-xs-12.content-landing.virtual-appt\").querySelector(\"h1\").textContent=\"We are here for you\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.col-xs-12.content-landing.virtual-appt\").querySelector(\"h1\").style.color=\"blue\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.col-xs-12.col-sm-4\").querySelector(\"h2\").textContent=\"Speak live with A JEWELRY EXPERT\"");

        eyes.checkWindow("Contact-Us-Modified");

        driver.findElement(By.xpath("//div[@class = 'menu-open']")).click();
        waitFor(3);
        eyes.checkWindow("Menu");
    }

    @Test(description = "Zales - collections")
    public void zalesCollections() {
        System.out.println("Running test: zalesCollections");
        Eyes eyes = getEyes();
        WebDriver driver = getDriver();

        String url = "https://www.zales.com/collections";
        driver.get(url);
        eyes.checkWindow("Collections");

        waitFor(35);
//        String popup1 = "//button[@class = 'con-x']";
//        closePopup(driver, popup1);

        waitFor(3);
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.m-0\").style = \"display: none;\"");
        eyes.checkWindow("Changed Image");
        waitFor(3);
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.m-0\").style = \"display: block;\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelectorAll(\"div.fullwidthbanner.section-break\").forEach(f => { f.style = \"display: none;\" })");
        eyes.checkWindow("Banners");

    }

    private void closePopup(WebDriver driver, String locator) {
        WebElement closePopup = driver.findElement(By.xpath(locator));
        if (null != closePopup) {
            System.out.println("popup found");
            closePopup.click();
            waitFor(3);
        } else {
            System.out.println("popup not found");
        }
    }

}
