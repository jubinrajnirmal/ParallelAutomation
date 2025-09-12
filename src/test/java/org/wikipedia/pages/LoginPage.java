package org.wikipedia.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
	private final WebDriver driver;
	private final WebDriverWait wait;

// CTOR
	public LoginPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(18));
	}

// Locators
	private final By loginPageTitle = By.xpath("//*[contains(@id,'firstHeading')]");
	private final By usernameField = By.id("wpName1");
	private final By passwordField = By.id("wpPassword1");
	private final By loginButton = By.id("wpLoginAttempt");
	private final By rememberMeInput = By.id("wpRemember"); // may be hidden
	private final By rememberMeLabel = By.cssSelector("label[for='wpRemember']"); // visible control
	private final By errorMessage = By.cssSelector(".cdx-message--error .cdx-message__content");
	private final By captcha = By.xpath("//div[contains(@class,'fancycaptcha-image')]");
	
	
// Helpers
	private WebElement visible(By locator) {
		return wait.ignoring(StaleElementReferenceException.class)
				.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	private WebElement clickable(By locator) {
		return wait.ignoring(StaleElementReferenceException.class)
				.until(ExpectedConditions.elementToBeClickable(locator));
	}

	private WebElement present(By locator) {
		return wait.ignoring(StaleElementReferenceException.class)
				.until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	
	private void safeClick(By locator) {
		clickable(locator);
		for (int i = 0; i < 3; i++) {
			try {
				driver.findElement(locator).click();
				return;
			} catch (StaleElementReferenceException | ElementClickInterceptedException e) {
				if (i == 2)
					throw e;
				wait.until(ExpectedConditions.elementToBeClickable(locator));
			}
		}
	}

	private void type(By locator, String value) {
		WebElement el = visible(locator);
		el.clear();
		el.sendKeys(value);
	}

	private boolean jsChecked(WebElement input) {
		Object v = ((JavascriptExecutor) driver).executeScript("return arguments[0].checked === true;", input);
		return Boolean.TRUE.equals(v);
	}

// Actions
	public boolean isLoginPageTitleCorrect() {
		return visible(loginPageTitle).getText().trim().equals("Log in");
	}

	public boolean isUsernameFieldVisible() {
		return visible(usernameField).isDisplayed();
	}

	public boolean isPasswordFieldVisible() {
		return visible(passwordField).isDisplayed();
	}

	public boolean isLoginButtonVisible() {
		return visible(loginButton).isDisplayed();
	}

	public void enterValidUsername(String username) {
		type(usernameField, username);
	}

	public void enterValidPassword(String password) {
		type(passwordField, password);
	}

	public void clickLoginButton() {
		safeClick(loginButton);
	}

// Remember-me: interact via label, read state via JS (input may be hidden)
	public void clickRememberMeCheckbox() {
		boolean before = isRememberMeCheckboxSelected();
		try {
			safeClick(rememberMeLabel);
		} catch (Exception e) {
			// Fallback: click hidden input via JS if label not clickable
			WebElement input = present(rememberMeInput);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", input);
		}
		WebElement input = present(rememberMeInput);
		wait.until(d -> jsChecked(input) != before);
	}

	public boolean isRememberMeCheckboxSelected() {
		WebElement input = present(rememberMeInput); // presence, not visibility
		return jsChecked(input);
	}

	public boolean isCaptchaPresent() {
		// presence + displayed; no long waits to avoid blocking error checks
		return driver.findElements(captcha).stream().anyMatch(WebElement::isDisplayed);
	}

	public boolean isErrorMessageVisible() {
		return visible(errorMessage).isDisplayed();
	}

	public String getErrorMessageText() {
		return visible(errorMessage).getText();
	}

	public void leaveFieldEmpty(String fieldName) {
		if (fieldName.equalsIgnoreCase("Username")) {
			WebElement el = visible(usernameField);
			el.clear();
		} else if (fieldName.equalsIgnoreCase("Password")) {
			WebElement el = visible(passwordField);
			el.clear();
		}
	}

	public void enterInField(String fieldName, String value) {
		if (fieldName.equalsIgnoreCase("Username")) {
			type(usernameField, value);
		} else if (fieldName.equalsIgnoreCase("Password")) {
			type(passwordField, value);
		}
	}

	public String getValidationMessage(String fieldName) {
		WebElement field;
		if (fieldName.equalsIgnoreCase("Username")) {
			field = visible(usernameField);
		} else if (fieldName.equalsIgnoreCase("Password")) {
			field = visible(passwordField);
		} else {
			throw new IllegalArgumentException("Unknown field: " + fieldName);
		}
		return (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].validationMessage;", field);
	}
}

/*
 * package org.wikipedia.pages;
 * 
 * import java.time.Duration;
 * 
 * import org.openqa.selenium.By; import
 * org.openqa.selenium.ElementClickInterceptedException; import
 * org.openqa.selenium.JavascriptExecutor; import
 * org.openqa.selenium.StaleElementReferenceException; import
 * org.openqa.selenium.WebDriver; import org.openqa.selenium.WebElement; import
 * org.openqa.selenium.support.ui.ExpectedConditions; import
 * org.openqa.selenium.support.ui.WebDriverWait;
 * 
 * public class LoginPage { private final WebDriver driver; private final
 * WebDriverWait wait;
 * 
 * //CTOR public LoginPage(WebDriver driver) { this.driver = driver; this.wait =
 * new WebDriverWait(driver, Duration.ofSeconds(18)); }
 * 
 * 
 * //Locator address using By private By loginPageTitle =
 * By.xpath("//*[contains(@id,'firstHeading')]"); private By usernameField =
 * By.id("wpName1"); private By passwordField = By.id("wpPassword1"); private By
 * loginButton = By.id("wpLoginAttempt"); private By rememberMeCheckbox =
 * By.id("wpRemember"); private By errorMessage =
 * By.cssSelector(".cdx-message--error .cdx-message__content");
 * 
 * private WebElement visible(By locator) { return
 * wait.ignoring(StaleElementReferenceException.class)
 * .until(ExpectedConditions.visibilityOfElementLocated(locator)); }
 * 
 * private WebElement clickable(By locator) { return
 * wait.ignoring(StaleElementReferenceException.class)
 * .until(ExpectedConditions.elementToBeClickable(locator)); }
 * 
 * private void safeClick(By locator) { clickable(locator); for (int i = 0; i <
 * 3; i++) { try { driver.findElement(locator).click(); return; } catch
 * (StaleElementReferenceException | ElementClickInterceptedException e) { if (i
 * == 2) throw e; wait.until(ExpectedConditions.elementToBeClickable(locator));
 * } } }
 * 
 * private void type(By locator, String value) { WebElement el =
 * visible(locator); el.clear(); el.sendKeys(value); }
 * 
 * private boolean isDisplayed(By locator) { return
 * visible(locator).isDisplayed(); }
 * 
 * // Actions public boolean isLoginPageTitleCorrect() { return
 * visible(loginPageTitle).getText().trim().equals("Log in"); }
 * 
 * public boolean isUsernameFieldVisible() { return isDisplayed(usernameField);
 * }
 * 
 * public boolean isPasswordFieldVisible() { return isDisplayed(passwordField);
 * }
 * 
 * public boolean isLoginButtonVisible() {
 * driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5)); return
 * isDisplayed(loginButton); }
 * 
 * public void enterValidUsername(String Username) { type(usernameField,
 * Username); }
 * 
 * public void enterValidPassword(String Password) { type(passwordField,
 * Password); }
 * 
 * public void clickLoginButton() { safeClick(loginButton); }
 * 
 * public void clickRememberMeCheckbox() { safeClick(rememberMeCheckbox); }
 * 
 * public boolean isRememberMeCheckboxSelected() { return
 * visible(rememberMeCheckbox).isSelected(); }
 * 
 * public boolean isErrorMessageVisible() { return
 * visible(errorMessage).isDisplayed(); }
 * 
 * public String getErrorMessageText() { return visible(errorMessage).getText();
 * }
 * 
 * public void leaveFieldEmpty(String fieldName) { if
 * (fieldName.equalsIgnoreCase("Username")) { WebElement el =
 * visible(usernameField); el.clear(); } else if
 * (fieldName.equalsIgnoreCase("Password")) { WebElement el =
 * visible(passwordField); el.clear(); } }
 * 
 * public void enterInField(String fieldName, String value) { if
 * (fieldName.equalsIgnoreCase("Username")) { type(usernameField, value); } else
 * if (fieldName.equalsIgnoreCase("Password")) { type(passwordField, value); } }
 * 
 * public String getValidationMessage(String fieldName) { WebElement field; if
 * (fieldName.equalsIgnoreCase("Username")) { field = visible(usernameField); }
 * else if (fieldName.equalsIgnoreCase("Password")) { field =
 * visible(passwordField); } else { throw new
 * IllegalArgumentException("Unknown field: " + fieldName); } return (String)
 * ((JavascriptExecutor) driver)
 * .executeScript("return arguments[0].validationMessage;", field); }
 * 
 * }
 * 
 * //Actions
 * 
 * public boolean isLoginPageTitleCorrect() { String loginPageTitleText =
 * driver.findElement(loginPageTitle).getText().trim(); return
 * loginPageTitleText.equals("Log in"); }
 * 
 * public boolean isUsernameFieldVisible() { return
 * driver.findElement(usernameField).isDisplayed(); }
 * 
 * public boolean isPasswordFieldVisible() { return
 * driver.findElement(passwordField).isDisplayed(); }
 * 
 * public boolean isLoginButtonVisible() { return
 * driver.findElement(loginButton).isDisplayed(); }
 * 
 * public void enterValidUsername(String Username) {
 * driver.findElement(usernameField).sendKeys(Username); }
 * 
 * public void enterValidPassword(String Password) {
 * driver.findElement(passwordField).sendKeys(Password); }
 * 
 * public void clickLoginButton() { driver.findElement(loginButton).click(); }
 * 
 * public void clickRememberMeCheckbox() {
 * driver.findElement(rememberMeCheckbox).click(); }
 * 
 * public boolean isRememberMeCheckboxSelected() { return
 * driver.findElement(rememberMeCheckbox).isSelected(); }
 * 
 * public boolean isErrorMessageVisible() { return
 * driver.findElement(errorMessage).isDisplayed(); }
 * 
 * public String getErrorMessageText() { WebDriverWait wait = new
 * WebDriverWait(driver, Duration.ofSeconds(10)); WebElement errorMsgElement =
 * wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
 * return errorMsgElement.getText(); }
 * 
 * public void leaveFieldEmpty(String fieldName) { if
 * (fieldName.equalsIgnoreCase("Username")) {
 * driver.findElement(usernameField).clear(); } else if
 * (fieldName.equalsIgnoreCase("Password")) {
 * driver.findElement(passwordField).clear(); } }
 * 
 * public void enterInField(String fieldName, String value) { if
 * (fieldName.equalsIgnoreCase("Username")) {
 * driver.findElement(usernameField).sendKeys(value); } else if
 * (fieldName.equalsIgnoreCase("Password")) {
 * driver.findElement(passwordField).sendKeys(value); } }
 * 
 * public String getValidationMessage(String fieldName) { WebElement field; if
 * (fieldName.equalsIgnoreCase("Username")) { field =
 * driver.findElement(usernameField); } else if
 * (fieldName.equalsIgnoreCase("Password")) { field =
 * driver.findElement(passwordField); } else { throw new
 * IllegalArgumentException("Unknown field: " + fieldName); }
 * 
 * return (String) ((JavascriptExecutor) driver)
 * .executeScript("return arguments[0].validationMessage;",field); }
 * 
 * }
 */