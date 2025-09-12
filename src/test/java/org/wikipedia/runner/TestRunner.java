package org.wikipedia.runner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@SuppressWarnings("deprecation")
@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/Features", glue = { "org.wikipedia.stepdefs",
		"org.wikipedia.hooks" }, tags = "@Login", plugin = { "pretty", "json:Reports/cucumber.json",
				"junit:Reports/cucumber.junit",
				"html:Reports/cucumber-reports.html" }, monochrome = false, dryRun = false, publish = true

)

public class TestRunner {

}
