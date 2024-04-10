package am.aua;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class AppTest {
    private static final String URL = "https://opensource-demo.orangehrmlive.com";
    private static final String ATTR_VALUE = "value";

    // Menu title -> Page title
    private static final Map<String, String> menuAndPageTitles = Map.ofEntries(
      Map.entry("Admin", "Admin\nUser Management"),
      Map.entry("PIM", "PIM"),
      Map.entry("Leave", "Leave"),
      Map.entry("Time", "Time\nTimesheets"),
      Map.entry("Recruitment", "Recruitment"),
      Map.entry("My Info", "PIM"),
      Map.entry("Performance", "Performance\nManage Reviews"),
      Map.entry("Dashboard", "Dashboard"),
      Map.entry("Directory", "Directory"),
      Map.entry("Claim", "Claim"),
      Map.entry("Buzz", "Buzz")
    );

    private static WebDriver driver;
    private static WebDriverWait wait;

    @BeforeAll
    static void beforeAll() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @AfterAll
    static void afterAll() {
        driver.quit();
    }

    @BeforeEach
    void setUp() {
        driver.get(URL);
    }

    @Test
    void commandsSelectorsAssertions() {
        // COMMANDS
        /* Toggle fullscreen */
        driver.manage().window().fullscreen();
        driver.manage().window().fullscreen();

        /* Setting window size */
        driver.manage().window().setSize(new Dimension(1600, 900));

        /* Setting window position*/
        driver.manage().window().setPosition(new Point(0, 0));

        /* Maximize window */
        driver.manage().window().maximize();

        // SELECTORS and ASSERTIONS
        wait.until(visibilityOfElementLocated(By.cssSelector("input[name=\"username\"]")));
        driver.findElement(By.name("username")).sendKeys("Admin");
        assertEquals("Admin", driver.findElement(By.cssSelector("input[name=\"username\"]")).getAttribute(ATTR_VALUE));

        driver.findElement(By.cssSelector("input[name=\"password\"]")).sendKeys("admin123");
        assertEquals("admin123", driver.findElement(By.cssSelector("input[name=\"password\"]")).getAttribute(ATTR_VALUE));

        driver.findElement(By.tagName("button")).click();
        wait.until(visibilityOfElementLocated(By.className("oxd-topbar-header-title")));

        assertEquals("Dashboard", driver.findElement(By.xpath("/html/body/div/div[1]/div[1]/header/div[1]/div[1]/span/h6")).getText());
        assertEquals("Dashboard", driver.findElement(By.cssSelector(".oxd-main-menu-item.active")).getText());

        int menuItemCount = driver.findElements(By.className("oxd-main-menu-item")).size();
        for (int i = 0; i < menuItemCount; i++) {
            WebElement menuItemElement = driver.findElements(By.className("oxd-main-menu-item")).get(i);
            String menuTitle = menuItemElement.getText();

            if (menuTitle.equals("Maintenance")) { // Skipping this as this is page requires admin privileges
                continue;
            }
            menuItemElement.click();
            wait.until(visibilityOfElementLocated(By.className("oxd-topbar-header-title")));
            WebElement pageTitleElement = driver.findElement(By.className("oxd-topbar-header-title"));
            assertTrue(pageTitleElement.isDisplayed());

            String expectedPageTitle = menuAndPageTitles.get(menuTitle);
            assertEquals(expectedPageTitle, pageTitleElement.getText());
        }
    }
}
