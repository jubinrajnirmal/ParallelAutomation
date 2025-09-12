package org.wikipedia.utilities;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverUtilities {

	protected static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<RemoteWebDriver>();

	private DriverUtilities() {
	}

	public static void resetDriver() {
		if (getDriver() != null) {
			getDriver().quit();
			driver.remove();
		}
	}

	public static RemoteWebDriver getDriver() {
		return driver.get();
	}

	@SuppressWarnings("deprecation")
	public static void createDriver(String browser) {
		System.out.println("BrowserName from DriverUtil is " + browser);
		URL gridURL;
		try {
			gridURL = new URL("http://localhost:4444");
		} catch (MalformedURLException e) {
			throw new RuntimeException("Malformed URL", e);
		}

		RemoteWebDriver remoteDriver;

		switch (browser.toLowerCase()) {
		case "chrome":
			remoteDriver = new RemoteWebDriver(gridURL, new ChromeOptions());
			break;
		case "firefox":
			remoteDriver = new RemoteWebDriver(gridURL, new FirefoxOptions());
			break;
		case "edge":
			remoteDriver = new RemoteWebDriver(gridURL, new EdgeOptions());
			break;
		default:
			throw new IllegalArgumentException("Invalid browser: " + browser);
		}

		driver.set(remoteDriver);
	}

}
	/*// 1 Private Static Instance
	private static DriverUtilities driverUtilities;
	private WebDriver driver;

	// 2 Private Constructor
	private DriverUtilities() {
		super();
	}

	// 3 Public static getInstance()
	public static synchronized DriverUtilities getInstance() {
		if (driverUtilities == null) {
			driverUtilities = new DriverUtilities();
		}
		return driverUtilities;
	}

	public synchronized WebDriver getDriver() {
		if (driver == null || !isSessionActive(driver)) {
			createDriver();
		}
		return driver;
	}

	public synchronized void quitDriver() {
		try {
			if (driver != null) {
				driver.quit();
			}
		} finally {
			driver = null;
		}
	}
	
	private boolean isSessionActive(WebDriver d) {
		try {
			return d != null && ((RemoteWebDriver) d).getSessionId() != null;
		} catch (Exception e) {
			return false;
		}
	}
	
	private void createDriver() {
		String driverName = getDriverName();
		//if (driverName == null) driverName = "chrome";
		switch (driverName.trim().toLowerCase()) {
		case "chrome":
			this.driver = new ChromeDriver();
			break;
		case "firefox":
			this.driver = new FirefoxDriver();
			break;
		default:
			break;
		}
	}

	private String getDriverName() {
		// Return the browser that is going to used for auto testing.
		// Chrome/Firefox/Edge

		Properties config = new Properties();

		try {
			config.load(new FileInputStream("src/test/resources/config.properties"));
		} catch (IOException e) {
			
		}

		return config.getProperty("browser");
	}

}
*/