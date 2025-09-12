package org.wikipedia.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
	private WebDriver driver;
	private WebDriverWait wait;
	
	//CTOR
	public HomePage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
	}
	
	
	// Helpers
	private WebElement visible(By locator) {
	    return wait.ignoring(StaleElementReferenceException.class)
	               .until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	private void safeClick(By locator) {
	    wait.ignoring(StaleElementReferenceException.class)
	        .until(ExpectedConditions.elementToBeClickable(locator));
	    for (int i = 0; i < 3; i++) {
	        try {
	            driver.findElement(locator).click();
	            return;
	        } catch (StaleElementReferenceException | ElementClickInterceptedException e) {
	            if (i == 2) throw e;
	            wait.until(ExpectedConditions.elementToBeClickable(locator));
	        }
	    }
	}

	@SuppressWarnings("unused")
	private void scrollIntoView(WebElement el) {
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
	}
	
	
	
	//Locator address using By
	private By wikiLogo = By.cssSelector("span.central-textlogo__image");
	private By readWikipediaDropdown = By.id("js-lang-list-button");
	private By languageListContainer = By.id("js-lang-lists");
	private By searchLanguageDropdown = By.id("searchLanguage");
	
	
	//Actions
	public boolean isWikiLogoDisplayed() {
	    return visible(wikiLogo).isDisplayed();
	}
	
	
	public void clickLanguageLink(String language) {
		// Building the locator based on the language parameter
		String langCode = getLangCode(language);
		By languageLink = By.id("js-link-box-" + langCode);
		driver.findElement(languageLink).click();
	}
	
	public void clickReadWikipediaDropdown() {
	    safeClick(readWikipediaDropdown);
	}
	
	public boolean isLanguageListVisible() {
		WebElement langList = wait.until(ExpectedConditions.visibilityOfElementLocated(languageListContainer));
	    return langList.isDisplayed();
	}
	
	
	public List<WebElement> getAllLanguagesFromDropdown() {
		Select dropdown = new Select (driver.findElement(searchLanguageDropdown));
		return dropdown.getOptions();
	}
	
	
	/*//Robust way to check if all languages are selectable across different browsers//
	public boolean areAllLanguagesSelectable() {
		WebElement selectElement = visible(searchLanguageDropdown);
		scrollIntoView(selectElement);
		
		Select select = new Select(selectElement);
		List<WebElement> options = select.getOptions();
		if (options.isEmpty()) return false;
		
		for (int i = 0; i < options.size(); i++) {
			WebElement option = options.get(i);
			if (option == null || !option.isDisplayed()) {
				continue;
			}
			
	
	 * // Try by index first try { select.selectByIndex(i); //Refetch and verify
	 * selection applied wait.until(d -> { Select s = new
	 * Select(d.findElement(searchLanguageDropdown)); return
	 * s.getFirstSelectedOption().getAttribute("value")
	 * .equals(option.getAttribute("value")); }); } catch (Exception first) { //
	 * Fallback: by value, then by clicking the option element try { String value =
	 * option.getAttribute("value"); if (value != null && !value.isEmpty()) {
	 * select.selectByValue(value); wait.until(d -> { Select s = new
	 * Select(d.findElement(searchLanguageDropdown)); return
	 * value.equals(s.getFirstSelectedOption().getAttribute("value")); }); } else {
	 * scrollIntoView(option); option.click(); wait.until(d -> { Select s = new
	 * Select(d.findElement(searchLanguageDropdown)); return
	 * s.getFirstSelectedOption().getText().equals(option.getText()); }); } } catch
	 * (Exception second) { // One option failed all strategies return false; } }
	 * 
	 * // Refresh references occasionally to avoid stale collections in FF try {
	 * select = new Select(driver.findElement(searchLanguageDropdown)); options =
	 * select.getOptions(); } catch (StaleElementReferenceException ignored) {
	 * select = new Select(visible(searchLanguageDropdown)); options =
	 * select.getOptions(); } } return true; }
	 */
	
	
	public boolean areAllLanguagesSelectable() {
		Select dropdown = new Select(driver.findElement(searchLanguageDropdown));
		List<WebElement> options = dropdown.getOptions();
		for (WebElement option : options) {
			try {
				dropdown.selectByVisibleText(option.getText());
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}
	
	
	// Lanugage Mapping to Language Code
	private String getLangCode(String language) {
		switch (language.toLowerCase()) {
		case "english": return "en";
		case "русский": return "ru";
        case "français": return "fr";
        case "deutsch": return "de";
        case "español": return "es";
        case "italiano": return "it";
        case "日本語": return "ja";
        case "中文": return "zh";
        case "português": return "pt";
        case "polski": return "pl";
        default: throw new IllegalArgumentException("Language not supported: " + language);
		}
	}
	
	
	
}
