## Prerequisites
* Get the appropriate version of the chrome driver and gecko driver
* Download the selenium-server-standalone jar (I used v3.9.1 and placed it in the lib directory) 
* Set the Applitools API key as an environment variable - *APPLITOOLS_API_KEY* (You can register at www.applitools.com and get a trial account)

## Setting up the SeleniumGrid
IF you want to run the Selenium Grid based tests, you will need to start the Selenium Grid hub and node(s) manually.

To start the Selenium Grid Hub:
```
java -jar lib/selenium-server-standalone-3.9.1.jar -role hub
```

to start the Selenium Grid Node. *Replace path to the drivers as on your machine*
```
java -Dwebdriver.chrome.driver=<path_to_chromedriver> -Dwebdriver.gecko.driver
=<path_to_geckodriver> -jar lib/selenium-server-standalone-3.9.1.jar -role node -hub http://localhost:4444/grid/register
```

# Running the tests
Run the tests from the IDE or from command line
```
./gradlew clean test
```

or, you can run the testng tests from your IDE