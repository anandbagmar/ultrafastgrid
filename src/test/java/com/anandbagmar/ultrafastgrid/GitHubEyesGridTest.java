package com.anandbagmar.ultrafastgrid;

import Utilities.DriverUtils;
import com.applitools.eyes.*;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;

public class GitHubEyesGridTest extends BaseTest {
    private Eyes eyes;
    private EyesRunner runner;
    private String siteName = "blog-eyes";
    private String url = "https://github.com";
    LocalDateTime before;
    LocalDateTime after;
    private int numOfBrowsers;
    private int numOfTests;

    @BeforeClass
    public void setUp() {
        before = LocalDateTime.now();
        numOfBrowsers = 0;
        numOfTests = 0;
        batch = new BatchInfo("blog - SeGrid");
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        String browser = "chrome";
        if (method.getName().contains("firefox")) {
            browser = "firefox";
        }
        WebDriver innerDriver = null;
        System.out.println("Running test with browser - " + browser);
        numOfTests++;
        innerDriver = createDriver(browser);

        RectangleSize viewportSize = new RectangleSize(1024, 768);
        driver = null;
        runner = new ClassicRunner();
        eyes = configureEyes(runner);

        driver = eyes.open(innerDriver, siteName, method.getName(), viewportSize);
    }

    private WebDriver createDriver(String browser) {
        WebDriver innerDriver = null;
        try {
            switch (browser) {
                case "chrome":
                    DriverUtils.getPathForChromeDriverFromMachine();
                    innerDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), new ChromeOptions());
                    break;
                case "firefox":
                    DriverUtils.getPathForFirefoxDriverFromMachine();
                    innerDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), new FirefoxOptions());
                    break;
                default:
                    innerDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), new ChromeOptions());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return innerDriver;
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        quitDriver();
        eyes.closeAsync();
    }

    @AfterClass
    public void tearDown() {
        TestResultsSummary allTestResults = runner.getAllTestResults(false);
        TestResultContainer[] results = allTestResults.getAllResults();
        for (int i = 0; i < results.length; i++) {
            Throwable ex = results[0].getException();
            TestResults testResult = results[i].getTestResults();
            handleTestResults(ex, testResult);
        }
        after = LocalDateTime.now();
        long seconds = Duration.between(before, after).toMillis() / 1000;
        System.out.println(">>> " + GitHubEyesGridTest.class.getSimpleName() + " - " + numOfTests + " Tests took '" + seconds + "' seconds to run for '" + numOfBrowsers + "' configurations <<<");
    }

    @Test(description = "Blogs in 2019")
    public void blogIn2019() {
        String url = "https://essenceoftesting.blogspot.com";
        eyes.setForceFullPageScreenshot(false);
        checkBlogPages(eyes, url);
    }

    @Test(description = "Blogs in 2019 - Mobile View")
    public void blogIn2019MobileView() {
        String url = "https://essenceoftesting.blogspot.com/?m=1";
        checkBlogPages(eyes, url);
    }

    @Test(description = "Blog profile")
    public void seeProfile() {
        checkProfilePage(eyes);
    }

    @Test(description = "Blogs in 2019")
    public void blogIn2019_firefox() {
        String url = "https://essenceoftesting.blogspot.com";
        eyes.setForceFullPageScreenshot(false);
        checkBlogPages(eyes, url);
    }

    @Test(description = "Blogs in 2019 - Mobile View")
    public void blogIn2019MobileView_firefox() {
        String url = "https://essenceoftesting.blogspot.com/?m=1";
        checkBlogPages(eyes, url);
    }

    @Test(description = "Blog profile")
    public void seeProfile_firefox() {
        checkProfilePage(eyes);
    }

    private Eyes configureEyes(EyesRunner runner) {
        Eyes eyes = new Eyes(runner);
        eyes.setBatch(batch);
        eyes.setMatchLevel(MatchLevel.STRICT);
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
//        eyes.setLogHandler(new StdoutLogHandler(false));
        eyes.setForceFullPageScreenshot(true);
        eyes.setSendDom(true);
        return eyes;
    }
}
