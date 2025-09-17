package org.wikipedia.hooks;
import java.time.Duration;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.wikipedia.pages.HomePage;
import org.wikipedia.pages.LoginPage;
import org.wikipedia.pages.MainPage;
import org.wikipedia.runner.TestNGRunner;
import org.wikipedia.utilities.DriverUtilities;

import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {
	//Thread-local page objects
	private static final ThreadLocal<MainPage> MAIN_PAGE = new ThreadLocal<>();
	private static final ThreadLocal<LoginPage> LOGIN_PAGE = new ThreadLocal<>();
	private static final ThreadLocal<HomePage> HOME_PAGE = new ThreadLocal<>();
	
	//Fetching driver from DriverUtilities' ThreadLocal
	public static RemoteWebDriver getDriver() {
		return DriverUtilities.getDriver();
	};
	
	// Per-thread getters for page objects
	public static MainPage mainPage()   { return MAIN_PAGE.get(); }
	public static LoginPage loginPage() { return LOGIN_PAGE.get(); }
	public static HomePage homePage()   { return HOME_PAGE.get(); }

	
	
	@Before
	public void init() { 
		 String browser = TestNGRunner.getBrowser();
		    if (browser == null || browser.isBlank()) {
		        browser = System.getProperty("browser", "chrome");
		    }

		    DriverUtilities.createDriver(browser);

		    RemoteWebDriver d = getDriver();
		    d.manage().window().maximize();
		    d.manage().window().setSize(new Dimension(1440, 900));
		    d.manage().timeouts().implicitlyWait(Duration.ofSeconds(1)); 

		    // Create page objects for this thread
		    MAIN_PAGE.set(new MainPage(d));
		    LOGIN_PAGE.set(new LoginPage(d));
		    HOME_PAGE.set(new HomePage(d));
	}
	
	
	@After
	public void tearDown() {
	    // Clear this thread’s page objects first
	    MAIN_PAGE.remove();
	    LOGIN_PAGE.remove();
	    HOME_PAGE.remove();

	    // Then quit only this thread’s driver
	    DriverUtilities.resetDriver();
	}
	
	
}



/*
 * public class Hooks { public static RemoteWebDriver driver; public static
 * MainPage mainPage; public static LoginPage loginPage; public static HomePage
 * homePage;
 * 
 * 
 * @Before public void init() {
 * 
 * String browser = TestNGRunner.getBrowser(); if (browser == null ||
 * browser.isBlank()) { browser = System.getProperty("browser", "chrome"); }
 * System.out.println(">>> Browser from Hooks = " + browser);
 * 
 * // Setting up the driver DriverUtilities.createDriver(browser);
 * 
 * // Expose drive to step defs driver = DriverUtilities.getDriver();
 * driver.manage().window().maximize();
 * driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
 * 
 * 
 * // Page Init //loginPage = new LoginPage(driver); //allOrdersPage = new
 * AllOrdersPage(driver); //myAccount = new MyAccount(driver); //shopCartPage =
 * new ShopCartPage(driver); mainPage = new MainPage(driver); loginPage = new
 * LoginPage(driver); homePage = new HomePage(driver);
 * 
 * 
 * }
 * 
 * @After public void tearDown() { DriverUtilities.resetDriver(); }
 * 
 * }
 */