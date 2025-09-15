package org.wikipedia.runner;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
 
@CucumberOptions(
        features = "src/test/resources/features",   // path to your feature files
        glue = {"org.wikipedia.stepdefs", "org.wikipedia.hooks"},        // package(s) for steps & hooks
        plugin = {
                "pretty",
                "html:target/cucumber-reports.html",
                "json:target/cucumber-reports.json",
                "junit:target/cucumber-reports.xml"
        },
        publish = true,
        monochrome = true
        //tags = "@InvalidLogin and @Login" 
)
public class TestNGRunner extends AbstractTestNGCucumberTests  {
	private static ThreadLocal<String> browserName = new ThreadLocal<>();
 
	@BeforeTest(alwaysRun = true)
    @Parameters({"browser"})
    public void setBrowser(String browser) {
        browserName.set(browser);
        System.out.println(">>> Browser from testng.xml = " + browser);
    }
 
    public static String getBrowser() {
       System.out.println("Return BrowserName from Runner:" + browserName.get());
    	return browserName.get();
    }
}