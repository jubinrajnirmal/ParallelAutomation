#LOGIN TEST FEATURE FILE
#AUTHOR: JUBIN 
#DATE: 07/09/2025

@homepage
Feature: Wikipedia Home Page Languages and Dropdowns
	As a user of Wikipedia
  	I want to verify the homepage language links and dropdowns
  	So that I can confirm they navigate to the correct localized pages
  
  	Background: 
    	Given I am on the Wikipedia home page
    	
    @smoke @languages
    Scenario Outline: Verify language links on the Wikipedia home page
        When I click on the "<language>" language link
    	Then I should be navigated to the "<languagePage>" page
    	
    	Examples: # fromExcel: TestData/LoginData.xlsx|HomeLanguages
	    | language | languagePage |
    	# rows injected by generator
	      

	@regression @dropdown    
	Scenario: Verify "Read Wikipedia in your language" dropdown
   		When I click on the "Read Wikipedia in your language" dropdown
    	Then I should see the full list of available languages
    	
    	
    @regression @dropdown @full	
	Scenario: Verify all available languages in the search dropdown
  		When I collect all the languages from the search dropdown
  		Then each language option should be selectable	