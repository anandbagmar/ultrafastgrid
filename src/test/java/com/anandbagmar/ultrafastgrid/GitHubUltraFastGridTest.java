package com.anandbagmar.ultrafastgrid;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.selenium.Eyes;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class GitHubUltraFastGridTest extends BaseTest {
    private final String siteName = "github-eyes-ufg-test";
    RectangleSize viewportSize = new RectangleSize(1024, 768);
    private static BatchInfo batch;

    String expectedH1Text = "SignIn";
    String expectedUserName = "Username";
    String expectedErrorMessage = "Incorrect credentials";

    @BeforeClass
    public void beforeClass() {
        batch = new BatchInfo(siteName);
        System.out.println("GitHubUltraFastGridTest: BeforeClass: Batch name: '" + siteName + "'");
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        setupBeforeMethod(siteName, method, viewportSize, true, batch);
    }

    //    @Test(description = "Login to Github - 1st build, no Eyes")
    public void loginGithubFirstBuildNoEyes() {
        WebDriver driver = getDriver();

        String url = "https://github.com/login";
        driver.get(url);
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"h1\").innerText=\"" + expectedH1Text + "\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.footer\").style=\"display: none;\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"label[for='login_field']\").innerText=\"" + expectedUserName + "\"");
        driver.findElement(By.cssSelector("input.btn")).click();
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"h1\").innerText=\"" + expectedH1Text + "\"");
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

    //    @Test(description = "Validate error messages on Login to Github - new build, no Eyes")
    public void loginGithubNewBuildNoEyes() {
        WebDriver driver = getDriver();
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

    //    @Test(description = "Login to Github - 1st build, with Eyes")
    public void loginGithubFirstBuildWithEyes() {
        Eyes eyes = getEyes();
        WebDriver innerDriver = getDriver();
        WebDriver driver = eyes.open(innerDriver, "github-" + viewportSize.toString(), "loginGithub" + "-" + viewportSize.toString(), viewportSize);

        String url = "https://github.com/login";
        driver.get(url);
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"h1\").innerText=\"" + expectedH1Text + "\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.footer\").style=\"display: none;\"");
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"label[for='login_field']\").innerText=\"" + expectedUserName + "\"");
        eyes.checkWindow("loginPage");
        driver.findElement(By.cssSelector("input.btn")).click();
        ((JavascriptExecutor) driver).executeScript("document.querySelector(\"h1\").innerText=\"" + expectedH1Text + "\"");
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
        Eyes eyes = getEyes();
        WebDriver driver = getDriver();

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

}
