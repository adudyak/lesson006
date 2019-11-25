package home.aqacources.orangehrm;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class OrangeHrm {
    private WebDriver driver;
    private WebDriverWait wait;

    private final String URL = "https://s1.demo.opensourcecms.com/s/44";
    private final String FRAME_NAME = "preview-frame";
    private final String LOGIN_ID = "txtUsername";
    private final String LOGIN = "Username";
    private final String PASSWORD_ID = "txtPassword";
    private final String PASSWORD = "Temp1234%";
    private final String LOGIN_BUTTON_ID = "btnLogin";
    private final String MESSAGE_SPAN_ID = "spanMessage";
    private final String INVALID_CREDS_MESSAGE = "Invalid credentials";
    private final String EMPTY_CREDS_MESSAGE = "Username cannot be empty";
    private final String EMPTY_PASS_MESSAGE = "Password cannot be empty";
    private final String REMOVE_FRAME_XPATH = "//span[contains(text(), 'Remove Frame')]";

    /**
     * Setup webdriver and Chrome options
     */
    @Before
    public void setUp() {

        // Disable infobars please use this code
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);

        // Initialize path to ChromeDriver
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");

        // Initialize instance of ChromeDriver and add options
        driver = new ChromeDriver(options);

        // Set 10 second for implicitly wait
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        // Maximize window
        driver.manage().window().maximize();

        // Explicitly wait
        wait = new WebDriverWait(driver, 10);
        wait.ignoring(TimeoutException.class)
                .withMessage("Looking for an element")   // Print this message
                .withTimeout(Duration.ofSeconds(10))      // timeout
                .pollingEvery(Duration.ofSeconds(1));     // Tries each second
    }

    /**
     * Test login form in frame
     */
    @Test
    public void testLoginTest() {
        // Open site
        driver.get(URL);

        // Wait for awesome frame
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(FRAME_NAME));

        // Input random login and password
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id(LOGIN_ID)))).sendKeys(LOGIN);
        driver.findElement(By.id(PASSWORD_ID)).sendKeys(PASSWORD);

        // Click login
        driver.findElement(By.id(LOGIN_BUTTON_ID)).click();

        // Verify error message for invalid login
        Assert.assertEquals("Error message for invalid credentials is not as expected", INVALID_CREDS_MESSAGE, driver.findElement(By.id(MESSAGE_SPAN_ID)).getText());

        // Click login
        driver.findElement(By.id(LOGIN_BUTTON_ID)).click();

        // Verify error message for empty inputs
        Assert.assertEquals("Error message for empty credentials is not as expected", EMPTY_CREDS_MESSAGE, driver.findElement(By.id(MESSAGE_SPAN_ID)).getText());

        // Input login only
        driver.findElement(By.id(LOGIN_ID)).sendKeys(LOGIN);

        // Click login
        driver.findElement(By.id(LOGIN_BUTTON_ID)).click();

        // Verify error message for empty password
        Assert.assertEquals("Error message for empty password is not as expected", EMPTY_PASS_MESSAGE, driver.findElement(By.id(MESSAGE_SPAN_ID)).getText());

        // Close frame
        driver.switchTo().defaultContent();
        driver.findElement(By.xpath(REMOVE_FRAME_XPATH)).click();

        // Verify frame is closed
        ExpectedConditions.not(ExpectedConditions.frameToBeAvailableAndSwitchToIt(FRAME_NAME));
    }

    /**
     * After method, quit driver
     */
    @After
    public void tearDown() {
        // Close all window an driver
        driver.quit();
    }
}
