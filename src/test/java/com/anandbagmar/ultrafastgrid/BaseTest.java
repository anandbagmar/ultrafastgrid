package com.anandbagmar.ultrafastgrid;

import com.applitools.eyes.*;
import com.applitools.eyes.selenium.BrowserType;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.ITestResult;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public abstract class BaseTest {
    private static final int concurrency = 20;
    private static final String APPLITOOLS_API_KEY = System.getenv("APPLITOOLS_API_KEY");
    private static LocalDateTime bt_beforeMethod;
    private static LocalDateTime bt_afterMethod;
    private static ThreadLocal<EyesRunner> runner = new ThreadLocal<>();
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<Eyes> eyes = new ThreadLocal<>();

    private static BatchInfo batchInfo = null;
    private static String appName = "applitools-shopping";
    private static final boolean IS_UFG = (null != System.getenv("USE_UFG") && Boolean.parseBoolean(System.getenv("USE_UFG")));
    private static final boolean IS_INJECT = (null != System.getenv("INJECT") && Boolean.parseBoolean(System.getenv("INJECT")));
    public static boolean IS_EYES_ENABLED = (null != System.getenv("EYES") && Boolean.parseBoolean(System.getenv("EYES")));
    public static final String BROWSER_NAME = (null == System.getenv("BROWSER")) ? "chrome" : System.getenv("BROWSER");

    protected static RectangleSize getViewportSize() {
        return new RectangleSize(1200, 1024);
    }

    public static boolean isInject() {
        return IS_INJECT;
    }

    public static void runBeforeSuite() {
        System.out.println("------------------------ BEFORE SUITE - started  ------------------------");
        System.out.println("IS_EYES_ENABLED: " + IS_EYES_ENABLED);
        System.out.println("IS_UFG: " + IS_UFG);
        System.out.println("IS_INJECT: " + IS_INJECT);
        System.out.println("BROWSER_NAME: " + BROWSER_NAME);
        appName = getUpdatedAppName();
        System.out.println("appNameFromTest: " + appName);
        if (IS_EYES_ENABLED) {
            setupBatchInfo();
        }
        System.out.println("------------------------ BEFORE SUITE - finished ------------------------");
    }

    public static void createRunner() {
        EyesRunner eyesRunner = IS_UFG ? new VisualGridRunner(concurrency) : new ClassicRunner();
        runner.set(eyesRunner);
        runner.get().setDontCloseBatches(true);
    }

    private static void setupBatchInfo() {
        if (null == batchInfo) {
            batchInfo = new BatchInfo(appName);
            batchInfo.setNotifyOnCompletion(false);
            String batchID = String.valueOf(randomWithRange());
            batchInfo.setId(batchID);
            batchInfo.addProperty("Repo", new File(System.getProperty("user.dir")).getName());
        }
        System.out.println(null == batchInfo ? "batchInfo is null" : "batchInfo: " + batchInfo.getId());
    }

    public static synchronized Eyes createEyes(WebDriver driver, String className, Method method, boolean takeFullPageScreenshot) {
        System.out.println("------------------------ createEyes - started  ------------------------");
        if (IS_EYES_ENABLED) {
            Eyes myEyes = configureEyes(runner.get(), batchInfo, takeFullPageScreenshot);
            myEyes.open(driver, appName, className + "-" + method.getName(), getViewportSize());
            System.out.println("BeforeMethod: Test name: " + myEyes.getConfiguration().getTestName() + ", App Name: " + myEyes.getConfiguration().getAppName() + ", Batch name: '" + myEyes.getConfiguration().getBatch().getName() + "'");
            System.out.println("------------------------ createEyes - finished ------------------------");
            eyes.set(myEyes);
            return myEyes;
        } else {
            throw new RuntimeException("IS_EYES_ENABLED=false");
        }
    }

    private static String getUpdatedAppName() {
        if (IS_UFG) {
            appName = appName + "-UFG";
        }
        return appName;
    }

    private static long randomWithRange() {
        Random random = new Random();
        return new Date().getTime() - random.nextInt();
    }

    public static synchronized void createDriver(Method method, String browser) {
        System.out.println("------------------------ createDriver - started  ------------------------");
        System.out.println("BaseTest: createDriver for test: '" + method.getName() + "' with ThreadID: " + Thread.currentThread().getId());
        bt_beforeMethod = LocalDateTime.now();
        WebDriver innerDriver = null;
        System.out.println("Running test with browser - " + browser);
        switch (browser.toLowerCase()) {
            case "chrome":
                System.out.println("Creating local ChromeDriver");
                innerDriver = createChromeDriver();
                break;
            case "firefox":
                System.out.println("Creating local FirefoxDriver");
                innerDriver = new FirefoxDriver();
                break;
            case "edge":
                System.out.println("Creating local EdgeDriver");
                innerDriver = new EdgeDriver();
                break;
            case "safari":
                System.out.println("Creating local SafariDriver");
                innerDriver = new SafariDriver();
                innerDriver.manage().window().maximize();
                innerDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
                break;
            case "self_healing":
                System.out.println("Creating Driver using ExecutionCloud");
                innerDriver = createExecutionCloudRemoteDriver();
                break;
            default:
                System.out.println("Default: Creating local ChromeDriver");
                innerDriver = createChromeDriver();
        }
        System.out.println("------------------------ createDriver - finished ------------------------");
        driver.set(innerDriver);
    }

    private static WebDriver createChromeDriver() {
        WebDriver innerDriver;
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.addArguments("--remote-allow-origins=*");
//        addExtensionsToChromeOptions(options);
//        options.addArguments("headless");
        innerDriver = new ChromeDriver(options);
        return innerDriver;
    }

    private static WebDriver createExecutionCloudRemoteDriver() {
        WebDriver innerDriver;
        ChromeOptions chromeOptions = new ChromeOptions();
//        addExtensionsToChromeOptions(chromeOptions);
        DesiredCapabilities capabilities = new DesiredCapabilities(chromeOptions);
        capabilities.setCapability("applitools:apiKey", APPLITOOLS_API_KEY);
        try {
            innerDriver = new RemoteWebDriver(new URL(Eyes.getExecutionCloudURL()), capabilities);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return innerDriver;
    }

    private static void addExtensionsToChromeOptions(ChromeOptions chromeOptions) {
        // https://chrome.google.com/webstore/detail/json-formatter/bcjindcccaagfpapjjmafapmmgkkhgoa?utm_source=ext_app_menu
        File jsonFormatter = new File("./src/test/resources/JSON-Formatter.crx");
        // https://chrome.google.com/webstore/detail/modheader-modify-http-hea/idgpnmonknjnojddfkpgkljpfnnfcklj?utm_source=ext_app_menu
        File modHeaders = new File("./src/test/resources/ModHeader.crx");
        ArrayList<File> extensions = new ArrayList<>();
        extensions.add(jsonFormatter);
        extensions.add(modHeaders);
        chromeOptions.addExtensions(extensions);
    }

    public static synchronized boolean isVisualValidationPassed(ITestResult result, Eyes eyes) {
        System.out.println("------------------------ isVisualValidationPassed - started  ------------------------");
        System.out.printf("AfterMethod: Test name: %s, App Name: %s%n", result.getName(), appName);

        boolean mismatchFound = false;
        if (IS_EYES_ENABLED && eyes!=null) {
            System.out.printf("Batch name: '%s'%n", eyes.getConfiguration().getBatch().getName());
            // fail the test if there is any visual difference found
            TestResultsSummary allTestResults = runner.get().getAllTestResults(false);
            TestResultContainer[] results = allTestResults.getAllResults();
            for (TestResultContainer eachResult : results) {
                Throwable ex = results[0].getException();
                TestResults testResult = eachResult.getTestResults();
                mismatchFound = handleTestResults(ex, testResult) || mismatchFound;
            }
        }

        bt_afterMethod = LocalDateTime.now();
        long seconds = Duration.between(bt_beforeMethod, bt_afterMethod).toMillis() / 1000;
        System.out.println(">>> " + BaseTest.class.getSimpleName() + " - Tests: '" + result.getName() + "' took '" + seconds + "' seconds to run");
        System.out.println("------------------------ isVisualValidationPassed - finished ------------------------");
        return !mismatchFound;
    }

    public static void runAfterSuite() {
        System.out.println("------------------------ After SUITE - started  ------------------------");
        if (IS_EYES_ENABLED) {
            closeBatch();
        }
        System.out.println("------------------------ After SUITE - finished ------------------------");
    }

    private static void closeBatch() {
        if (null != batchInfo) {
            System.out.println("Complete the batch");
            batchInfo.setCompleted(true);
        }
    }

    public static void waitFor(int numSeconds) {
        try {
            Thread.sleep(numSeconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void quitDriver(WebDriver driver) {
        if (null != driver) {
            try {
                driver.close();
                driver.quit();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                driver = null;
            }
        }
    }

    protected static boolean handleTestResults(Throwable ex, TestResults result) {
        boolean hasMismatches = false;
        System.out.println(result);
        System.out.println("\tTest Name: " + result.getName() + " :: " + result);
        System.out.println("\tTest status: " + result.getStatus());
        System.out.printf("\t\tName = '%s', \nBrowser = %s,OS = %s, viewport = %dx%d, matched = %d, mismatched = %d, missing = %d, aborted = %s\n",
                result.getName(),
                result.getHostApp(),
                result.getHostOS(),
                result.getHostDisplaySize().getWidth(),
                result.getHostDisplaySize().getHeight(),
                result.getMatches(),
                result.getMismatches(),
                result.getMissing(),
                (result.isAborted() ? "aborted" : "no"));
        System.out.println("Results available here: " + result.getUrl());
        hasMismatches = result.getMismatches() != 0 || result.isAborted();
        System.out.println("Visual validation failed? - " + hasMismatches);
        return hasMismatches;
    }

    private static synchronized Eyes configureEyes(EyesRunner runner, BatchInfo batch, boolean takeFullPageScreenshot) {
        Eyes eyes = new Eyes(runner);
        System.out.println("Is Applitools Visual AI enabled? - " + IS_EYES_ENABLED);
        Configuration config = eyes.getConfiguration();
        config.setBatch(batch);
        config.setSaveNewTests(false);
        config.setMatchLevel(MatchLevel.STRICT);
        config.setIsDisabled(!IS_EYES_ENABLED);
        config.setStitchMode(StitchMode.CSS);
        config.setForceFullPageScreenshot(takeFullPageScreenshot);
        config.getBatch().setNotifyOnCompletion(false);
        String branchName = System.getenv("BRANCH_NAME");
        branchName = ((null != branchName) && (!branchName.trim().isEmpty())) ? branchName.toLowerCase() : "main";
        System.out.println("Branch name: " + branchName);
        config.setBranchName(branchName);
        config.setApiKey(APPLITOOLS_API_KEY);
        eyes.setLogHandler(new StdoutLogHandler(true));
        config.setSendDom(true);
        getUFGBrowserConfiguration(config);
        eyes.setConfiguration(config);
        return eyes;
    }

    private static synchronized void getUFGBrowserConfiguration(Configuration config) {
        config.addBrowser(1600, 1200, BrowserType.EDGE_CHROMIUM);
//        config.addBrowser(1200, 1024, BrowserType.SAFARI);
        config.addBrowser(1500, 1200, BrowserType.CHROME);
        config.addBrowser(1400, 1200, BrowserType.FIREFOX);
//        config.addDeviceEmulation(DeviceName.Galaxy_S20, ScreenOrientation.LANDSCAPE);
//        config.addDeviceEmulation(DeviceName.iPad, ScreenOrientation.LANDSCAPE);
//        config.addDeviceEmulation(DeviceName.iPhone_11_Pro_Max, ScreenOrientation.LANDSCAPE);
        System.out.println("Running tests on Ultrafast Grid with '" + config.getBrowsersInfo().size() + "' browsers configurations");
    }

    public static Eyes getEyes() {
        return eyes.get();
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

}
