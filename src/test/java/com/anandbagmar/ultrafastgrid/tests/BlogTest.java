package com.anandbagmar.ultrafastgrid.tests;

import com.anandbagmar.ultrafastgrid.BlogBaseTest;
import org.testng.*;

import java.lang.reflect.*;

public class BlogTest extends BlogBaseTest {

    //    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {
        setupBeforeMethod(method);
    }

    //    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {
        quitDriver();
    }

    //    @Test(description = "Blogs in 2019")
    public void blogIn2019() {
        String url = "https://essenceoftesting.blogspot.com";
        checkBlogPages(url);
    }

    //    @Test(description = "Blogs in 2019 - Mobile View")
    public void blogIn2019MobileView() {
        String url = "https://essenceoftesting.blogspot.com/?m=1";
        checkBlogPages(url);
    }

    //    @Test(description = "Blog profile")
    public void seeProfile() {
        checkProfilePage();
    }

//    //    @Test(description = "Blogs in 2019")
//    public void blogIn2019_firefox() {
//        String url = "https://essenceoftesting.blogspot.com";
//        checkBlogPages(url);
//    }
//
//    //    @Test(description = "Blogs in 2019 - Mobile View")
//    public void blogIn2019MobileView_firefox() {
//        String url = "https://essenceoftesting.blogspot.com/?m=1";
//        checkBlogPages(url);
//    }
//
//    //    @Test(description = "Blog profile")
//    public void seeProfile_firefox() {
//        checkProfilePage();
//    }
}
