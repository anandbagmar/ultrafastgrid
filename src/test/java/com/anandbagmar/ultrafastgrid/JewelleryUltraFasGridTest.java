package com.anandbagmar.ultrafastgrid;

import com.applitools.eyes.*;
import com.applitools.eyes.selenium.*;
import org.openqa.selenium.*;

import java.lang.reflect.*;

public class JewelleryUltraFasGridTest extends BaseTest {
    private final String appName = "zales-eyes-ufg-test";
    RectangleSize viewportSizeWeb = new RectangleSize(1024, 768);

    //    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {
        setupBeforeMethod(appName, method, viewportSizeWeb, true, true);
    }

    //    @Test(description = "Zales Necklaces, Web")
    public void zalesNecklacesWeb() {
        System.out.println("Running test: zalesNecklacesWeb");
        Eyes eyes = getEyes();
        WebDriver driver = getDriver();

        String url = "https://www.zales.com/necklaces";
        driver.get(url);
        eyes.checkWindow("Necklaces-before wait");
        waitFor(25);
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"h1.text-center.content-hdr\").style.color=\"red\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"h1.text-center.content-hdr\").innerText = \"Necklace\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.section-break.row.subsection.text-center\").querySelector(\"h3\").innerText = \"STYLISH DESIGNS FOR EVERY 1\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.exclusions.dark-text\").style = \"display: none;\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.exclusions.dark-text\").style = \"display: compact;\"");

        eyes.checkWindow("Necklaces");
    }

    //    @Test(description = "Zales - Contact-Us")
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

    //    @Test(description = "Zales - collections")
    public void zalesCollections() {
        System.out.println("Running test: zalesCollections");
        Eyes eyes = getEyes();
        WebDriver driver = getDriver();

        String url = "https://www.zales.com/collections";
        driver.get(url);
        eyes.checkWindow("Collections");

        waitFor(15);
//        String popup1 = "//button[@class = 'con-x']";
//        closePopup(driver, popup1);

//        waitFor(3);
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.m-0\").style = \"display: none;\"");
        eyes.checkWindow("Changed Image");
        waitFor(3);
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.m-0\").style = \"display: block;\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelectorAll(\"div.fullwidthbanner.section-break\").forEach(f => { f.style = \"display: none;\" })");
        eyes.checkWindow("Banners");

    }

    //    @Test(description = "Zales - navigation")
    public void zalesNavigation() {
        System.out.println("Running test: zalesNavigation");
        Eyes eyes = getEyes();
        WebDriver driver = getDriver();

        driver.get("https://www.zales.com");
        waitFor(10);
        eyes.checkWindow("Home");

        driver.get("https://www.zales.com/watches");
        waitFor(3);
        eyes.checkWindow("Watches");

        driver.get("https://www.zales.com/watches/movado/c/0108010800");
        waitFor(3);
        eyes.checkWindow("Watches-Movado");

        driver.get("https://www.zales.com/compare");
        waitFor(3);
        eyes.checkWindow("Compare");
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
