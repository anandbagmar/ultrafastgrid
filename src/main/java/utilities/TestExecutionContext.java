package utilities;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.EyesRunner;
import com.applitools.eyes.selenium.Eyes;
import org.openqa.selenium.WebDriver;

public class TestExecutionContext {
    private WebDriver innerDriver;
    private String testName;
    private Eyes eyes;
    private EyesRunner eyesRunner;
    private BatchInfo batchInfo;

    public TestExecutionContext(String testName, WebDriver innerDriver) {
        instantiateTestExecutionContext(testName, innerDriver, null, null, null);
    }

    public TestExecutionContext(String testName, WebDriver innerDriver, Eyes eyes, EyesRunner runner, BatchInfo batchInfo) {
        instantiateTestExecutionContext(testName, innerDriver, eyes, runner, batchInfo);
    }

    private void instantiateTestExecutionContext(String testName, WebDriver innerDriver, Eyes eyes, EyesRunner runner, BatchInfo batchInfo) {
        this.testName = testName;
        this.innerDriver = innerDriver;
        this.eyes = eyes;
        this.eyesRunner = runner;
        this.batchInfo = batchInfo;
    }

    public WebDriver getInnerDriver() {
        return innerDriver;
    }

    public Eyes getEyes() {
        return eyes;
    }

    public EyesRunner getEyesRunner() {
        return eyesRunner;
    }

    public String getTestName() {
        return this.testName;
    }

    public BatchInfo getBatchInfo() {
        return this.batchInfo;
    }

    //    public void dump() {
//        System.out.println("TestExecutionContext: dump()");
//        System.out.println("TestName        : " + this.testName);
//        System.out.println("InnerDriver hash: " + this.innerDriver.hashCode());
//        System.out.println("Eyes.hash       : " + this.eyes.hashCode());
//        System.out.println("EyesRunner.hash : " + this.eyesRunner.hashCode());
//    }
}
