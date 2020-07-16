package com.anandbagmar.ultrafastgrid;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class BlogTest extends BlogBaseTest {

    @BeforeMethod
    public void beforeMethod(Method method) {
        setupBeforeMethod(method);
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        quitDriver();
    }

    @Test(description = "Blogs in 2019")
    public void blogIn2019() {
        String url = "https://essenceoftesting.blogspot.com";
        checkBlogPages(url);
    }

    @Test(description = "Blogs in 2019 - Mobile View")
    public void blogIn2019MobileView() {
        String url = "https://essenceoftesting.blogspot.com/?m=1";
        checkBlogPages(url);
    }

    @Test(description = "Blog profile")
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
