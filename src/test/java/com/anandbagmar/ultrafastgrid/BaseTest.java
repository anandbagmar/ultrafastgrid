package com.anandbagmar.ultrafastgrid;

import com.applitools.eyes.BatchInfo;
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

    protected void checkBlogPages(Eyes eyes, String url) {
        driver.get(url);
        eyes.checkWindow("Blog home");
        eyes.setForceFullPageScreenshot(true);
        HashMap<String, String> blogPosts = getBlogPosts();
        blogPosts.keySet().forEach(key -> {
            System.out.println("title - " + blogPosts.get(key));
            System.out.println("url - " + key);
            driver.get(key);
            eyes.checkWindow("Blog title - " + blogPosts.get(key));
        });
    }
}
