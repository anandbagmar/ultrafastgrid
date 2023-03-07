[![CodeQL](https://github.com/anandbagmar/ultrafastgrid/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/anandbagmar/ultrafastgrid/actions/workflows/codeql-analysis.yml)

[![Java CI with Gradle](https://github.com/anandbagmar/ultrafastgrid/actions/workflows/buildInCI.yml/badge.svg)](https://github.com/anandbagmar/ultrafastgrid/actions/workflows/buildInCI.yml)

## Prerequisites
* Set the Applitools API key as an environment variable - *APPLITOOLS_API_KEY* (You can register at www.applitools.com and get a trial account)

# Running the tests
Run the tests from the IDE or from command line
```
./gradlew clean test
```

or, you can run the testng tests from your IDE

Example commands:
```
BROWSER=chrome USE_UFG=false  ./gradlew test
BROWSER=firefox USE_UFG=false  ./gradlew test

BROWSER=firefox USE_UFG=false  ./gradlew test --tests "*GitHub*"
```

Defaults:
```
BROWSER=chrome
USE_UFG=false
```