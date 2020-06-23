package com.anandbagmar.ultrafastgrid;

import Utilities.DriverUtils;
import com.applitools.eyes.*;
import com.applitools.eyes.selenium.*;
import com.applitools.eyes.visualgrid.model.DeviceName;
import com.applitools.eyes.visualgrid.model.ScreenOrientation;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;

public class GitHubUltraFastGridTest extends BaseTest {
    private Eyes eyes;
    private EyesRunner runner;
    final int concurrency = 20;
    private String siteName = "github-grid-test";
    LocalDateTime before;
    LocalDateTime after;
    LocalDateTime beforeMethod;
    LocalDateTime afterMethod;
    private int numOfBrowsers;
    private int numOfTests;
    RectangleSize viewportSize = new RectangleSize(1000, 1000);
    WebDriver innerDriver = null;

    String expectedH1Text = "SignIn";
    String expectedUserName = "Username";
    String expectedErrorMessage = "Incorrect credentials";

    @BeforeSuite
    public void setUpSuite() {
        String batchName = "mytest-" + viewportSize.toString();
        System.out.println("BeforeSuite: Batch name: '" + batchName + "'");
        batch = new BatchInfo(batchName);
    }

    @BeforeClass
    public void setUp() {
        before = LocalDateTime.now();
        System.out.println("BeforeClass: Time: '" + before.toString() + "'");
        numOfBrowsers = 0;
        numOfTests = 0;
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        beforeMethod = LocalDateTime.now();
        String browser = (null == System.getenv("browser")) ? "chrome" : System.getenv("browser");
        System.out.println("BeforeMethod: Running test with browser - " + browser);
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

        driver = null;

        runner = new VisualGridRunner(concurrency);
//        runner = new ClassicRunner();
        eyes = configureEyes(runner);

    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        System.out.println("AfterMethod");
        quitDriver();
        eyes.closeAsync();
        afterMethod = LocalDateTime.now();
        long seconds = Duration.between(beforeMethod, afterMethod).toMillis() / 1000;
        System.out.println(">>> " + GitHubUltraFastGridTest.class.getSimpleName() + " - Tests: '" + result.getTestName() + "' took '" + seconds + "' seconds to run for '" + numOfBrowsers + "' configurations <<<");
    }

    @AfterSuite
    public void tearDown() {
        System.out.println("AfterSuite: Get results from Applitools");
        TestResultsSummary allTestResults = runner.getAllTestResults(false);
        TestResultContainer[] results = allTestResults.getAllResults();
        boolean mismatchFound = true;
        for (TestResultContainer result : results) {
            Throwable ex = results[0].getException();
            TestResults testResult = result.getTestResults();
            mismatchFound = mismatchFound && handleTestResults(ex, testResult);
        }
        after = LocalDateTime.now();
        long seconds = Duration.between(before, after).toMillis() / 1000;
        System.out.println("Overall mismatchFound: " + mismatchFound);
        System.out.println(">>> " + GitHubUltraFastGridTest.class.getSimpleName() + " - '" + numOfTests + "' Tests took '" + seconds + "' seconds to run for '" + numOfBrowsers + "' configurations <<<");
        Assert.assertFalse(mismatchFound, "Visual differences found in tests");
    }

    @Test(description = "Login to Github - 1st build, no Eyes")
    public void loginGithubFirstBuildNoEyes() {
        driver = innerDriver;

        String url = "https://github.com/login";
        driver.get(url);
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"h1\").innerText=\"" + expectedH1Text  + "\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.footer\").style=\"display: none;\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"label[for='login_field']\").innerText=\"" + expectedUserName + "\"");
        driver.findElement(By.cssSelector("input.btn")).click();
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"h1\").innerText=\"" + expectedH1Text  + "\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.footer\").style=\"display: none;\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"label[for='login_field']\").innerText=\"" + expectedUserName + "\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.container-lg.px-2\").innerText=\"" + expectedErrorMessage + "\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.container-lg.px-2\").style.background=\"white\"");

        String h1Text = driver.findElement(By.cssSelector("h1")).getText();
        String usernameLabel = driver.findElement(By.cssSelector("label[for='login_field']")).getText();
        String passwordLabel = driver.findElement(By.cssSelector("label[for='password']")).getText();
        String errorMessage = driver.findElement(By.cssSelector("div.container-lg.px-2")).getText();
        System.out.println(String.format("H1 text: '%s'", h1Text));
        System.out.println(String.format("usernameLabel  : '%s'", usernameLabel));
        System.out.println(String.format("passwordLabel  : '%s'", passwordLabel));
        System.out.println(String.format("errorMessage  : '%s'", errorMessage));

        Assert.assertEquals(h1Text, expectedH1Text);
        Assert.assertEquals(usernameLabel, expectedUserName);
        Assert.assertTrue(passwordLabel.contains("Password") && passwordLabel.contains("Forgot password?"));
        Assert.assertEquals(errorMessage, expectedErrorMessage);
    }

    @Test(description = "Validate error messages on Login to Github - new build, no Eyes")
    public void loginGithubNewBuildNoEyes() {
        driver = innerDriver;
        String expectedH1Text = "SignIn";
        String expectedUserName = "Username";
        String expectedErrorMessage = "Incorrect credentials";

        String url = "https://github.com/login";
        driver.get(url);
        driver.findElement(By.cssSelector("input.btn")).click();

        String h1Text = driver.findElement(By.cssSelector("h1")).getText();
        String usernameLabel = driver.findElement(By.cssSelector("label[for='login_field']")).getText();
        String passwordLabel = driver.findElement(By.cssSelector("label[for='password']")).getText();
        String errorMessage = driver.findElement(By.cssSelector("div.container-lg.px-2")).getText();
        System.out.println(String.format("H1 text: '%s'", h1Text));
        System.out.println(String.format("usernameLabel  : '%s'", usernameLabel));
        System.out.println(String.format("passwordLabel  : '%s'", passwordLabel));
        System.out.println(String.format("errorMessage  : '%s'", errorMessage));

        Assert.assertEquals(h1Text, expectedH1Text);
        Assert.assertEquals(usernameLabel, expectedUserName);
        Assert.assertTrue(passwordLabel.contains("Password") && passwordLabel.contains("Forgot password?"));
        Assert.assertEquals(errorMessage, expectedErrorMessage);
    }

    @Test(description = "Login to Github - 1st build, with Eyes")
    public void loginGithubFirstBuildWithEyes() {
        driver = eyes.open(innerDriver, "github-" + viewportSize.toString(), "loginGithub" + "-" + viewportSize.toString(), viewportSize);

        String url = "https://github.com/login";
        driver.get(url);
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"h1\").innerText=\"" + expectedH1Text  + "\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.footer\").style=\"display: none;\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"label[for='login_field']\").innerText=\"" + expectedUserName + "\"");
        eyes.checkWindow("loginPage");
        driver.findElement(By.cssSelector("input.btn")).click();
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"h1\").innerText=\"" + expectedH1Text  + "\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.footer\").style=\"display: none;\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"label[for='login_field']\").innerText=\"" + expectedUserName + "\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.container-lg.px-2\").innerText=\"" + expectedErrorMessage + "\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.container-lg.px-2\").style.background=\"white\"");
        eyes.checkWindow("loginErrors");

        String h1Text = driver.findElement(By.cssSelector("h1")).getText();
        String usernameLabel = driver.findElement(By.cssSelector("label[for='login_field']")).getText();
        String passwordLabel = driver.findElement(By.cssSelector("label[for='password']")).getText();
        String errorMessage = driver.findElement(By.cssSelector("div.container-lg.px-2")).getText();
        System.out.println(String.format("H1 text: '%s'", h1Text));
        System.out.println(String.format("usernameLabel  : '%s'", usernameLabel));
        System.out.println(String.format("passwordLabel  : '%s'", passwordLabel));
        System.out.println(String.format("errorMessage  : '%s'", errorMessage));
    }

    @Test(description = "Validate error messages on Login to Github - new build, with Eyes")
    public void loginGithubNewBuildWithEyes() {
        driver = eyes.open(innerDriver, "github-" + viewportSize.toString(), "loginGithub" + "-" + viewportSize.toString(), viewportSize);

        String url = "https://github.com/login";
        driver.get(url);
        eyes.checkWindow("loginPage");
        driver.findElement(By.cssSelector("input.btn")).click();
        eyes.checkWindow("loginErrors");

        String h1Text = driver.findElement(By.cssSelector("h1")).getText();
        String usernameLabel = driver.findElement(By.cssSelector("label[for='login_field']")).getText();
        String passwordLabel = driver.findElement(By.cssSelector("label[for='password']")).getText();
        String errorMessage = driver.findElement(By.cssSelector("div.container-lg.px-2")).getText();
        System.out.println(String.format("H1 text: '%s'", h1Text));
        System.out.println(String.format("usernameLabel  : '%s'", usernameLabel));
        System.out.println(String.format("passwordLabel  : '%s'", passwordLabel));
        System.out.println(String.format("errorMessage  : '%s'", errorMessage));
    }

    private Eyes configureEyes(EyesRunner runner) {
        Eyes eyes = new Eyes(runner);
        eyes.setBatch(batch);
        eyes.setMatchLevel(MatchLevel.STRICT);
        eyes.setStitchMode(StitchMode.CSS);
        String applitoolsApiKey = System.getenv("APPLITOOLS_API_KEY");
        System.out.println("API key: " + applitoolsApiKey);
        eyes.setApiKey(applitoolsApiKey);
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
        config.addBrowser(900, 600, BrowserType.EDGE_CHROMIUM);
        config.addBrowser(900, 600, BrowserType.CHROME);
        config.addBrowser(900, 600, BrowserType.FIREFOX);

        config.addBrowser(1024, 1024, BrowserType.IE_11);
        config.addBrowser(1024, 1024, BrowserType.IE_10);
        config.addBrowser(1024, 1024, BrowserType.EDGE_CHROMIUM);
        config.addBrowser(1024, 1024, BrowserType.EDGE_CHROMIUM_ONE_VERSION_BACK);
        config.addBrowser(1024, 1024, BrowserType.EDGE_LEGACY);
        config.addBrowser(1024, 1024, BrowserType.SAFARI);
        config.addBrowser(1024, 1024, BrowserType.SAFARI_ONE_VERSION_BACK);
        config.addBrowser(1024, 1024, BrowserType.SAFARI_TWO_VERSIONS_BACK);
        config.addBrowser(1024, 1024, BrowserType.CHROME);
        config.addBrowser(1024, 1024, BrowserType.CHROME_ONE_VERSION_BACK);
        config.addBrowser(1024, 1024, BrowserType.CHROME_TWO_VERSIONS_BACK);
        config.addBrowser(1024, 1024, BrowserType.FIREFOX);
        config.addBrowser(1024, 1024, BrowserType.FIREFOX_ONE_VERSION_BACK);
        config.addBrowser(1024, 1024, BrowserType.FIREFOX_TWO_VERSIONS_BACK);

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
