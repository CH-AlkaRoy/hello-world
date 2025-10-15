# Benefit Search Test Automation

A comprehensive, best-practice test automation framework for testing Collective Health's Benefit Search functionality.

## 🎯 Overview

This test suite implements industry-standard best practices including:

- **Page Object Model (POM)** design pattern for maintainability
- **Data-Driven Testing** using TestNG DataProviders
- **Comprehensive Logging** with SLF4J and Logback
- **Configuration Management** through external properties files
- **Clean Code Principles** (SOLID, DRY, KISS)
- **Test Lifecycle Management** with proper setup and teardown
- **Parallel Test Execution** for faster feedback
- **Detailed Reporting** with ExtentReports integration

## 📋 Prerequisites

- Java 11 or higher
- Maven 3.6+
- Chrome/Firefox/Edge browser installed
- Git

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/CH-AlkaRoy/hello-world.git
cd hello-world
```

### 2. Install Dependencies

```bash
mvn clean install -DskipTests
```

### 3. Configure Test Environment

Edit `src/test/resources/test-config.properties` to match your environment:

```properties
base.url=https://benefits.collectivehealth.com
browser=chrome
implicit.wait=10
explicit.wait=30
```

## 🧪 Running Tests

### Run All Tests

```bash
mvn clean test
```

### Run Smoke Tests Only

```bash
mvn clean test -Psmoke
```

### Run Regression Tests

```bash
mvn clean test -Pregression
```

### Run Specific Test Suite

```bash
mvn clean test -Dtest=BenefitSearchTestSuite
```

### Run with Custom TestNG XML

```bash
mvn clean test -DsuiteXmlFile=src/test/resources/testng.xml
```

## 📁 Project Structure

```
benefit-search-test-automation/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── collectivehealth/
│   │               └── benefitSearch/
│   │                   ├── pages/           # Page Object classes
│   │                   ├── utils/           # Utility classes
│   │                   └── config/          # Configuration classes
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── collectivehealth/
│       │           └── benefitSearch/
│       │               └── testplan/
│       │                   └── BenefitSearchTestSuite.java
│       └── resources/
│           ├── test-config.properties       # Test configuration
│           ├── testng.xml                   # TestNG suite configuration
│           └── logback.xml                  # Logging configuration
├── pom.xml                                  # Maven dependencies
└── README.md
```

## 🏗️ Architecture & Best Practices

### 1. Page Object Model (POM)

The framework follows the Page Object Model pattern to:
- Separate test logic from page-specific code
- Improve code reusability and maintainability
- Reduce code duplication

### 2. Test Organization

Tests are organized using TestNG annotations:
- `@BeforeSuite` / `@AfterSuite` - Suite-level setup/cleanup
- `@BeforeTest` / `@AfterTest` - Test-level setup/cleanup
- `@BeforeMethod` / `@AfterMethod` - Method-level setup/cleanup
- `@Test` - Individual test methods with priorities and groups

### 3. Configuration Management

- External configuration via `test-config.properties`
- Environment-specific settings
- Easy switching between environments (dev, staging, production)

### 4. Logging Strategy

- SLF4J API with Logback implementation
- Multiple log levels (TRACE, DEBUG, INFO, WARN, ERROR)
- Console and file logging
- Detailed test execution logs

### 5. Data-Driven Testing

- TestNG DataProviders for parameterized tests
- Separation of test data from test logic
- Easy addition of new test scenarios

### 6. Test Groups

Tests are categorized into groups:
- **smoke** - Critical path tests
- **regression** - Comprehensive test coverage
- **negative-testing** - Error handling validation
- **pagination** - Pagination functionality
- **sorting** - Sorting functionality
- **data-driven** - Parameterized tests

### 7. Error Handling

- Comprehensive exception handling
- Detailed error logging
- Screenshot capture on test failure
- Retry mechanism for flaky tests

## 📊 Reporting

Test execution generates:
- TestNG HTML reports in `target/surefire-reports/`
- ExtentReports in `target/test-reports/`
- Log files in `target/test-logs/`

## 🔧 Customization

### Adding New Tests

1. Create test method in `BenefitSearchTestSuite.java`
2. Add appropriate annotations (`@Test`, priority, groups)
3. Follow AAA pattern (Arrange, Act, Assert)
4. Add comprehensive logging

Example:
```java
@Test(
    priority = 7,
    description = "Test description here",
    groups = {"regression", "your-group"}
)
public void testYourFeature() {
    logger.info("Executing: Your Feature Test");
    
    try {
        // Arrange: Setup test data
        
        // Act: Perform actions
        
        // Assert: Verify results
        
        logger.info("Your Feature Test completed successfully");
    } catch (Exception e) {
        logger.error("Test failed: {}", e.getMessage(), e);
        throw e;
    }
}
```

### Adding Page Objects

Create page object classes in `src/main/java/com/collectivehealth/benefitSearch/pages/`:

```java
public class SearchPage extends BasePage {
    // Locators
    private By searchInput = By.id("search-input");
    private By searchButton = By.id("search-button");
    
    // Methods
    public void performSearch(String keyword) {
        driver.findElement(searchInput).sendKeys(keyword);
        driver.findElement(searchButton).click();
    }
}
```

## 🤝 Contributing

1. Follow the existing code structure and naming conventions
2. Write clean, self-documenting code
3. Add comprehensive comments for complex logic
4. Ensure all tests pass before committing
5. Update documentation as needed

## 📝 Code Standards

- **Naming Conventions**:
  - Classes: PascalCase (e.g., `BenefitSearchTestSuite`)
  - Methods: camelCase (e.g., `testBasicBenefitSearch`)
  - Constants: UPPER_SNAKE_CASE (e.g., `CONFIG_FILE`)
  
- **Comments**:
  - Javadoc for all public methods
  - Inline comments for complex logic
  - Clear test descriptions

- **Code Quality**:
  - Single Responsibility Principle
  - Don't Repeat Yourself (DRY)
  - Keep It Simple, Stupid (KISS)
  - You Aren't Gonna Need It (YAGNI)

## 🐛 Troubleshooting

### Common Issues

1. **WebDriver not found**
   - Solution: WebDriverManager handles this automatically
   
2. **Configuration file not found**
   - Solution: Ensure `test-config.properties` exists in `src/test/resources/`
   
3. **Tests failing due to timeouts**
   - Solution: Increase wait times in `test-config.properties`

## 📜 License

Copyright © 2025 Collective Health

## 👥 Authors

- Collective Health QA Team

## 📞 Support

For questions or issues, please contact the QA team or create an issue in the repository.

---

**Happy Testing! 🎉**
