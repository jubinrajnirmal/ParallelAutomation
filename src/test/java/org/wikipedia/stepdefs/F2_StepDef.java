package org.wikipedia.stepdefs;

import org.openqa.selenium.WebElement;
import org.wikipedia.data.DataFile;
import org.wikipedia.hooks.Hooks;

import static org.junit.Assert.assertTrue;

import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class F2_StepDef {
	private List<WebElement> languages;
	
	@Given("I am on the Wikipedia home page")
	public void i_am_on_the_wikipedia_home_page() {
		Hooks.getDriver().get(DataFile.homePageURL);
		assertTrue(Hooks.homePage().isWikiLogoDisplayed());
	}

	@When("I click on the {string} language link")
	public void i_click_on_the_language_link(String language) {
		Hooks.homePage().clickLanguageLink(language);
	}

	@Then("I should be navigated to the {string} page")
	public void i_should_be_navigated_to_the_page(String expectedURL) {
		String currentUrl = Hooks.getDriver().getCurrentUrl();
		assertTrue("Expected URL to contain:" + expectedURL + " but got: " + currentUrl,
				currentUrl.contains(expectedURL));
	}

	@When("I click on the {string} dropdown")
	public void i_click_on_the_dropdown(String dropDownBtn) {
		Hooks.homePage().clickReadWikipediaDropdown();
	}

	@Then("I should see the full list of available languages")
	public void i_should_see_the_full_list_of_available_languages() {
		assertTrue("Expected the language list to be visible", Hooks.homePage().isLanguageListVisible());
	}

	@When("I collect all the languages from the search dropdown")
	public void i_collect_all_the_languages_from_the_search_dropdown() {
		languages = Hooks.homePage().getAllLanguagesFromDropdown();
		assertTrue("Language dropdown should not be empty", languages.size() > 0);
	}

	@Then("each language option should be selectable")
	public void each_language_option_should_be_selectable() {
		assertTrue("Not all languages were selectable", Hooks.homePage().areAllLanguagesSelectable());
	}

}
