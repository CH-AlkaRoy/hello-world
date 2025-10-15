package com.collectivehealth.benefitSearch.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * SearchResultsPage - Page Object for Search Results page.
 * 
 * Handles all interactions with search results including:
 * - Result validation and verification
 * - Pagination controls
 * - Sorting options
 * - Result filtering
 * 
 * @author Collective Health QA Team
 * @version 2.0
 */
public class SearchResultsPage extends BasePage {
    
    // ============================================
    // Page Element Locators
    // ============================================
    
    // Results Display
    private final By resultsContainer = By.id("search-results-container");
    private final By resultItems = By.className("result-item");
    private final By resultTitle = By.className("result-title");
    private final By resultDescription = By.className("result-description");
    private final By resultCategory = By.className("result-category");
    private final By totalResultsCount = By.id("total-results-count");
    private final By noResultsMessage = By.className("no-results-message");
    
    // Pagination Controls
    private final By paginationContainer = By.className("pagination");
    private final By nextPageButton = By.id("next-page-btn");
    private final By previousPageButton = By.id("previous-page-btn");
    private final By currentPageNumber = By.className("current-page");
    private final By pageNumberLinks = By.className("page-number");
    
    // Sorting Controls
    private final By sortDropdown = By.id("sort-dropdown");
    private final By sortByRelevance = By.cssSelector("option[value='relevance']");
    private final By sortByDate = By.cssSelector("option[value='date']");
    private final By sortByName = By.cssSelector("option[value='name']");
    
    // Loading Indicators
    private final By loadingOverlay = By.className("loading-overlay");
    
    // ============================================
    // Constructor
    // ============================================
    
    /**
     * Initialize SearchResultsPage.
     * 
     * @param driver WebDriver instance
     */
    public SearchResultsPage(WebDriver driver) {
        super(driver);
        logger.info("SearchResultsPage initialized");
        waitForResultsToLoad();
    }
    
    // ============================================
    // Results Verification Methods
    // ============================================
    
    /**
     * Check if search results are displayed.
     * 
     * @return true if results container is visible, false otherwise
     */
    public boolean isResultsDisplayed() {
        try {
            boolean isDisplayed = wait.until(
                ExpectedConditions.visibilityOfElementLocated(resultsContainer)
            ).isDisplayed();
            logger.debug("Search results displayed: {}", isDisplayed);
            return isDisplayed;
        } catch (Exception e) {
            logger.warn("Results container not found: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if search returned any results.
     * 
     * @return true if results exist, false otherwise
     */
    public boolean hasResults() {
        List<WebElement> results = driver.findElements(resultItems);
        boolean hasResults = !results.isEmpty();
        logger.debug("Has results: {} (count: {})", hasResults, results.size());
        return hasResults;
    }
    
    /**
     * Get count of results on current page.
     * 
     * @return Number of result items displayed
     */
    public int getResultsCount() {
        List<WebElement> results = driver.findElements(resultItems);
        int count = results.size();
        logger.debug("Results count on current page: {}", count);
        return count;
    }
    
    /**
     * Get total results count across all pages.
     * 
     * @return Total number of results found
     */
    public int getTotalResultsCount() {
        try {
            String countText = driver.findElement(totalResultsCount).getText();
            // Extract number from text like "Showing 150 results"
            String numberOnly = countText.replaceAll("[^0-9]", "");
            int count = Integer.parseInt(numberOnly);
            logger.debug("Total results count: {}", count);
            return count;
        } catch (Exception e) {
            logger.warn("Could not retrieve total results count: {}", e.getMessage());
            return 0;
        }
    }
    
    /**
     * Get all result titles on current page.
     * 
     * @return List of result title texts
     */
    public List<String> getResultTitles() {
        logger.debug("Retrieving result titles");
        List<WebElement> titles = driver.findElements(resultTitle);
        List<String> titleTexts = titles.stream()
            .map(WebElement::getText)
            .toList();
        logger.debug("Found {} result titles", titleTexts.size());
        return titleTexts;
    }
    
    /**
     * Check if all results match specific category.
     * 
     * @param expectedCategory Category to verify
     * @return true if all results match category, false otherwise
     */
    public boolean areAllResultsOfType(String expectedCategory) {
        logger.debug("Verifying all results are of type: {}", expectedCategory);
        List<WebElement> categories = driver.findElements(resultCategory);
        
        boolean allMatch = categories.stream()
            .map(WebElement::getText)
            .allMatch(cat -> cat.equalsIgnoreCase(expectedCategory));
        
        logger.debug("All results match category {}: {}", expectedCategory, allMatch);
        return allMatch;
    }
    
    /**
     * Check if results contain specific category.
     * 
     * @param category Category to look for
     * @return true if category found in results, false otherwise
     */
    public boolean containsCategory(String category) {
        logger.debug("Checking if results contain category: {}", category);
        List<WebElement> categories = driver.findElements(resultCategory);
        
        boolean contains = categories.stream()
            .map(WebElement::getText)
            .anyMatch(cat -> cat.equalsIgnoreCase(category));
        
        logger.debug("Results contain category {}: {}", category, contains);
        return contains;
    }
    
    // ============================================
    // Pagination Methods
    // ============================================
    
    /**
     * Check if pagination is displayed.
     * 
     * @return true if pagination controls are visible, false otherwise
     */
    public boolean isPaginationDisplayed() {
        try {
            boolean isDisplayed = driver.findElement(paginationContainer).isDisplayed();
            logger.debug("Pagination displayed: {}", isDisplayed);
            return isDisplayed;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            logger.debug("Pagination not found");
            return false;
        }
    }
    
    /**
     * Navigate to next page of results.
     * 
     * @return Current SearchResultsPage instance
     */
    public SearchResultsPage goToNextPage() {
        logger.info("Navigating to next page");
        WebElement nextButton = wait.until(
            ExpectedConditions.elementToBeClickable(nextPageButton)
        );
        nextButton.click();
        waitForResultsToLoad();
        logger.debug("Navigated to next page successfully");
        return this;
    }
    
    /**
     * Navigate to previous page of results.
     * 
     * @return Current SearchResultsPage instance
     */
    public SearchResultsPage goToPreviousPage() {
        logger.info("Navigating to previous page");
        WebElement prevButton = wait.until(
            ExpectedConditions.elementToBeClickable(previousPageButton)
        );
        prevButton.click();
        waitForResultsToLoad();
        logger.debug("Navigated to previous page successfully");
        return this;
    }
    
    /**
     * Navigate to specific page number.
     * 
     * @param pageNumber Page number to navigate to
     * @return Current SearchResultsPage instance
     */
    public SearchResultsPage goToPage(int pageNumber) {
        logger.info("Navigating to page: {}", pageNumber);
        List<WebElement> pageLinks = driver.findElements(pageNumberLinks);
        
        for (WebElement link : pageLinks) {
            if (link.getText().equals(String.valueOf(pageNumber))) {
                link.click();
                waitForResultsToLoad();
                logger.debug("Navigated to page {} successfully", pageNumber);
                return this;
            }
        }
        
        logger.warn("Page number {} not found in pagination", pageNumber);
        return this;
    }
    
    /**
     * Get current page number.
     * 
     * @return Current page number
     */
    public int getCurrentPageNumber() {
        try {
            String pageText = driver.findElement(currentPageNumber).getText();
            int pageNum = Integer.parseInt(pageText);
            logger.debug("Current page number: {}", pageNum);
            return pageNum;
        } catch (Exception e) {
            logger.warn("Could not determine current page number: {}", e.getMessage());
            return 1;
        }
    }
    
    /**
     * Check if currently on specific page.
     * 
     * @param expectedPage Expected page number
     * @return true if on expected page, false otherwise
     */
    public boolean isOnPage(int expectedPage) {
        int currentPage = getCurrentPageNumber();
        boolean isOnPage = currentPage == expectedPage;
        logger.debug("On page {} (expected {}): {}", currentPage, expectedPage, isOnPage);
        return isOnPage;
    }
    
    // ============================================
    // Sorting Methods
    // ============================================
    
    /**
     * Sort results by specified criteria.
     * 
     * @param sortBy Sort criteria (relevance, date, name)
     * @return Current SearchResultsPage instance
     */
    public SearchResultsPage sortBy(String sortBy) {
        logger.info("Sorting results by: {}", sortBy);
        WebElement dropdown = wait.until(
            ExpectedConditions.elementToBeClickable(sortDropdown)
        );
        
        org.openqa.selenium.support.ui.Select select = 
            new org.openqa.selenium.support.ui.Select(dropdown);
        select.selectByValue(sortBy.toLowerCase());
        
        waitForResultsToLoad();
        logger.debug("Results sorted by: {}", sortBy);
        return this;
    }
    
    /**
     * Verify results are sorted by specific criteria.
     * 
     * @param expectedSort Expected sort criteria
     * @return true if sorted by expected criteria, false otherwise
     */
    public boolean isSortedBy(String expectedSort) {
        try {
            WebElement dropdown = driver.findElement(sortDropdown);
            org.openqa.selenium.support.ui.Select select = 
                new org.openqa.selenium.support.ui.Select(dropdown);
            
            String selectedOption = select.getFirstSelectedOption().getAttribute("value");
            boolean isSorted = selectedOption.equalsIgnoreCase(expectedSort);
            
            logger.debug("Sorted by {} (expected {}): {}", 
                selectedOption, expectedSort, isSorted);
            return isSorted;
        } catch (Exception e) {
            logger.warn("Could not verify sort order: {}", e.getMessage());
            return false;
        }
    }
    
    // ============================================
    // Helper Methods
    // ============================================
    
    /**
     * Wait for results to load completely.
     * Waits for loading overlay to disappear.
     */
    private void waitForResultsToLoad() {
        logger.debug("Waiting for results to load");
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingOverlay));
            logger.debug("Results loaded successfully");
        } catch (Exception e) {
            logger.debug("No loading overlay found, assuming results loaded immediately");
        }
    }
    
    /**
     * Check if "no results" message is displayed.
     * 
     * @return true if no results message shown, false otherwise
     */
    public boolean isNoResultsMessageDisplayed() {
        try {
            boolean isDisplayed = driver.findElement(noResultsMessage).isDisplayed();
            logger.debug("No results message displayed: {}", isDisplayed);
            return isDisplayed;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            logger.debug("No results message not found");
            return false;
        }
    }
    
    /**
     * Click on first result in the list.
     * 
     * @return BenefitDetailPage object (to be implemented)
     */
    public void clickFirstResult() {
        logger.info("Clicking on first result");
        List<WebElement> results = driver.findElements(resultItems);
        
        if (!results.isEmpty()) {
            results.get(0).click();
            logger.debug("Clicked on first result successfully");
        } else {
            logger.warn("No results available to click");
        }
    }
}
