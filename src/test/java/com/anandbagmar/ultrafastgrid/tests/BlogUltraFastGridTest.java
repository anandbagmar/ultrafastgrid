package com.anandbagmar.ultrafastgrid.tests;

import com.anandbagmar.ultrafastgrid.BlogBaseTest;
import com.applitools.eyes.*;
import com.applitools.eyes.selenium.*;
import org.testng.annotations.*;

import java.lang.reflect.*;

public class BlogUltraFastGridTest extends BlogBaseTest {
    private final String appName = "blog-eyes-ufg-test";
    RectangleSize viewportSize = new RectangleSize(1024, 768);
    private boolean isDisabled = false;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {
        setupBeforeMethod(appName, method, viewportSize, false, isDisabled, "chrome");
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
