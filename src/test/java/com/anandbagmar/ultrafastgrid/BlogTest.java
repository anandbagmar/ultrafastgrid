package com.anandbagmar.ultrafastgrid;

import Utilities.DriverUtils;
import com.applitools.eyes.*;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;

public class BlogTest extends BaseTest {
    LocalDateTime before;
    LocalDateTime after;
    private int numOfBrowsers;
    private int numOfTests;

    @BeforeClass
    public void setUp() {
        before = LocalDateTime.now();
        numOfBrowsers = 0;
        numOfTests = 0;
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        String browser = "chrome";
        if (method.getName().contains("firefox")) {
            browser = "firefox";
        }
        System.out.println("Running test with browser - " + browser);
        numOfTests++;
        switch (browser) {
            case "chrome":
                DriverUtils.getPathForChromeDriverFromMachine();
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                driver = new ChromeDriver(options);
                break;
            case "firefox":
                DriverUtils.getPathForFirefoxDriverFromMachine();
                driver = new FirefoxDriver();
                break;
            default:
                driver = new ChromeDriver();
        }
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        quitDriver();
    }

    @AfterClass
    public void tearDown() {
        after = LocalDateTime.now();
        long seconds = Duration.between(before, after).toMillis() / 1000;
        System.out.println(">>> " + BlogTest.class.getSimpleName() + " - " + numOfTests + " Tests took '" + seconds + "' seconds to run for '" + numOfBrowsers + "' configurations <<<");
    }

    @Test(description = "Blogs in 2019")
    public void blogIn2019() {
        String url = "https://essenceoftesting.blogspot.com";
        checkBlogPages(url);
    }

    @Test(description = "Blogs in 2019 - Mobile View")
    public void blogIn2019MobileView() {
        String url = "https://essenceoftesting.blogspot.com/?m=1";
        checkBlogPages(url);
    }

    @Test(description = "Blog profile")
    public void seeProfile() {
        checkProfilePage();
    }

    @Test(description = "Blogs in 2019")
    public void blogIn2019_firefox() {
        String url = "https://essenceoftesting.blogspot.com";
        checkBlogPages(url);
    }

    @Test(description = "Blogs in 2019 - Mobile View")
    public void blogIn2019MobileView_firefox() {
        String url = "https://essenceoftesting.blogspot.com/?m=1";
        checkBlogPages(url);
    }

    @Test(description = "Blog profile")
    public void seeProfile_firefox() {
        checkProfilePage();
    }
}
