# Wikipedia Parallel Automation Suite

Automated UI regression tests for key Wikipedia flows (account login, home page checks, etc.) built with Selenium WebDriver, Cucumber, and TestNG. The suite is optimized for data-driven testing, parallel execution on a Selenium Grid, and CI/CD integration via Jenkins.

## Highlights
- Parallel cross-browser execution against a Selenium Grid using TestNG and thread-safe drivers.
- Cucumber Scenario Outlines whose `Examples` tables are generated automatically from Excel workbooks.
- Centralised test data overrides loaded from `LoginData.xlsx` at runtime.
- Reporting outputs in HTML, JSON, and JUnit XML for local analysis or pipeline publishing.

## Project Layout
```text
ParallelAutomation/
|-- pom.xml                  # Maven build + plugins (Surefire, Exec)
|-- Jenkinsfile              # Sample Jenkins pipeline
|-- testng.xml               # Cross-browser parallel suite definition
|-- src/test/java/
|   |-- org.wikipedia.data/      # Runtime data config (DataFile)
|   |-- org.wikipedia.hooks/     # Cucumber hooks / driver lifecycle
|   |-- org.wikipedia.pages/     # Page Object Model classes
|   |-- org.wikipedia.runner/    # TestNG + Cucumber runners
|   `-- org.wikipedia.utilities/ # Driver utilities & Excel helpers
`-- src/test/resources/
    |-- Features/                # Feature templates with Excel markers
    |   `-- _generated/          # Generated feature files (git-ignored)
    `-- TestData/LoginData.xlsx  # Data source for tests
```

## Prerequisites
- Java 21 (ensure `JAVA_HOME` points to the JDK).
- Maven 3.9+.
- Selenium Grid running and reachable at `http://localhost:4444` (matches `DriverUtilities`).
  - Quick start (Docker): `docker run -d -p 4444:4444 -p 7900:7900 --name selenium-grid selenium/standalone-chrome:latest`.
- Optional: Google Chrome, Firefox, and Edge installed on the Grid nodes for full cross-browser coverage.

## Getting Started
1. Clone the repository and install dependencies:
   ```bash
   git clone <repo-url>
   cd ParallelAutomation
   mvn dependency:resolve
   ```
2. Update `src/test/resources/TestData/LoginData.xlsx` (sheet `Config`) if you need different URLs or credentials.
3. Ensure your Selenium Grid is running before executing the suite.

## Running the Suite
- Execute the full parallel suite (three browsers in parallel):
  ```bash
  mvn clean test
  ```
  Surefire is configured to use `testng.xml`, which supplies the `browser` parameter to each TestNG test.

- Run a single browser from the command line (overrides TestNG parameter):
  ```bash
  mvn clean test -Dbrowser=chrome
  ```
  Any TestNG parameter can also be overridden with `-Dparameter=value`.

- Filter scenarios by Cucumber tag:
  ```bash
  mvn clean test -Dcucumber.filter.tags="@smoke"
  ```

### Working with the Excel Feature Generator
- Feature templates live in `src/test/resources/Features`. To link an Examples table to Excel data, add a marker comment:
  ```
  Examples:    # fromExcel: TestData/LoginData.xlsx|SheetName
    | columnA | columnB |
  ```
- During the Maven `test-compile` phase, the Exec plugin runs `ExcelExamplesGenerator`:
  - Reads markers in the template.
  - Pulls matching rows from the specified Excel sheet.
  - Writes fully-populated features to `Features/_generated` (consumed by Cucumber).
- Customise the input/output directories via system properties: `-Dexcel.in=...` and `-Dexcel.out=...`.

### Runtime Test Data
`DataFile` uses `ExcelConfig` to load key/value pairs from `LoginData.xlsx` (sheet `Config`). If the workbook is unavailable, sane defaults defined in code are used. Update the sheet to tweak URLs, credentials, or other shared constants without redeploying the code.

## Reporting & Outputs
After each run you will find:
- `target/cucumber-reports.html` - detailed Cucumber HTML report.
- `target/cucumber-reports.{json,xml}` - JSON/JUnit outputs for pipelines.
- `test-output/` - vanilla TestNG HTML reports per suite.
- Screenshots and raw dumps (if captured) are stored under `target/surefire-reports`.

## Jenkins Integration
A lightweight `Jenkinsfile` is included as a starting point. Typical stages:
1. Checkout repository.
2. Set up JDK and Maven toolchains.
3. Start/verify Selenium Grid availability.
4. Run `mvn clean test` (optionally pass browser or tag parameters).
5. Publish reports (`cucumber-reports.html`, TestNG reports, JSON/XML artefacts).

## Troubleshooting
- **`Connection refused` to Grid**: confirm the Grid URL matches the one in `DriverUtilities` or override with `-Dselenium.grid.url` after extending the code to read it.
- **Empty generated features**: ensure the Excel sheet names and header columns exactly match the marker comment.
- **Parallel driver collisions**: ThreadLocal drivers and page objects are already used. If you add new stateful utilities, make them thread-safe.

## Extending the Suite
- Add new feature templates under `src/test/resources/Features` and corresponding step definitions in `src/test/java/org/wikipedia/stepdefs`.
- Extend `DriverUtilities` to support additional browsers or remote capabilities (for example, mobile emulation).
- Store environment-specific data in new Excel sheets; call `ExcelConfig.get("key", default)` wherever needed.

Happy testing!
