package com.anandbagmar.ultrafastgrid;

import Utilities.DriverUtils;
import com.applitools.eyes.*;
import com.applitools.eyes.selenium.BrowserType;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.visualgrid.model.DeviceName;
import com.applitools.eyes.visualgrid.model.ScreenOrientation;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;

public class BlogUltraFastGridTest extends BaseTest {
    private Eyes eyes;
    private EyesRunner runner;
    final int concurrency = 20;
    private String siteName = "blog-grid";
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
        batch = new BatchInfo("blog - Ultrafast Grid");
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        WebDriver innerDriver = null;
        String browser = (null == System.getenv("browser")) ? "chrome" : System.getenv("browser");
        System.out.println("Running test with browser - " + browser);
        numOfTests++;
        switch (browser) {
            case "chrome":
                DriverUtils.getPathForChromeDriverFromMachine();
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                innerDriver = new ChromeDriver(options);
                break;
            case "firefox":
                DriverUtils.getPathForFirefoxDriverFromMachine();
                innerDriver = new FirefoxDriver();
                break;
            default:
                innerDriver = new ChromeDriver();
        }

        RectangleSize viewportSize = new RectangleSize(1024, 768);
        driver = null;

        runner = new VisualGridRunner(concurrency);
        eyes = configureEyes(runner);

        driver = eyes.open(innerDriver, siteName, method.getName(), viewportSize);
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
        System.out.println(">>> " + BlogUltraFastGridTest.class.getSimpleName() + " - " + numOfTests + " Tests took '" + seconds + "' seconds to run for '" + numOfBrowsers + "' configurations <<<");
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

    private Eyes configureEyes(EyesRunner runner) {
        Eyes eyes = new Eyes(runner);
        eyes.setBatch(batch);
        eyes.setMatchLevel(MatchLevel.STRICT);
        eyes.setStitchMode(StitchMode.CSS);
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        eyes.setLogHandler(new StdoutLogHandler(false));
        eyes.setForceFullPageScreenshot(false);
        eyes.setSendDom(true);
        Configuration config = getVGConfiguration(eyes);
        eyes.setConfiguration(config);
        return eyes;
    }

    private Configuration getVGConfiguration(Eyes eyes) {
        Configuration config = eyes.getConfiguration();
        config.addBrowser(900, 600, BrowserType.IE_11);
        config.addBrowser(900, 600, BrowserType.IE_10);
        config.addBrowser(900, 600, BrowserType.EDGE);
        config.addBrowser(900, 600, BrowserType.CHROME);
        config.addBrowser(900, 600, BrowserType.FIREFOX);

        config.addBrowser(1024, 1024, BrowserType.IE_11);
        config.addBrowser(1024, 1024, BrowserType.IE_10);
        config.addBrowser(1024, 1024, BrowserType.EDGE);
        config.addBrowser(1024, 1024, BrowserType.CHROME);
        config.addBrowser(1024, 1024, BrowserType.FIREFOX);

        config.addBrowser(1280, 1024, BrowserType.IE_11);
        config.addBrowser(1280, 1024, BrowserType.IE_10);
        config.addBrowser(1280, 1024, BrowserType.EDGE);
        config.addBrowser(1280, 1024, BrowserType.CHROME);
        config.addBrowser(1280, 1024, BrowserType.FIREFOX);

        config.addDeviceEmulation(DeviceName.iPhone_4, ScreenOrientation.PORTRAIT);
        config.addDeviceEmulation(DeviceName.Galaxy_S5, ScreenOrientation.PORTRAIT);
        config.addDeviceEmulation(DeviceName.iPad, ScreenOrientation.PORTRAIT);
        config.addDeviceEmulation(DeviceName.iPad_Mini, ScreenOrientation.PORTRAIT);
        config.addDeviceEmulation(DeviceName.iPad_Pro, ScreenOrientation.PORTRAIT);
        config.addDeviceEmulation(DeviceName.Galaxy_Note_3, ScreenOrientation.PORTRAIT);
        config.addDeviceEmulation(DeviceName.iPhone_X, ScreenOrientation.PORTRAIT);

        config.addDeviceEmulation(DeviceName.iPhone_4, ScreenOrientation.LANDSCAPE);
        config.addDeviceEmulation(DeviceName.Galaxy_S5, ScreenOrientation.LANDSCAPE);
        config.addDeviceEmulation(DeviceName.iPad, ScreenOrientation.LANDSCAPE);
        config.addDeviceEmulation(DeviceName.iPad_Mini, ScreenOrientation.LANDSCAPE);
        config.addDeviceEmulation(DeviceName.iPad_Pro, ScreenOrientation.LANDSCAPE);
        config.addDeviceEmulation(DeviceName.Galaxy_Note_3, ScreenOrientation.LANDSCAPE);
        config.addDeviceEmulation(DeviceName.iPhone_X, ScreenOrientation.LANDSCAPE);

        numOfBrowsers = config.getBrowsersInfo().size();
        return config;
    }
}
