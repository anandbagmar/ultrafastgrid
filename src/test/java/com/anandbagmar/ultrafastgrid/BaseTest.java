package com.anandbagmar.ultrafastgrid;

import com.anandbagmar.ultrafastgrid.utilities.TestExecutionContext;
import com.applitools.eyes.*;
import com.applitools.eyes.selenium.*;
import com.applitools.eyes.visualgrid.model.DeviceName;
import com.applitools.eyes.visualgrid.model.ScreenOrientation;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public abstract class BaseTest {
    private final int concurrency = 20;
    private Map<String, BatchInfo> batchContext;
    private Map<Long, TestExecutionContext> sessionContext;
    private LocalDateTime bt_beforeMethod;
    private LocalDateTime bt_afterMethod;
    private EyesRunner runner;

    protected static RectangleSize getViewportSize() {
        return new RectangleSize(1024, 960);
    }

    protected static String getBrowserName() {
        return (null== System.getenv("BROWSER")) ? "chrome" : System.getenv("BROWSER");
    }

    protected static boolean isDisabled() {
        return (null == System.getenv("DISABLED"))? false : Boolean.parseBoolean(System.getenv("DISABLED"));
    }
    protected static boolean isInject() {
        return null == System.getenv("INJECT")? false : Boolean.parseBoolean(System.getenv("INJECT"));
    }

    private static boolean isUfg() {
        return (null == System.getenv("USE_UFG")? false : Boolean.parseBoolean(System.getenv("USE_UFG")));
    }
    @BeforeSuite
    protected void beforeSuite() {
        System.out.println("--------------------------------------------------------------------");
        String applitoolsDontCloseBatches = System.getenv("APPLITOOLS_DONT_CLOSE_BATCHES");
        if (null == applitoolsDontCloseBatches) {
            throw new IllegalArgumentException("Env variable 'APPLITOOLS_DONT_CLOSE_BATCHES' should be set to true before running these tests for batches to work correctly");
        }
        System.out.println("APPLITOOLS_DONT_CLOSE_BATCHES: env : " + applitoolsDontCloseBatches);

        boolean useUFG = isUfg();
        System.out.println("useUFG: " + useUFG);
        runner = useUFG ? new VisualGridRunner(concurrency) : new ClassicRunner();
        System.out.println("--------------------------------------------------------------------");
    }

    protected synchronized void setupBeforeMethod(Method method) {
        WebDriver innerDriver = createDriver(method, "chrome");
        addContext(Thread.currentThread().getId(), new TestExecutionContext(method.getName(), innerDriver));
    }

    protected synchronized void setupBeforeMethod(String appName, Method method, boolean takeFullPageScreenshot) {
        String className = method.getDeclaringClass().getSimpleName();
        String testName = method.getName();
        appName = getUpdatedAppName(appName);

        BatchInfo batchInfo = getBatchInfoForTestClass(className);
        if (null == batchInfo) {
            System.out.println(className + ": BeforeMethod: " + testName + ": BatchInfo not yet created. Creating it now");
            batchInfo = new BatchInfo(appName);
            batchInfo.setNotifyOnCompletion(false);
            String batchID = String.valueOf(randomWithRange());
            if (null != System.getenv("JENKINS_HOME")) {
                batchID = System.getenv("APPLITOOLS_BATCH_ID");
            }
            System.out.println(className + ": BeforeMethod: " + testName + ": Setting BatchID to: '" + batchID + "'");
            batchInfo.setId(batchID);
            addBatchInfoForTestClass(className, batchInfo);
            System.out.println(className + ": BeforeMethod: " + testName + ": App name: '" + appName + "', Batch name: '" + batchInfo.getName() + "', BatchID: '" + batchInfo.getId() + "'");
        } else {
            System.out.println(className + ": BeforeMethod: " + testName + ": BatchInfo already created. Reuse it. Batch name: '" + batchInfo.getName() + "', BatchID: '" + batchInfo.getId() + "'");
        }

        WebDriver innerDriver = createDriver(method, getBrowserName());

        Eyes eyes = configureEyes(runner, batchInfo, takeFullPageScreenshot);
        addContext(Thread.currentThread().getId(), new TestExecutionContext(method.getName(), innerDriver, eyes, runner, batchInfo));
        eyes.open(innerDriver, appName, method.getName(), getViewportSize());
        System.out.println("BeforeMethod: Test name: " + eyes.getConfiguration().getTestName() + ", App Name: " + eyes.getConfiguration().getAppName() + ", Batch name: '" + eyes.getConfiguration().getBatch().getName() + "', BatchID: '" + eyes.getConfiguration().getBatch().getId() + "'");
    }

    private String getUpdatedAppName(String appName) {
        if (isUfg()) {
            appName = appName + "-UFG";
        }
        return appName;
    }

    private long randomWithRange() {
        Random random = new Random();
        return new Date().getTime() - random.nextInt();
    }

    private synchronized WebDriver createDriver(Method method, String browser) {
        System.out.println("BaseTest: createDriver for test: '" + method.getName() + "' with ThreadID: " + Thread.currentThread().getId());
        bt_beforeMethod = LocalDateTime.now();
        WebDriver innerDriver = null;
        System.out.println("Running test with browser - " + browser);
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                options.addArguments("--remote-allow-origins=*");
//                options.addArguments("headless");
                innerDriver = new ChromeDriver(options);
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                innerDriver = new FirefoxDriver();
                break;
            case "self_healing":
                innerDriver = createExecutionCloudRemoteDriver();
                break;
            default:
                innerDriver = new ChromeDriver();
        }
        return innerDriver;
    }

    private static WebDriver createExecutionCloudRemoteDriver() {
        WebDriver innerDriver;
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setBrowserName("chrome");
        try {
            innerDriver = new RemoteWebDriver(new URL(Eyes.getExecutionCloudURL()), caps);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return innerDriver;
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {
        TestExecutionContext testExecutionContext = getContext(Thread.currentThread().getId());
        Eyes eyes = testExecutionContext.getEyes();
        System.out.println("AfterMethod: Test name: " + eyes.getConfiguration().getTestName() + ", App Name: " + eyes.getConfiguration().getAppName() + ", Batch name: '" + eyes.getConfiguration().getBatch().getName() + "', BatchID: '" + eyes.getConfiguration().getBatch().getId() + "'");

        eyes.closeAsync();
        quitDriver();

        bt_afterMethod = LocalDateTime.now();
        long seconds = Duration.between(bt_beforeMethod, bt_afterMethod).toMillis() / 1000;
        System.out.println(">>> " + BaseTest.class.getSimpleName() + " - Tests: '" + result.getTestName() + "' took '" + seconds + "' seconds to run");
        removeContext(Thread.currentThread().getId());
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        TestResultsSummary allTestResults = runner.getAllTestResults(false);
        TestResultContainer[] results = allTestResults.getAllResults();
        System.out.println("Number of tests: " + results.length);
        boolean mismatchFound = false;
        for (TestResultContainer eachResult : results) {
            Throwable ex = results[0].getException();
            TestResults testResult = eachResult.getTestResults();
            mismatchFound = handleTestResults(ex, testResult) || mismatchFound;
        }
        System.out.println("Overall Visual Validaiton failed? - " + mismatchFound);
    }

    protected void waitFor(int numSeconds) {
        try {
            Thread.sleep(numSeconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void quitDriver() {
        TestExecutionContext testExecutionContext = getContext(Thread.currentThread().getId());
        WebDriver driver = testExecutionContext.getInnerDriver();
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

    protected boolean handleTestResults(Throwable ex, TestResults result) {
        if (!result.getStatus().equals(TestResultsStatus.Disabled)) {
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
        }
        boolean hasMismatches = result.getMismatches() != 0 || result.isAborted();
        System.out.println("Visual validation failed? - " + hasMismatches);
        return hasMismatches;
    }

    private synchronized void removeContext(long threadId) {
        if (null != sessionContext) {
            System.out.println("SessionContext is initialized");
            TestExecutionContext testExecutionContext = sessionContext.remove(threadId);
            if (null == testExecutionContext) {
                System.out.println("ERROR: TestExecutionContext was already removed. This is crazy!");
            } else {
                System.out.println("Removed TestExecutionContext for test: " + testExecutionContext.getTestName());
            }
        }
    }

    private synchronized void addContext(long threadId, TestExecutionContext testExecutionContext) {
        if (null == sessionContext) {
            System.out.println("SessionContext is null. Initializing");
            sessionContext = new HashMap<Long, TestExecutionContext>();
        } else {
            System.out.println("SessionContext already initialized");
        }

        System.out.println("Adding context for threadId: " + threadId);
        this.sessionContext.put(threadId, testExecutionContext);
    }

    protected synchronized TestExecutionContext getContext(long threadId) {
        return this.sessionContext.get(threadId);
    }

    protected synchronized BatchInfo getBatchInfoForTestClass(String className) {
        if (null == batchContext) {
//            System.out.println("getBatchInfoForTestClass: BatchContext is null. Initializing");
            batchContext = new HashMap<String, BatchInfo>();
        } else {
//            System.out.println("getBatchInfoForTestClass: BatchContext already initialized.");
        }
        BatchInfo batchInfo = this.batchContext.get(className);
        if (null != batchInfo) {
            System.out.println("getBatchInfoForTestClass: ClassName: '" + className + "', BatchInfo: '" + batchInfo + "', Batch name: '" + batchInfo.getName() + "', BatchID: '" + batchInfo.getId() + "', Batch Hashcode: " + batchInfo.hashCode() + ", Number of BatchInfo in BatchContext: " + batchContext.size());
        } else {
            System.out.println("getBatchInfoForTestClass: ClassName: '" + className + "', BatchInfo: '" + batchInfo + ", Number of BatchInfo in BatchContext: " + batchContext.size());
        }
        return batchInfo;
    }

    protected synchronized void addBatchInfoForTestClass(String className, BatchInfo batchInfo) {
        if (null == batchContext) {
//            System.out.println("addBatchInfoForTestClass: ClassName: '" + className + "':, BatchContext is null. Initializing");
            batchContext = new HashMap<>();
        } else {
//            System.out.println("addBatchInfoForTestClass: ClassName: '" + className + "':, BatchContext already initialized.");
        }
        batchContext.put(className, batchInfo);
//        System.out.println("addBatchInfoForTestClass: Added BatchInfo for ClassName: '" + className + "', Batch name: '" + batchInfo.getName() + "', BatchID: '" + batchInfo.getId() + "', Batch Hashcode: " + batchInfo.hashCode() + "', Number of BatchInfo in BatchContext: " + batchContext.size());
    }

    protected synchronized WebDriver getDriver() {
        TestExecutionContext testExecutionContext = getContext(Thread.currentThread().getId());
        System.out.println("Returning Driver for TestName: " + testExecutionContext.getTestName());
        return testExecutionContext.getInnerDriver();
    }

    protected synchronized Eyes getEyes() {
        TestExecutionContext testExecutionContext = getContext(Thread.currentThread().getId());
        System.out.println("Returning Eyes for TestName: " + testExecutionContext.getTestName());
        return testExecutionContext.getEyes();
    }

    private synchronized Eyes configureEyes(EyesRunner runner, BatchInfo batch, boolean takeFullPageScreenshot) {
        Eyes eyes = new Eyes(runner);
        System.out.println("Is Applitools Visual AI enabled? - " + !isDisabled());
        Configuration config = eyes.getConfiguration();
        config.setBatch(batch);
        config.setMatchLevel(MatchLevel.STRICT);
        config.setIsDisabled(isDisabled());
        config.setStitchMode(StitchMode.CSS);
        config.setForceFullPageScreenshot(takeFullPageScreenshot);
        config.getBatch().setNotifyOnCompletion(false);
        String branchName = System.getenv("BRANCH_NAME");
        branchName = ((null != branchName) && (!branchName.trim().isEmpty())) ? branchName.toLowerCase() : "main";
        System.out.println("Branch name: " + branchName);
        config.setBranchName(branchName);
        String applitoolsApiKey = System.getenv("APPLITOOLS_API_KEY");
        System.out.println("API key: " + applitoolsApiKey);
        config.setApiKey(applitoolsApiKey);
        eyes.setLogHandler(new StdoutLogHandler(true));
        config.setSendDom(true);
        config = getUFGBrowserConfiguration(config);
        eyes.setConfiguration(config);
        return eyes;
    }

    private synchronized Configuration getUFGBrowserConfiguration(Configuration config) {

//        config.addBrowser(1024, 1024, BrowserType.IE_11);
//        config.addBrowser(1024, 1024, BrowserType.IE_10);
        config.addBrowser(1024, 1024, BrowserType.EDGE_CHROMIUM);
//        config.addBrowser(1024, 1024, BrowserType.EDGE_CHROMIUM_ONE_VERSION_BACK);
//        config.addBrowser(1024, 1024, BrowserType.EDGE_LEGACY);
        config.addBrowser(1200, 1024, BrowserType.SAFARI);
//        config.addBrowser(1024, 1024, BrowserType.SAFARI_ONE_VERSION_BACK);
//        config.addBrowser(1024, 1024, BrowserType.SAFARI_TWO_VERSIONS_BACK);
        config.addBrowser(1024, 1200, BrowserType.CHROME);
//        config.addBrowser(1024, 1024, BrowserType.CHROME_ONE_VERSION_BACK);
//        config.addBrowser(1024, 1024, BrowserType.CHROME_TWO_VERSIONS_BACK);
        config.addBrowser(1200, 1200, BrowserType.FIREFOX);
//        config.addBrowser(1024, 1024, BrowserType.FIREFOX_ONE_VERSION_BACK);
//        config.addDeviceEmulation(DeviceName.iPhone_4, ScreenOrientation.PORTRAIT);
//        config.addDeviceEmulation(DeviceName.Galaxy_S5, ScreenOrientation.PORTRAIT);
        config.addDeviceEmulation(DeviceName.Galaxy_S20, ScreenOrientation.PORTRAIT);
        config.addDeviceEmulation(DeviceName.iPad, ScreenOrientation.PORTRAIT);
//        config.addDeviceEmulation(DeviceName.iPad_Mini, ScreenOrientation.PORTRAIT);
//        config.addDeviceEmulation(DeviceName.iPad_Pro, ScreenOrientation.PORTRAIT);
//        config.addDeviceEmulation(DeviceName.Galaxy_Note_3, ScreenOrientation.PORTRAIT);
//        config.addDeviceEmulation(DeviceName.iPhone_X, ScreenOrientation.PORTRAIT);
        config.addDeviceEmulation(DeviceName.iPhone_11_Pro_Max, ScreenOrientation.PORTRAIT);

//        config.addDeviceEmulation(DeviceName.iPhone_4, ScreenOrientation.LANDSCAPE);
//        config.addDeviceEmulation(DeviceName.Galaxy_S5, ScreenOrientation.LANDSCAPE);
//        config.addDeviceEmulation(DeviceName.iPad, ScreenOrientation.LANDSCAPE);
//        config.addDeviceEmulation(DeviceName.iPad_Mini, ScreenOrientation.LANDSCAPE);
//        config.addDeviceEmulation(DeviceName.iPad_Pro, ScreenOrientation.LANDSCAPE);
//        config.addDeviceEmulation(DeviceName.Galaxy_Note_3, ScreenOrientation.LANDSCAPE);
//        config.addDeviceEmulation(DeviceName.iPhone_X, ScreenOrientation.LANDSCAPE);

        System.out.println("Running tests on Ultrafast Grid with '" + config.getBrowsersInfo().size() + "' browsers configurations");
        return config;
    }

}
