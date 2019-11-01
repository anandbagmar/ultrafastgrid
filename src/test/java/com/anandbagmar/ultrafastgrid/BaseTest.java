package com.anandbagmar.ultrafastgrid;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.TestResults;
import com.applitools.eyes.selenium.Eyes;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeSuite;

import java.util.HashMap;
import java.util.List;

public abstract class BaseTest {
    protected static BatchInfo batch;
    protected WebDriver driver;

    @BeforeSuite
    public void setUp() {
    }

    protected void waitFor(int numSeconds) {
        try {
            Thread.sleep(numSeconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void quitDriver() {
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

    protected HashMap<String, String> getBlogPosts() {
        List<WebElement> listOfBlogs = driver.findElements(By.xpath("//a[contains(@href,\".com/2019\")]"));
        HashMap<String, String> blogPosts = new HashMap<>();
        driver.findElements(By.xpath("//a[contains(@href,\".com/2019\")]")).forEach(ele -> {
            String title = ele.getText();
            String href = ele.getAttribute("href");
            if (href.contains(".html") && !href.contains("#") && ele.getAttribute("class").isEmpty()) {
                blogPosts.put(href, title);
            }
        });
        blogPosts.clear();
        blogPosts.put("https://essenceoftesting.blogspot.com/2019/02/talks-and-workshops-in-agile-india-2019.html", "Talks and workshops in Agile India 2019");
        blogPosts.put("https://essenceoftesting.blogspot.com/2019/10/overcoming-chromedriver-version.html", "Chrome driver version");
        return blogPosts;
    }

    protected void checkBlogPages(String url) { checkBlogPages(null, url); }

    protected void checkBlogPages(Eyes eyes, String url) {
        driver.get(url);
        if (null != eyes) {
            eyes.checkWindow("Blog home");
            eyes.setForceFullPageScreenshot(true);
        }
        HashMap<String, String> blogPosts = getBlogPosts();
        blogPosts.keySet().forEach(key -> {
            System.out.println("title - " + blogPosts.get(key));
            System.out.println("url - " + key);
            driver.get(key);
            if (null != eyes) {
                eyes.checkWindow("Blog title - " + blogPosts.get(key));
            }
        });
    }

    protected void checkProfilePage(Eyes eyes) {
        String url = "https://essenceoftesting.blogspot.com";
        driver.get(url);
        if (null != eyes) {
            eyes.setForceFullPageScreenshot(false);
            eyes.checkWindow("Blog home");
            eyes.setForceFullPageScreenshot(true);
        }
        driver.findElement(By.cssSelector("a.profile-link")).click();
        if (null != eyes) {
            eyes.checkWindow("Blog profile");
        }
    }

    protected void checkProfilePage() {
        checkBlogPages(null);
    }

    protected void handleTestResults(Throwable ex, TestResults result) {
        System.out.println("\t\t" + result);
        System.out.printf("\t\tBrowser = %s,OS = %s, viewport = %dx%d, matched = %d,mismatched = %d, missing = %d,aborted = %s\n",
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
}
