[![CodeQL](https://github.com/anandbagmar/ultrafastgrid/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/anandbagmar/ultrafastgrid/actions/workflows/codeql-analysis.yml)

[![Java CI with Gradle](https://github.com/anandbagmar/ultrafastgrid/actions/workflows/buildInCI.yml/badge.svg)](https://github.com/anandbagmar/ultrafastgrid/actions/workflows/buildInCI.yml)

## Prerequisites
* Set the Applitools API key as an environment variable - *APPLITOOLS_API_KEY* (You can register at www.applitools.com and get a trial account)

# Running the tests

### Run without Applitools
* Run without Applitools:

    `./gradlew test --tests "*ApplitoolsShoppingTest"`


* Run without Applitools (new build): 

    `INJECT=true ./gradlew test --tests "ApplitoolsShoppingTest"`

### Run with Applitools 
* Run with UFG: 

    `EYES=true USE_UFG=true ./gradlew test --tests "ApplitoolsShoppingWithEyesTest"` 


* Run with UFG on Execution Cloud: 

    `BROWSER=self_healing EYES=true USE_UFG=true ./gradlew test --tests "ApplitoolsShoppingWithEyesTest"`


* Run with UFG on Execution Cloud (with self healing): 

    `INJECT=true BROWSER=self_healing EYES=true USE_UFG=true ./gradlew test --tests "ApplitoolsShoppingWithEyesTest"`


### Options:
```
BROWSER=chrome [chrome|firefox|edge|safari|self_healing]

USE_UFG=false [true|false]

EYES=false [true|false]

INJECT=false [true|false]
```