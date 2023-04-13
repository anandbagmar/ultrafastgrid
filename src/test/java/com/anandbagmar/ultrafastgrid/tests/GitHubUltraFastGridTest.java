package com.anandbagmar.ultrafastgrid.tests;

import com.anandbagmar.ultrafastgrid.BaseTest;
import com.applitools.eyes.selenium.Eyes;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class GitHubUltraFastGridTest extends BaseTest {
    private final String appName = "github";
    String expectedH1Text = "SignIn";
    String expectedUserName = "Username";
    String expectedErrorMessage = "Incorrect credentials";

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {
        setupBeforeMethod(appName, method, true);
    }

    @Test(description = "Login to Github")
    public void loginGithub() {
        Eyes eyes = getEyes();
        WebDriver driver = getDriver();

        String url = "https://github.com/login";
        driver.get(url);
        eyes.checkWindow("onLoad");
        if (isInject()) {
//            ((JavascriptExecutor) driver).executeScript("document.querySelector(\"h1\").innerText=\"" + expectedH1Text + "\"");
            ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div.footer\").style=\"display: none;\"");
//            ((JavascriptExecutor) driver).executeScript("document.querySelector(\"label[for='login_field']\").innerText=\"" + expectedUserName + "\"");
//            ((JavascriptExecutor) driver).executeScript("document.querySelector(\"input.btn\").classList.remove(\"btn\")");
            ((JavascriptExecutor) driver).executeScript("document.querySelector(\"input.btn\").setAttribute(\"value\",\"Sign\")");
        }
        eyes.checkWindow("loginPage");
        driver.findElement(By.xpath("//input[@name=\"commit\"]")).click();
        eyes.checkWindow("loginErrors");
    }
}
