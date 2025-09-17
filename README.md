# Wikipedia Parallel Automation Suite

Selenium + Cucumber + TestNG suite that exercises core Wikipedia flows on a Selenium Grid.
Excel-driven Scenario Outline examples are generated before tests so data lives in `LoginData.xlsx`.
Run `mvn clean test` (uses `testng.xml`) and grab the reports from `target/cucumber-reports.html` or `test-output/`.
