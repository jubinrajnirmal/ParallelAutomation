package org.wikipedia.hooks;
import java.time.Duration;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.wikipedia.pages.HomePage;
import org.wikipedia.pages.LoginPage;
import org.wikipedia.pages.MainPage;
import org.wikipedia.runner.TestNGRunner;
import org.wikipedia.utilities.DriverUtilities;

import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {
	public static RemoteWebDriver driver;
	public static MainPage mainPage;
	public static LoginPage loginPage;
	public static HomePage homePage;

	@Before
	public void init() {
		
		String browser = TestNGRunner.getBrowser();
		if (browser == null || browser.isBlank()) {
			browser = System.getProperty("browser", "chrome");
		}
		System.out.println(">>> Browser from Hooks = " + browser);
		
		// Setting up the driver
		DriverUtilities.createDriver(browser);
		
		// Expose drive to step defs
		driver = DriverUtilities.getDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));


		// Page Init
		//loginPage = new LoginPage(driver);
		//allOrdersPage = new AllOrdersPage(driver);
		//myAccount = new MyAccount(driver);
		//shopCartPage = new ShopCartPage(driver);
		mainPage = new MainPage(driver);
		loginPage = new LoginPage(driver);
		homePage = new HomePage(driver);


	}

	@After
	public void tearDown() {
			DriverUtilities.resetDriver();
	}

}
