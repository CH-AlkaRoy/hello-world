package com.collectivehealth.benefitSearch.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * BasePage - Parent class for all Page Object classes.
 * 
 * Implements common functionality shared across all pages:
 * - WebDriver management
 * - Wait utilities
 * - Common page interactions
 * - Logging
 * 
 * Best Practices:
 * - DRY (Don't Repeat Yourself) - Common code in one place
 * - Encapsulation - Protected access for subclasses
 * - Single Responsibility - Handles common page operations
 * 
 * @author Collective Health QA Team
 * @version 2.0
 */
public abstract class BasePage {
    
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected static final Logger logger = LoggerFactory.getLogger(BasePage.class);
    
    // Default wait timeout in seconds
    private static final int DEFAULT_WAIT_TIMEOUT = 30;
    
    /**
     * Constructor initializes WebDriver and WebDriverWait.
     * 
     * @param driver WebDriver instance
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_TIMEOUT));
        logger.debug("Initialized {} page object", this.getClass().getSimpleName());
    }
    
    /**
     * Constructor with custom wait timeout.
     * 
     * @param driver WebDriver instance
     * @param waitTimeout Custom wait timeout in seconds
     */
    public BasePage(WebDriver driver, int waitTimeout) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(waitTimeout));
        logger.debug("Initialized {} page object with custom timeout: {} seconds", 
            this.getClass().getSimpleName(), waitTimeout);
    }
    
    /**
     * Get current page title.
     * 
     * @return Page title as String
     */
    public String getPageTitle() {
        String title = driver.getTitle();
        logger.debug("Current page title: {}", title);
        return title;
    }
    
    /**
     * Get current page URL.
     * 
     * @return Current URL as String
     */
    public String getCurrentUrl() {
        String url = driver.getCurrentUrl();
        logger.debug("Current URL: {}", url);
        return url;
    }
    
    /**
     * Navigate to specific URL.
     * 
     * @param url URL to navigate to
     */
    public void navigateTo(String url) {
        logger.info("Navigating to URL: {}", url);
        driver.get(url);
    }
    
    /**
     * Refresh current page.
     */
    public void refreshPage() {
        logger.debug("Refreshing current page");
        driver.navigate().refresh();
    }
    
    /**
     * Navigate back to previous page.
     */
    public void navigateBack() {
        logger.debug("Navigating back");
        driver.navigate().back();
    }
    
    /**
     * Navigate forward.
     */
    public void navigateForward() {
        logger.debug("Navigating forward");
        driver.navigate().forward();
    }
    
    /**
     * Check if page is loaded by verifying title.
     * 
     * @param expectedTitle Expected page title
     * @return true if title matches, false otherwise
     */
    public boolean isPageLoaded(String expectedTitle) {
        String actualTitle = getPageTitle();
        boolean isLoaded = actualTitle.equals(expectedTitle);
        logger.debug("Page loaded check - Expected: {}, Actual: {}, Result: {}", 
            expectedTitle, actualTitle, isLoaded);
        return isLoaded;
    }
    
    /**
     * Wait for page to load completely.
     * Uses JavaScript to check document ready state.
     */
    protected void waitForPageLoad() {
        logger.debug("Waiting for page to load completely");
        wait.until(driver -> 
            ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return document.readyState").equals("complete"));
        logger.debug("Page loaded successfully");
    }
}
