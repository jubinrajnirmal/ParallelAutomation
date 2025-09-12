package org.wikipedia.pages;

import static org.junit.Assert.assertTrue;

import java.time.Duration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.wikipedia.data.DataFile;
import java.util.List;
import org.openqa.selenium.support.ui.Select;

/**
 * Chrome-only JUnit 4 test class. Right-click this file in Eclipse → Run As →
 * JUnit Test.
 *
 * Requires selenium-java (v4+) and junit (v4) on the classpath.
 */
public class test {

	private WebDriver driver;
	private WebDriverWait wait;

	@Before
	public void setUp() {
		FirefoxOptions options = new FirefoxOptions();
		// Uncomment the next line if you want headless mode:
		// options.addArguments("--headless=new");
		options.addArguments("--disable-gpu", "--no-sandbox");

		driver = new FirefoxDriver(options);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5)); // use explicit waits
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	@After
	public void tearDown() {
		if (driver != null) {
			try {
				driver.quit();
			} catch (Throwable ignored) {
			}
		}
	}

	private By loginBtn = By.id("pt-login-2");
	private By pageTitle = By.xpath("//*[contains(@id,'Welcome_to_Wikipedia')]");		
	public boolean isPageTitleCorrect() {
		String pageTitleText = driver.findElement(pageTitle).getText().trim();
		return pageTitleText.equals("Welcome to Wikipedia");
	}
	public boolean isLoginBtnVisible() {
		return driver.findElement(loginBtn).isDisplayed();
	}
	public void clickLoginBtn() {
		driver.findElement(loginBtn).click();
	}
	
	@Test
	public void locatorPlayground() throws InterruptedException {
		driver.get(DataFile.mainPageURL);
		Thread.sleep(5000);
		isPageTitleCorrect();
		assertTrue("Page title is not correct", isPageTitleCorrect());
		Thread.sleep(2000);
		isLoginBtnVisible();
		assertTrue("Login button is not visible", isLoginBtnVisible());
		clickLoginBtn();
		Thread.sleep(5000);		
		assertTrue("Login page is not displayed", driver.getCurrentUrl().contains("auth.wikimedia.org"));
	}

	// Optional helper if you want to probe session state while debugging
	@SuppressWarnings("unused")
	private static boolean isAlive(WebDriver d) {
		try {
			return ((RemoteWebDriver) d).getSessionId() != null;
		} catch (Throwable t) {
			return false;
		}
	}
}

