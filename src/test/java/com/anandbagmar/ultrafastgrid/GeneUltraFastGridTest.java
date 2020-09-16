package com.anandbagmar.ultrafastgrid;

import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.selenium.Eyes;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class GeneUltraFastGridTest extends BaseTest {
    private final String appName = "gene-eyes-ufg-test";
    RectangleSize viewportSize = new RectangleSize(1024, 768);

//    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {
        setupBeforeMethod(appName, method, viewportSize, true, true);
    }

//    @Test(description = "Gene - 1st build, with Eyes")
//    public void geneFirstBuildNoEyes() {
//        Eyes eyes = getEyes();
//        WebDriver driver = getDriver();
//
//        String url = "https://www.gene.com/";
//        driver.get(url);
//        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"a[href='/good/diversity-inclusion']\").text = \"Diversity & Inclusion!\"");
//        eyes.checkWindow("Home");
//
//        driver.get("https://www.gene.com/diversity-inclusion");
//        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"svg.icon--linkedin\").style=\"display: none;\"");
//        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"a[href='/good/diversity-inclusion/foster-belonging']\").text = \"Learn Now\"");
//        eyes.checkWindow("Diversity & Inclusion");
//
//        driver.findElement(By.cssSelector("a[href='/good/diversity-inclusion/foster-belonging']")).click();
//        eyes.checkWindow("Foster Belonging");
//    }

//    @Test(description = "Gene - 2nd build, with Eyes")
    public void geneFirstBuildNoEyes() {
        Eyes eyes = getEyes();
        WebDriver driver = getDriver();

        String url = "https://www.gene.com/";
        driver.get(url);
        eyes.checkWindow("Home");

        driver.get("https://www.gene.com/diversity-inclusion");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"svg.icon--linkedin\").style=\"display: none;\"");
        eyes.checkWindow("Diversity & Inclusion");

        driver.findElement(By.cssSelector("a[href='/good/diversity-inclusion/foster-belonging']")).click();
        eyes.checkWindow("Foster Belonging");
    }
}
