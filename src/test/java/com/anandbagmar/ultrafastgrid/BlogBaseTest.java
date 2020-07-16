package com.anandbagmar.ultrafastgrid;

import com.applitools.eyes.selenium.Eyes;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;

public abstract class BlogBaseTest extends BaseTest {
    protected HashMap<String, String> getBlogPosts() {
//        WebDriver driver = getDriver();

        HashMap<String, String> blogPosts = new HashMap<>();
//        List<WebElement> listOfBlogs = driver.findElements(By.xpath("//a[contains(@href,\".com/2019\")]"));
//        driver.findElements(By.xpath("//a[contains(@href,\".com/2019\")]")).forEach(ele -> {
//            String title = ele.getText();
//            String href = ele.getAttribute("href");
//            if (href.contains(".html") && !href.contains("#") && ele.getAttribute("class").isEmpty()) {
//                blogPosts.put(href, title);
//            }
//        });
//        blogPosts.clear();
        blogPosts.put("http://essenceoftesting.blogspot.com/2020/03/tracking-functional-coverage.html", "Tracking functional coverage from your api / functional UI (e2e) tests");
        blogPosts.put("https://essenceoftesting.blogspot.com/2019/02/talks-and-workshops-in-agile-india-2019.html", "Talks and workshops in Agile India 2019");
        blogPosts.put("https://essenceoftesting.blogspot.com/2019/10/overcoming-chromedriver-version.html", "Chrome driver version");
        return blogPosts;
    }

    protected void checkBlogPages(String url) {
        checkBlogPages(null, url);
    }

    protected void checkBlogPages(Eyes eyes, String url) {
        WebDriver driver = getDriver();

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
        WebDriver driver = getDriver();

        String url = "https://essenceoftesting.blogspot.com";
        driver.get(url);
        if (null != eyes) {
            eyes.setForceFullPageScreenshot(false);
            eyes.checkWindow("Blog home");
            eyes.setForceFullPageScreenshot(true);
        }
        driver.get("https://www.blogger.com/profile/12067921188948137145");
        if (null != eyes) {
            eyes.checkWindow("Blog profile");
        }
    }

    protected void checkProfilePage() {
        checkProfilePage(null);
    }
}
