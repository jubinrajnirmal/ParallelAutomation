package org.wikipedia.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MainPage {
	private WebDriver driver;
	private WebDriverWait wait;

	// CTOR
	public MainPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(18));
	}

	// Helpers for stable element interaction
	private WebElement visible(By locator) {
		return wait.ignoring(StaleElementReferenceException.class)
				.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	private void safeClick(By locator) {
		// Wait until clickable, then try clicking up to 3 times to work around
		// flakiness
		wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(locator));
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

	// Locator address using By
	private By pageTitle = By.xpath("//*[contains(@id,'Welcome_to_Wikipedia')]");
	private By loginBtn = By.cssSelector("#pt-login-2");

	//Actions

	public boolean isPageTitleCorrect() {
		return visible(pageTitle).getText().trim().equals("Welcome to Wikipedia");
	}

	public boolean isLoginBtnVisible() {
		return visible(loginBtn).isDisplayed();
	}

	public void clickLoginBtn() {
	    safeClick(loginBtn);
	    // Wait for navigation toward login page; tolerant for Grid latency
	    new WebDriverWait(driver, Duration.ofSeconds(12))
	        .until(ExpectedConditions.urlContains("Special:UserLogin"));
	}
}