#LOGIN TEST FEATURE FILE
#AUTHOR: JUBIN 
#DATE: 04/09/2025

@Login
Feature: User Login on Wikipedia
	As a Wikipedia user
	I want to login in to my account
	So that I can access personalized features and contribute
	
	@Navigation @Login
	Scenario: Navigate to the login page from the main page 
		Given I am on the Wikipedia main page
	    When I click the Login link in the header
	    Then I should be on the Wikipedia Login page
	    And I should see the Username field
	    And I should see the Password field
	    And I should see the Login button
	    
	    
	@ValidLogin @Login	    
	Scenario: Successful login with valid credentials
		Given I am on the Wikipedia Login page 
	    When I enter the valid username
	    And I enter the valid password
	    And I click the Login button
	    Then I should be logged in successfully
	    
	    
	@Checkbox @Login    
	Scenario: "Keep me logged in" checkbox functionality
	    Given I am on the Wikipedia Login page 
	    When I check the "Keep me logged in" checkbox
	    Then the "Keep me logged in" checkbox should be selected
	    And When I uncheck the "Keep me logged in" checkbox
	    Then the "Keep me logged in" checkbox should not be selected


    @InvalidLogin @Login
	Scenario Outline: Unsuccessful login attempts with invalid credentials
	    Given I am on the Wikipedia Login page 
	    When I enter "<username>" in the Username field
	    And I enter "<password>" in the Password field
	    And I click the Login button
	    Then I should see an error message "<expected_error_message>"
	    And I should still be on the Wikipedia Login page
	
	    Examples:
	      | username            | password          | expected_error_message                                    |
	      | SeleniumT3stUser    | Inc0rrectPass     | Incorrect username or password entered. Please try again. |
	      | N0nExistentUser123  | AnyP4ssw0rd       | Incorrect username or password entered. Please try again. |
	     
	
	 @Validation @Login
	 Scenario Outline: Unsuccessful login attempts with empty fields for HTML5 validation erros
	    Given I am on the Wikipedia Login page 
	    When I leave the "<field_to_leave_empty>" field empty
	    And I enter "<other_field_value>" in the "<other_field_name>" field
	    And I click the Login button
	    Then I should see a browser validation message for the "<field_to_leave_empty>" field
	    And I should still be on the Wikipedia Login page
	
	    Examples:
	      | field_to_leave_empty | other_field_value | other_field_name |
	      | Username             | AnyPassword       | Password         |
	      | Password             | SeleniumTestUser  | Username         |



	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    