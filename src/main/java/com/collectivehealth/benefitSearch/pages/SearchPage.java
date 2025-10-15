package com.collectivehealth.benefitSearch.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * SearchPage - Page Object for Benefit Search page.
 * 
 * Encapsulates all elements and interactions on the search page.
 * Follows Page Object Model best practices:
 * - All locators defined as private fields
 * - Public methods for user actions
 * - Returns appropriate page objects for navigation
 * - Implements proper waits and error handling
 * 
 * @author Collective Health QA Team
 * @version 2.0
 */
public class SearchPage extends BasePage {
    
    // ============================================
    // Page Element Locators
    // ============================================
    
    // Search Input and Controls
    private final By searchInput = By.id("benefit-search-input");
    private final By searchButton = By.id("search-submit-btn");
    private final By clearButton = By.id("search-clear-btn");
    private final By searchSuggestionsDropdown = By.className("search-suggestions");
    
    // Filter Controls
    private final By filterPanel = By.id("filter-panel");
    private final By categoryFilter = By.name("category-filter");
    private final By providerFilter = By.name("provider-filter");
    private final By applyFiltersButton = By.id("apply-filters-btn");
    private final By clearFiltersButton = By.id("clear-filters-btn");
    
    // Error and Validation Messages
    private final By errorMessage = By.className("search-error-message");
    private final By validationMessage = By.className("validation-message");
    private final By noResultsMessage = By.className("no-results-message");
    
    // Loading Indicators
    private final By loadingSpinner = By.className("loading-spinner");
    
    // ============================================
    // Constructor
    // ============================================
    
    /**
     * Initialize SearchPage with WebDriver.
     * 
     * @param driver WebDriver instance
     */
    public SearchPage(WebDriver driver) {
        super(driver);
        logger.info("SearchPage initialized");
    }
    
    /**
     * Initialize SearchPage with custom timeout.
     * 
     * @param driver WebDriver instance
     * @param waitTimeout Custom wait timeout in seconds
     */
    public SearchPage(WebDriver driver, int waitTimeout) {
        super(driver, waitTimeout);
        logger.info("SearchPage initialized with custom timeout");
    }
    
    // ============================================
    // Page Actions
    // ============================================
    
    /**
     * Navigate to the search page.
     * 
     * @param baseUrl Base URL of the application
     * @return Current SearchPage instance for method chaining
     */
    public SearchPage navigateToSearchPage(String baseUrl) {
        String searchPageUrl = baseUrl + "/search";
        logger.info("Navigating to search page: {}", searchPageUrl);
        navigateTo(searchPageUrl);
        waitForPageLoad();
        return this;
    }
    
    /**
     * Enter search keyword in search input field.
     * 
     * @param keyword Search keyword to enter
     * @return Current SearchPage instance for method chaining
     */
    public SearchPage enterSearchKeyword(String keyword) {
        logger.info("Entering search keyword: {}", keyword);
        WebElement inputField = wait.until(ExpectedConditions.elementToBeClickable(searchInput));
        inputField.clear();
        inputField.sendKeys(keyword);
        logger.debug("Search keyword entered successfully");
        return this;
    }
    
    /**
     * Click the search button to execute search.
     * 
     * @return SearchResultsPage object
     */
    public SearchResultsPage clickSearchButton() {
        logger.info("Clicking search button");
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(searchButton));
        button.click();
        waitForSearchToComplete();
        logger.debug("Search executed successfully");
        return new SearchResultsPage(driver);
    }
    
    /**
     * Perform complete search operation.
     * Combines entering keyword and clicking search.
     * 
     * @param keyword Search keyword
     * @return SearchResultsPage object
     */
    public SearchResultsPage performSearch(String keyword) {
        logger.info("Performing search for: {}", keyword);
        enterSearchKeyword(keyword);
        return clickSearchButton();
    }
    
    /**
     * Perform search with filters applied.
     * 
     * @param keyword Search keyword
     * @param category Benefit category filter
     * @return SearchResultsPage object
     */
    public SearchResultsPage performSearchWithFilter(String keyword, String category) {
        logger.info("Performing filtered search - Keyword: {}, Category: {}", keyword, category);
        enterSearchKeyword(keyword);
        applyFilter(category);
        return clickSearchButton();
    }
    
    /**
     * Apply filter by category.
     * 
     * @param category Category to filter by
     * @return Current SearchPage instance for method chaining
     */
    public SearchPage applyFilter(String category) {
        logger.info("Applying filter - Category: {}", category);
        WebElement filter = wait.until(ExpectedConditions.elementToBeClickable(categoryFilter));
        
        // Select the category from dropdown
        org.openqa.selenium.support.ui.Select dropdown = 
            new org.openqa.selenium.support.ui.Select(filter);
        dropdown.selectByVisibleText(category);
        
        logger.debug("Filter applied successfully");
        return this;
    }
    
    /**
     * Clear search input field.
     * 
     * @return Current SearchPage instance for method chaining
     */
    public SearchPage clearSearch() {
        logger.info("Clearing search input");
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(clearButton));
        button.click();
        logger.debug("Search input cleared");
        return this;
    }
    
    /**
     * Clear all applied filters.
     * 
     * @return Current SearchPage instance for method chaining
     */
    public SearchPage clearFilters() {
        logger.info("Clearing all filters");
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(clearFiltersButton));
        button.click();
        logger.debug("Filters cleared");
        return this;
    }
    
    // ============================================
    // Validation Methods
    // ============================================
    
    /**
     * Check if search input field is displayed.
     * 
     * @return true if displayed, false otherwise
     */
    public boolean isSearchInputDisplayed() {
        try {
            boolean isDisplayed = driver.findElement(searchInput).isDisplayed();
            logger.debug("Search input displayed: {}", isDisplayed);
            return isDisplayed;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            logger.debug("Search input not found");
            return false;
        }
    }
    
    /**
     * Check if error message is displayed.
     * 
     * @return true if error message is displayed, false otherwise
     */
    public boolean isErrorMessageDisplayed() {
        try {
            boolean isDisplayed = driver.findElement(errorMessage).isDisplayed();
            logger.debug("Error message displayed: {}", isDisplayed);
            return isDisplayed;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            logger.debug("Error message not found");
            return false;
        }
    }
    
    /**
     * Get error message text.
     * 
     * @return Error message text, or empty string if not found
     */
    public String getErrorMessage() {
        try {
            String message = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage))
                .getText();
            logger.debug("Error message: {}", message);
            return message;
        } catch (Exception e) {
            logger.warn("Could not retrieve error message: {}", e.getMessage());
            return "";
        }
    }
    
    /**
     * Check if search suggestions are displayed.
     * 
     * @return true if suggestions dropdown is visible, false otherwise
     */
    public boolean areSearchSuggestionsDisplayed() {
        try {
            boolean isDisplayed = driver.findElement(searchSuggestionsDropdown).isDisplayed();
            logger.debug("Search suggestions displayed: {}", isDisplayed);
            return isDisplayed;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            logger.debug("Search suggestions not found");
            return false;
        }
    }
    
    /**
     * Get list of search suggestions.
     * 
     * @return List of suggestion texts
     */
    public List<String> getSearchSuggestions() {
        logger.debug("Retrieving search suggestions");
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchSuggestionsDropdown));
        List<WebElement> suggestions = driver.findElements(
            By.cssSelector(".search-suggestions .suggestion-item")
        );
        
        List<String> suggestionTexts = suggestions.stream()
            .map(WebElement::getText)
            .toList();
        
        logger.debug("Found {} suggestions", suggestionTexts.size());
        return suggestionTexts;
    }
    
    // ============================================
    // Helper Methods
    // ============================================
    
    /**
     * Wait for search operation to complete.
     * Waits for loading spinner to disappear.
     */
    private void waitForSearchToComplete() {
        logger.debug("Waiting for search to complete");
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));
            logger.debug("Search completed");
        } catch (Exception e) {
            logger.debug("No loading spinner found, assuming search completed immediately");
        }
    }
    
    /**
     * Verify search page is loaded.
     * 
     * @return true if page is loaded correctly, false otherwise
     */
    public boolean isPageReady() {
        boolean isReady = isSearchInputDisplayed();
        logger.debug("Search page ready: {}", isReady);
        return isReady;
    }
}
