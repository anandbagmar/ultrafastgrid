package com.anandbagmar.ultrafastgrid.utilities;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverUtils {
    public static String getPathForChromeDriverFromMachine() {
        WebDriverManager.chromedriver().setup();
        String chromeDriverPath = WebDriverManager.chromedriver().getDownloadedDriverPath();
        System.out.println("ChromeDriver path: " + chromeDriverPath);
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        return chromeDriverPath;
    }

    public static String getPathForFirefoxDriverFromMachine() {
        WebDriverManager.firefoxdriver().setup();
        String firefoxDriverPath = WebDriverManager.firefoxdriver().getDownloadedDriverPath();
        System.out.println("FirefoxDriver path: " + firefoxDriverPath);
        System.setProperty("webdriver.firefox.driver", firefoxDriverPath);
        return firefoxDriverPath;
    }
}
