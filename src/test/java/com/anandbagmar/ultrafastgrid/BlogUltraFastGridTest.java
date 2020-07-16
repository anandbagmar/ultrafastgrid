package com.anandbagmar.ultrafastgrid;

import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.selenium.Eyes;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class BlogUltraFastGridTest extends BlogBaseTest {
    final int concurrency = 20;
    private final String siteName = "blog-eyes-ufg-test";
    RectangleSize viewportSize = new RectangleSize(1024, 768);

    @BeforeClass
    public void beforeClass() {
        setUpClass(siteName);
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        setupBeforeMethod(siteName, method, viewportSize, true);
    }

    @Test(description = "Blogs in 2019")
    public void blogIn2019() {
        Eyes eyes = getEyes();
        String url = "https://essenceoftesting.blogspot.com";
        eyes.setForceFullPageScreenshot(false);
        checkBlogPages(eyes, url);
    }

    @Test(description = "Blogs in 2019 - Mobile View")
    public void blogIn2019MobileView() {
        String url = "https://essenceoftesting.blogspot.com/?m=1";
        checkBlogPages(getEyes(), url);
    }

    @Test(description = "Blog profile")
    public void seeProfile() {
        checkProfilePage(getEyes());
    }
}
