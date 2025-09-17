# Selenium Java Wikipedia Testing (Cucumber + TestNG + Jenkins )

UI tests for Wikipedia (homepage & login) using Selenium 4 (POM, Grid/parallel) with Apache POI–driven DDT.
Edit data in `src/test/resources/TestData/LoginData.xlsx` (`Config`, `InvalidLogins`, `Validation`, `HomeLanguages`); feature templates in `Features`, generated copies in `Features/_generated`.
Run: `mvn clean test` (Jenkins uses `Jenkinsfile`); reports land in `target/**` and Cucumber Cloud links are emailed.
Valid creds/URLs load from the Excel Config sheet; Scenario Outlines auto-fill `Examples` from Excel at build time.
