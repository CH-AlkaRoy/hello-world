package com.collectivehealth.benefitSearch.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * WebDriverFactory - Factory class for creating WebDriver instances.
 * 
 * Implements Factory Design Pattern for WebDriver creation.
 * Provides centralized driver configuration and management.
 * 
 * Best Practices:
 * - Factory Pattern for object creation
 * - WebDriverManager for automatic driver management
 * - Configuration centralization
 * - Browser-specific options handling
 * 
 * @author Collective Health QA Team
 * @version 2.0
 */
public class WebDriverFactory {
    
    private static final Logger logger = LoggerFactory.getLogger(WebDriverFactory.class);
    
    // Default timeouts in seconds
    private static final int DEFAULT_IMPLICIT_WAIT = 10;
    private static final int DEFAULT_PAGE_LOAD_TIMEOUT = 60;
    private static final int DEFAULT_SCRIPT_TIMEOUT = 30;
    
    /**
     * Private constructor to prevent instantiation.
     * This is a utility class with static methods only.
     */
    private WebDriverFactory() {
        throw new IllegalStateException("Utility class - do not instantiate");
    }
    
    /**
     * Create WebDriver instance based on browser type.
     * 
     * @param browserType Type of browser (chrome, firefox, edge)
     * @return Configured WebDriver instance
     */
    public static WebDriver createDriver(String browserType) {
        logger.info("Creating WebDriver for browser: {}", browserType);
        
        WebDriver driver;
        
        switch (browserType.toLowerCase()) {
            case "chrome":
                driver = createChromeDriver();
                break;
            case "firefox":
                driver = createFirefoxDriver();
                break;
            case "edge":
                driver = createEdgeDriver();
                break;
            default:
                logger.warn("Unknown browser type: {}. Defaulting to Chrome.", browserType);
                driver = createChromeDriver();
        }
        
        configureDriver(driver);
        logger.info("WebDriver created and configured successfully");
        
        return driver;
    }
    
    /**
     * Create Chrome WebDriver with recommended options.
     * 
     * @return Configured ChromeDriver instance
     */
    private static WebDriver createChromeDriver() {
        logger.debug("Setting up Chrome WebDriver");
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        
        // Recommended Chrome options for test automation
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        
        // Enable headless mode if configured
        if (isHeadlessMode()) {
            logger.debug("Running Chrome in headless mode");
            options.addArguments("--headless=new");
        }
        
        return new ChromeDriver(options);
    }
    
    /**
     * Create Firefox WebDriver with recommended options.
     * 
     * @return Configured FirefoxDriver instance
     */
    private static WebDriver createFirefoxDriver() {
        logger.debug("Setting up Firefox WebDriver");
        WebDriverManager.firefoxdriver().setup();
        
        FirefoxOptions options = new FirefoxOptions();
        
        // Recommended Firefox options
        options.addArguments("--disable-extensions");
        
        if (isHeadlessMode()) {
            logger.debug("Running Firefox in headless mode");
            options.addArguments("--headless");
        }
        
        return new FirefoxDriver(options);
    }
    
    /**
     * Create Edge WebDriver with recommended options.
     * 
     * @return Configured EdgeDriver instance
     */
    private static WebDriver createEdgeDriver() {
        logger.debug("Setting up Edge WebDriver");
        WebDriverManager.edgedriver().setup();
        
        EdgeOptions options = new EdgeOptions();
        
        // Recommended Edge options
        options.addArguments("--disable-extensions");
        options.addArguments("--remote-allow-origins=*");
        
        if (isHeadlessMode()) {
            logger.debug("Running Edge in headless mode");
            options.addArguments("--headless=new");
        }
        
        return new EdgeDriver(options);
    }
    
    /**
     * Configure WebDriver with common settings.
     * Applies timeouts and window management.
     * 
     * @param driver WebDriver instance to configure
     */
    private static void configureDriver(WebDriver driver) {
        logger.debug("Configuring WebDriver timeouts and window size");
        
        // Set timeouts
        driver.manage().timeouts()
            .implicitlyWait(Duration.ofSeconds(DEFAULT_IMPLICIT_WAIT));
        driver.manage().timeouts()
            .pageLoadTimeout(Duration.ofSeconds(DEFAULT_PAGE_LOAD_TIMEOUT));
        driver.manage().timeouts()
            .scriptTimeout(Duration.ofSeconds(DEFAULT_SCRIPT_TIMEOUT));
        
        // Maximize window (unless headless)
        if (!isHeadlessMode()) {
            driver.manage().window().maximize();
            logger.debug("Browser window maximized");
        }
    }
    
    /**
     * Create WebDriver with custom timeouts.
     * 
     * @param browserType Browser type
     * @param implicitWait Implicit wait in seconds
     * @param pageLoadTimeout Page load timeout in seconds
     * @return Configured WebDriver instance
     */
    public static WebDriver createDriver(String browserType, int implicitWait, int pageLoadTimeout) {
        logger.info("Creating WebDriver with custom timeouts - Browser: {}, Implicit Wait: {}s, Page Load: {}s",
            browserType, implicitWait, pageLoadTimeout);
        
        WebDriver driver = createDriver(browserType);
        
        // Override with custom timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
        
        logger.debug("Custom timeouts applied");
        return driver;
    }
    
    /**
     * Check if headless mode is enabled.
     * Reads from system property or environment variable.
     * 
     * @return true if headless mode is enabled, false otherwise
     */
    private static boolean isHeadlessMode() {
        String headless = System.getProperty("headless", 
            System.getenv().getOrDefault("HEADLESS", "false"));
        return Boolean.parseBoolean(headless);
    }
    
    /**
     * Safely quit WebDriver instance.
     * Handles null checks and exceptions.
     * 
     * @param driver WebDriver instance to quit
     */
    public static void quitDriver(WebDriver driver) {
        if (driver != null) {
            try {
                logger.info("Quitting WebDriver");
                driver.quit();
                logger.debug("WebDriver quit successfully");
            } catch (Exception e) {
                logger.error("Error quitting WebDriver: {}", e.getMessage(), e);
            }
        } else {
            logger.debug("WebDriver is null, nothing to quit");
        }
    }
}
