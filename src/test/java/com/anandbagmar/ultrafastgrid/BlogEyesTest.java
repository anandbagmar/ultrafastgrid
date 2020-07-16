package com.anandbagmar.ultrafastgrid;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.selenium.Eyes;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class BlogEyesTest extends BlogBaseTest {
    private final String siteName = "blog-eyes-classic-test";
    private final RectangleSize viewportSize = new RectangleSize(1024, 768);
    private static BatchInfo batch;

    @BeforeClass
    public void beforeClass() {
        batch = new BatchInfo(siteName);
        System.out.println("BlogEyesTest: BeforeClass: Batch name: '" + siteName + "'");
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        setupBeforeMethod(siteName, method, viewportSize, false, batch);
    }

    @Test(description = "Blogs in 2019")
    public void blogIn2019() {
        String url = "https://essenceoftesting.blogspot.com";
        Eyes eyes = getEyes();
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

//    @Test(description = "Blogs in 2019")
//    public void blogIn2019_firefox() {
//        Eyes eyes = getEyes();
//        String url = "https://essenceoftesting.blogspot.com";
//        eyes.setForceFullPageScreenshot(false);
//        checkBlogPages(eyes, url);
//    }
//
//    @Test(description = "Blogs in 2019 - Mobile View")
//    public void blogIn2019MobileView_firefox() {
//        String url = "https://essenceoftesting.blogspot.com/?m=1";
//        checkBlogPages(getEyes(), url);
//    }
//
//    @Test(description = "Blog profile")
//    public void seeProfile_firefox() {
//        checkProfilePage(getEyes());
//    }

}
