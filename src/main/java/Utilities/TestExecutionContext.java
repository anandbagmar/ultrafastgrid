package Utilities;

import com.applitools.eyes.EyesRunner;
import com.applitools.eyes.selenium.Eyes;
import org.openqa.selenium.WebDriver;

public class TestExecutionContext {
    private WebDriver innerDriver;
    private String testName;
    private Eyes eyes;
    private EyesRunner eyesRunner;

    public TestExecutionContext(String testName, WebDriver innerDriver) {
        instantiateTestExecutionContext(testName, innerDriver, null, null);
    }

    public TestExecutionContext(String testName, WebDriver innerDriver, Eyes eyes, EyesRunner runner) {
        instantiateTestExecutionContext(testName, innerDriver, eyes, runner);
    }

    private void instantiateTestExecutionContext(String testName, WebDriver innerDriver, Eyes eyes, EyesRunner runner) {
        this.testName = testName;
        this.innerDriver = innerDriver;
        this.eyes = eyes;
        this.eyesRunner = runner;
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

    public void addEyes(Eyes eyes) {
        this.eyes = eyes;
    }

    public void addEyesRunner(EyesRunner runner) {
        this.eyesRunner = runner;
    }

//    public void dump() {
//        System.out.println("TestExecutionContext: dump()");
//        System.out.println("TestName        : " + this.testName);
//        System.out.println("InnerDriver hash: " + this.innerDriver.hashCode());
//        System.out.println("Eyes.hash       : " + this.eyes.hashCode());
//        System.out.println("EyesRunner.hash : " + this.eyesRunner.hashCode());
//    }
}
