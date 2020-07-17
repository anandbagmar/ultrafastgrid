package com.anandbagmar.ultrafastgrid;

import Utilities.DriverUtils;
import Utilities.TestExecutionContext;
import com.applitools.eyes.*;
import com.applitools.eyes.selenium.*;
import com.applitools.eyes.visualgrid.model.DeviceName;
import com.applitools.eyes.visualgrid.model.ScreenOrientation;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseTest {
    private Map<Long, TestExecutionContext> sessionContext;
    private LocalDateTime bt_beforeMethod;
    private LocalDateTime bt_afterMethod;
    private final int concurrency = 20;

    protected void setupBeforeMethod(Method method) {
        WebDriver innerDriver = createDriver(method);
        addContext(Thread.currentThread().getId(), new TestExecutionContext(method.getName(), innerDriver));
    }

    public void setupBeforeMethod(String appName, Method method, RectangleSize viewportSize, boolean useUFG, BatchInfo batch) {
        System.out.println("Running test: '" + method.getName() + "', Batch name: '" + batch.getName() + "', BatchID: " + batch.getId());
        WebDriver innerDriver = createDriver(method);

        EyesRunner runner = useUFG ? new VisualGridRunner(concurrency) : new ClassicRunner();
        Eyes eyes = configureEyes(runner, batch);

        addContext(Thread.currentThread().getId(), new TestExecutionContext(method.getName(), innerDriver, eyes, runner));

        eyes.open(innerDriver, appName, method.getName(), viewportSize);
        System.out.println("BeforeMethod: Test name: " + eyes.getConfiguration().getTestName() + ", App Name: " + eyes.getConfiguration().getAppName() + ", Batch name: " + eyes.getConfiguration().getBatch().getName() + ", BatchID: " + eyes.getConfiguration().getBatch().getId());
        System.out.println("BeforeMethod: Eyes Hashcode: " + eyes.hashCode() + ", EyesRunner Hashcode: " + runner.hashCode());
    }

    private WebDriver createDriver(Method method) {
        System.out.println("BaseTest: createDriver for test: '" + method.getName() + "' with ThreadID: " + Thread.currentThread().getId());
        bt_beforeMethod = LocalDateTime.now();
        WebDriver innerDriver = null;
        String browser = (null == System.getenv("browser")) ? "chrome" : System.getenv("browser");
        System.out.println("Running test with browser - " + browser);
        switch (browser) {
            case "chrome":
                DriverUtils.getPathForChromeDriverFromMachine();
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                options.addArguments("headless");
                innerDriver = new ChromeDriver(options);
                break;
            case "firefox":
                DriverUtils.getPathForFirefoxDriverFromMachine();
                innerDriver = new FirefoxDriver();
                break;
            default:
                innerDriver = new ChromeDriver();
        }
        return innerDriver;
    }

    @AfterMethod (alwaysRun = true)
    public void afterMethod(ITestResult result) {
        TestExecutionContext testExecutionContext = getContext(Thread.currentThread().getId());
        Eyes eyes = testExecutionContext.getEyes();
        EyesRunner runner = testExecutionContext.getEyesRunner();
        System.out.println("AfterMethod: Running test: '" + result.getMethod().getMethodName() + "', Batch name: '" + eyes.getBatch().getName() + "', BatchID: " + eyes.getBatch().getId());
        System.out.println("AfterMethod: Test name: " + eyes.getConfiguration().getTestName() + ", App Name: " + eyes.getConfiguration().getAppName() + ", Batch name: " + eyes.getConfiguration().getBatch().getName() + ", BatchID: " + eyes.getConfiguration().getBatch().getId());
        System.out.println("AfterMethod: Eyes Hashcode: " + eyes.hashCode() + ", EyesRunner Hashcode: " + runner.hashCode());

        quitDriver();
        eyes.closeAsync();
        eyes.getBatch().setCompleted(false);
        eyes.getConfiguration().getBatch().setCompleted(false);
        eyes.getConfiguration().getBatch().setNotifyOnCompletion(false);
        TestResultsSummary allTestResults = runner.getAllTestResults(false);
        TestResultContainer[] results = allTestResults.getAllResults();
        System.out.println("Number of results for test - " + result.getMethod().getMethodName() + ": " + results.length);
        boolean mismatchFound = false;
        for (TestResultContainer eachResult : results) {
            Throwable ex = results[0].getException();
            TestResults testResult = eachResult.getTestResults();
            mismatchFound = mismatchFound || handleTestResults(ex, testResult);
        }
        System.out.println("Overall mismatchFound: " + mismatchFound);

        bt_afterMethod = LocalDateTime.now();
        long seconds = Duration.between(bt_beforeMethod, bt_afterMethod).toMillis() / 1000;
        System.out.println(">>> " + BaseTest.class.getSimpleName() + " - Tests: '" + result.getTestName() + "' took '" + seconds + "' seconds to run");
        removeContext(Thread.currentThread().getId());
//        Assert.assertFalse(mismatchFound, "Visual differences found in tests");
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
        System.out.println("\t\t" + result);
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
        boolean hasMismatches = result.getMismatches() != 0;
        System.out.println("result: has mismatches: " + hasMismatches);
        return hasMismatches;
    }

    private void removeContext(long threadId) {
        if (null != sessionContext) {
            System.out.println("SessionContext is initialized");
            dumpSessionContext();

            TestExecutionContext testExecutionContext = sessionContext.remove(threadId);
            if (null == testExecutionContext) {
                System.out.println("ERROR: TestExecutionContext was already removed. This is crazy!");
            } else {
                System.out.println("Removed TestExecutionContext for test: " + testExecutionContext.getTestName());
            }

            dumpSessionContext();
        }
    }

    private void addContext(long threadId, TestExecutionContext testExecutionContext) {
        if (null == sessionContext) {
            System.out.println("SessionContext is null. Initializing");
            sessionContext = new HashMap<Long, TestExecutionContext>();
        } else {
            System.out.println("SessionContext already initialized");
        }

        dumpSessionContext();

        System.out.println("Adding context for threadId: " + threadId);
        this.sessionContext.put(threadId, testExecutionContext);

        dumpSessionContext();
    }

    private void dumpSessionContext() {
        System.out.println("SessionContext dump");
        for (Long aLong : this.sessionContext.keySet()) {
            System.out.println("ThreadID: " + aLong + ", TestExecutionContext hashcode: " + this.sessionContext.get(aLong).hashCode());
        }
    }

    protected TestExecutionContext getContext(long threadId) {
        return this.sessionContext.get(threadId);
    }

    protected WebDriver getDriver() {
        TestExecutionContext testExecutionContext = getContext(Thread.currentThread().getId());
        System.out.println("Returning Driver for TestName: " + testExecutionContext.getTestName());
        return testExecutionContext.getInnerDriver();
    }

    protected Eyes getEyes() {
        TestExecutionContext testExecutionContext = getContext(Thread.currentThread().getId());
        System.out.println("Returning Eyes for TestName: " + testExecutionContext.getTestName());
        return testExecutionContext.getEyes();
    }

//    protected EyesRunner getEyesRunner() {
//        TestExecutionContext testExecutionContext = getContext(Thread.currentThread().getId());
//        return testExecutionContext.getEyesRunner();
//    }

    private Eyes configureEyes(EyesRunner runner, BatchInfo batch) {
        Eyes eyes = new Eyes(runner);
        Configuration config = eyes.getConfiguration();
        config.setBatch(batch);
        config.setMatchLevel(MatchLevel.STRICT);
        config.setStitchMode(StitchMode.CSS);
        String branchName = System.getenv("BRANCH_NAME");
        branchName = ((null != branchName) && (!branchName.trim().isEmpty())) ? branchName.toLowerCase() : "main";
        System.out.println("Branch name: " + branchName);
        config.setBranchName(branchName);
        String applitoolsApiKey = System.getenv("APPLITOOLS_API_KEY");
        System.out.println("API key: " + applitoolsApiKey);
        config.setApiKey(applitoolsApiKey);
        eyes.setLogHandler(new StdoutLogHandler(true));
        config.setForceFullPageScreenshot(false);
        config.setSendDom(true);
        config = getVGConfiguration(config);
        eyes.setConfiguration(config);
        return eyes;
    }

    private Configuration getVGConfiguration(Configuration config) {

//        config.addBrowser(900, 600, BrowserType.IE_11);
//        config.addBrowser(900, 600, BrowserType.IE_10);
        config.addBrowser(900, 600, BrowserType.EDGE_CHROMIUM);
        config.addBrowser(900, 600, BrowserType.CHROME);
        config.addBrowser(900, 600, BrowserType.FIREFOX);

//        config.addBrowser(1024, 1024, BrowserType.IE_11);
//        config.addBrowser(1024, 1024, BrowserType.IE_10);
//        config.addBrowser(1024, 1024, BrowserType.EDGE_CHROMIUM);
//        config.addBrowser(1024, 1024, BrowserType.EDGE_CHROMIUM_ONE_VERSION_BACK);
//        config.addBrowser(1024, 1024, BrowserType.EDGE_LEGACY);
//        config.addBrowser(1024, 1024, BrowserType.SAFARI);
//        config.addBrowser(1024, 1024, BrowserType.SAFARI_ONE_VERSION_BACK);
//        config.addBrowser(1024, 1024, BrowserType.SAFARI_TWO_VERSIONS_BACK);
//        config.addBrowser(1024, 1024, BrowserType.CHROME);
//        config.addBrowser(1024, 1024, BrowserType.CHROME_ONE_VERSION_BACK);
//        config.addBrowser(1024, 1024, BrowserType.CHROME_TWO_VERSIONS_BACK);
//        config.addBrowser(1024, 1024, BrowserType.FIREFOX);
//        config.addBrowser(1024, 1024, BrowserType.FIREFOX_ONE_VERSION_BACK);
//        config.addBrowser(1024, 1024, BrowserType.FIREFOX_TWO_VERSIONS_BACK);
//
//        config.addDeviceEmulation(DeviceName.iPhone_4, ScreenOrientation.PORTRAIT);
//        config.addDeviceEmulation(DeviceName.Galaxy_S5, ScreenOrientation.PORTRAIT);
//        config.addDeviceEmulation(DeviceName.iPad, ScreenOrientation.PORTRAIT);
//        config.addDeviceEmulation(DeviceName.iPad_Mini, ScreenOrientation.PORTRAIT);
//        config.addDeviceEmulation(DeviceName.iPad_Pro, ScreenOrientation.PORTRAIT);
//        config.addDeviceEmulation(DeviceName.Galaxy_Note_3, ScreenOrientation.PORTRAIT);
//        config.addDeviceEmulation(DeviceName.iPhone_X, ScreenOrientation.PORTRAIT);
//
//        config.addDeviceEmulation(DeviceName.iPhone_4, ScreenOrientation.LANDSCAPE);
//        config.addDeviceEmulation(DeviceName.Galaxy_S5, ScreenOrientation.LANDSCAPE);
//        config.addDeviceEmulation(DeviceName.iPad, ScreenOrientation.LANDSCAPE);
//        config.addDeviceEmulation(DeviceName.iPad_Mini, ScreenOrientation.LANDSCAPE);
//        config.addDeviceEmulation(DeviceName.iPad_Pro, ScreenOrientation.LANDSCAPE);
        config.addDeviceEmulation(DeviceName.Galaxy_Note_3, ScreenOrientation.LANDSCAPE);
        config.addDeviceEmulation(DeviceName.iPhone_X, ScreenOrientation.LANDSCAPE);

        System.out.println("Running tests on Ultrafast Grid with '" + config.getBrowsersInfo().size() + "' browsers configurations");
        return config;
    }

}
