package com.collectivehealth.benefitSearch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigurationManager - Singleton class for managing test configuration.
 * 
 * Implements Singleton Design Pattern for configuration management.
 * Provides centralized access to configuration properties.
 * 
 * Best Practices:
 * - Singleton Pattern for single instance
 * - Lazy initialization with thread safety
 * - Externalized configuration
 * - Default values fallback
 * - Type-safe getters
 * 
 * @author Collective Health QA Team
 * @version 2.0
 */
public class ConfigurationManager {
    
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationManager.class);
    private static final String CONFIG_FILE = "test-config.properties";
    
    // Singleton instance - thread-safe lazy initialization
    private static volatile ConfigurationManager instance;
    
    // Configuration properties
    private final Properties properties;
    
    /**
     * Private constructor for Singleton pattern.
     * Loads configuration from properties file.
     */
    private ConfigurationManager() {
        properties = new Properties();
        loadConfiguration();
    }
    
    /**
     * Get singleton instance of ConfigurationManager.
     * Thread-safe double-checked locking.
     * 
     * @return ConfigurationManager instance
     */
    public static ConfigurationManager getInstance() {
        if (instance == null) {
            synchronized (ConfigurationManager.class) {
                if (instance == null) {
                    instance = new ConfigurationManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * Load configuration from properties file.
     * Falls back to default values if file not found.
     */
    private void loadConfiguration() {
        logger.info("Loading configuration from: {}", CONFIG_FILE);
        
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {
            
            if (input == null) {
                logger.warn("Configuration file not found: {}. Using defaults.", CONFIG_FILE);
                setDefaultValues();
                return;
            }
            
            properties.load(input);
            logger.info("Configuration loaded successfully");
            logConfiguration();
            
        } catch (IOException e) {
            logger.error("Error loading configuration: {}", e.getMessage(), e);
            setDefaultValues();
        }
    }
    
    /**
     * Set default configuration values.
     */
    private void setDefaultValues() {
        logger.info("Setting default configuration values");
        
        properties.setProperty("base.url", "https://benefits.collectivehealth.com");
        properties.setProperty("browser", "chrome");
        properties.setProperty("implicit.wait", "10");
        properties.setProperty("explicit.wait", "30");
        properties.setProperty("page.load.timeout", "60");
        properties.setProperty("screenshot.on.failure", "true");
        properties.setProperty("log.level", "INFO");
        properties.setProperty("max.retry.count", "2");
        properties.setProperty("test.environment", "staging");
    }
    
    /**
     * Log loaded configuration (excluding sensitive data).
     */
    private void logConfiguration() {
        logger.debug("Configuration values:");
        logger.debug("  Base URL: {}", getBaseUrl());
        logger.debug("  Browser: {}", getBrowser());
        logger.debug("  Implicit Wait: {} seconds", getImplicitWait());
        logger.debug("  Explicit Wait: {} seconds", getExplicitWait());
        logger.debug("  Test Environment: {}", getTestEnvironment());
    }
    
    // ============================================
    // Typed Getters for Configuration Values
    // ============================================
    
    /**
     * Get base URL for application.
     * 
     * @return Base URL
     */
    public String getBaseUrl() {
        return getProperty("base.url", "https://benefits.collectivehealth.com");
    }
    
    /**
     * Get browser type.
     * 
     * @return Browser type (chrome, firefox, edge)
     */
    public String getBrowser() {
        return getProperty("browser", "chrome");
    }
    
    /**
     * Get implicit wait timeout.
     * 
     * @return Implicit wait in seconds
     */
    public int getImplicitWait() {
        return getIntProperty("implicit.wait", 10);
    }
    
    /**
     * Get explicit wait timeout.
     * 
     * @return Explicit wait in seconds
     */
    public int getExplicitWait() {
        return getIntProperty("explicit.wait", 30);
    }
    
    /**
     * Get page load timeout.
     * 
     * @return Page load timeout in seconds
     */
    public int getPageLoadTimeout() {
        return getIntProperty("page.load.timeout", 60);
    }
    
    /**
     * Check if screenshot should be taken on failure.
     * 
     * @return true if screenshot on failure enabled
     */
    public boolean isScreenshotOnFailure() {
        return getBooleanProperty("screenshot.on.failure", true);
    }
    
    /**
     * Get log level.
     * 
     * @return Log level (TRACE, DEBUG, INFO, WARN, ERROR)
     */
    public String getLogLevel() {
        return getProperty("log.level", "INFO");
    }
    
    /**
     * Get maximum retry count for flaky tests.
     * 
     * @return Maximum retry count
     */
    public int getMaxRetryCount() {
        return getIntProperty("max.retry.count", 2);
    }
    
    /**
     * Get retry delay in seconds.
     * 
     * @return Retry delay in seconds
     */
    public int getRetryDelay() {
        return getIntProperty("retry.delay.seconds", 5);
    }
    
    /**
     * Get test environment.
     * 
     * @return Test environment (local, dev, staging, production)
     */
    public String getTestEnvironment() {
        return getProperty("test.environment", "staging");
    }
    
    /**
     * Get report output directory.
     * 
     * @return Report output directory path
     */
    public String getReportOutputDirectory() {
        return getProperty("report.output.directory", "target/test-reports");
    }
    
    /**
     * Get default username.
     * 
     * @return Default username for tests
     */
    public String getDefaultUsername() {
        return getProperty("default.username", "testuser@collectivehealth.com");
    }
    
    /**
     * Get default password.
     * 
     * @return Default password for tests
     */
    public String getDefaultPassword() {
        return getProperty("default.password", "");
    }
    
    // ============================================
    // Generic Getters
    // ============================================
    
    /**
     * Get string property with default value.
     * 
     * @param key Property key
     * @param defaultValue Default value if key not found
     * @return Property value or default
     */
    public String getProperty(String key, String defaultValue) {
        String value = properties.getProperty(key, defaultValue);
        // Check system properties for override
        value = System.getProperty(key, value);
        return value;
    }
    
    /**
     * Get integer property with default value.
     * 
     * @param key Property key
     * @param defaultValue Default value if key not found or invalid
     * @return Property value as integer or default
     */
    public int getIntProperty(String key, int defaultValue) {
        try {
            String value = getProperty(key, String.valueOf(defaultValue));
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer value for key {}, using default: {}", 
                key, defaultValue);
            return defaultValue;
        }
    }
    
    /**
     * Get boolean property with default value.
     * 
     * @param key Property key
     * @param defaultValue Default value if key not found
     * @return Property value as boolean or default
     */
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key, String.valueOf(defaultValue));
        return Boolean.parseBoolean(value);
    }
    
    /**
     * Get double property with default value.
     * 
     * @param key Property key
     * @param defaultValue Default value if key not found or invalid
     * @return Property value as double or default
     */
    public double getDoubleProperty(String key, double defaultValue) {
        try {
            String value = getProperty(key, String.valueOf(defaultValue));
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            logger.warn("Invalid double value for key {}, using default: {}", 
                key, defaultValue);
            return defaultValue;
        }
    }
    
    /**
     * Set property value at runtime.
     * Useful for overriding configuration during test execution.
     * 
     * @param key Property key
     * @param value Property value
     */
    public void setProperty(String key, String value) {
        logger.debug("Setting property: {} = {}", key, value);
        properties.setProperty(key, value);
    }
    
    /**
     * Check if property exists.
     * 
     * @param key Property key
     * @return true if property exists, false otherwise
     */
    public boolean hasProperty(String key) {
        return properties.containsKey(key);
    }
}
