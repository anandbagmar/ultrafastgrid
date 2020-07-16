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

    public void setupBeforeMethod(String siteName, Method method, RectangleSize viewportSize, boolean useUFG, BatchInfo batch) {
        WebDriver innerDriver = createDriver(method);

        EyesRunner runner = useUFG ? new VisualGridRunner(concurrency) : new ClassicRunner();
        Eyes eyes = configureEyes(runner, batch);

        addContext(Thread.currentThread().getId(), new TestExecutionContext(method.getName(), innerDriver, eyes, runner));

        eyes.open(innerDriver, siteName, method.getName(), viewportSize);
    }

    private WebDriver createDriver(Method method) {
        System.out.println("BaseTest: BeforeMethod: Running test: " + method.getName());
        System.out.println("BaseTest: ThreadID: " + Thread.currentThread().getId());
        bt_beforeMethod = LocalDateTime.now();
        WebDriver innerDriver = null;
        String browser = (null == System.getenv("browser")) ? "chrome" : System.getenv("browser");
        System.out.println("BeforeMethod: Running test with browser - " + browser);
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

    @AfterMethod
    public void afterMethod(ITestResult result) {
        TestExecutionContext testExecutionContext = getContext(Thread.currentThread().getId());
        Eyes eyes = testExecutionContext.getEyes();
        EyesRunner runner = testExecutionContext.getEyesRunner();

        quitDriver();
        eyes.closeAsync();
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

    private void addContext(long threadId, TestExecutionContext testExecutionContext) {
        if (null == sessionContext) {
            System.out.println("SessionContext is null. Initializing");
            sessionContext = new HashMap<Long, TestExecutionContext>();
        } else {
            System.out.println("SessionContext already initialized");
        }

        System.out.println("Adding context for threadId: " + threadId);
        this.sessionContext.put(threadId, testExecutionContext);
    }

    protected TestExecutionContext getContext(long threadId) {
        return this.sessionContext.get(threadId);
    }

    protected WebDriver getDriver() {
        TestExecutionContext testExecutionContext = getContext(Thread.currentThread().getId());
        return testExecutionContext.getInnerDriver();
    }

    protected Eyes getEyes() {
        TestExecutionContext testExecutionContext = getContext(Thread.currentThread().getId());
        return testExecutionContext.getEyes();
    }

//    protected EyesRunner getEyesRunner() {
//        TestExecutionContext testExecutionContext = getContext(Thread.currentThread().getId());
//        return testExecutionContext.getEyesRunner();
//    }

    private Eyes configureEyes(EyesRunner runner, BatchInfo batch) {
        Eyes eyes = new Eyes(runner);
        eyes.setBatch(batch);
        eyes.setMatchLevel(MatchLevel.STRICT);
        eyes.setStitchMode(StitchMode.CSS);
        String branchName = System.getenv("BRANCH_NAME");
        branchName = ((null != branchName) && (!branchName.trim().isEmpty())) ? branchName.toLowerCase() : "main";
        eyes.setBranchName(branchName);
        String applitoolsApiKey = System.getenv("APPLITOOLS_API_KEY");
        System.out.println("API key: " + applitoolsApiKey);
        eyes.setApiKey(applitoolsApiKey);
//        eyes.setLogHandler(new StdoutLogHandler(false));
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

        System.out.println("Running tests on Ultrafast Grid with '" + config.getBrowsersInfo().size() + "' browsers configurations");
        return config;
    }

}
