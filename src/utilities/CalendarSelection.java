import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CalendarSelection {

    public static void main(String[] args) {
        // Set up WebDriver (Update the path)
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        WebDriver driver = new ChromeDriver();

        // Open the calendar page
        driver.get("YOUR_CALENDAR_PAGE_URL");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        // Get the target date (2 days before today)
        String previousDateFormatted = getPreviousDate(2);
        LocalDate targetDate = LocalDate.now().minusDays(2); // Two days before today
        
        // Extract Month and Year separately
        int targetMonth = targetDate.getMonthValue(); // (1 = Jan, 12 = Dec)
        int targetYear = targetDate.getYear();

        // Print for debugging
        System.out.println("Target Date: " + previousDateFormatted);
        System.out.println("Target Month: " + targetMonth);
        System.out.println("Target Year: " + targetYear);

        // Get the displayed month & year from the calendar (Modify XPath as per your application)
        WebElement calendarHeader = driver.findElement(By.xpath("//div[@class='calendar-header']"));
        String displayedMonthYear = calendarHeader.getText(); // Example: "April 2025"

        // Convert displayed month to YearMonth format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH);
        YearMonth displayedYearMonth = YearMonth.parse(displayedMonthYear, formatter);

        // Extract displayed month and year
        int displayedMonth = displayedYearMonth.getMonthValue();
        int displayedYear = displayedYearMonth.getYear();

        // If displayed month & year are different, navigate back
        if (displayedMonth != targetMonth || displayedYear != targetYear) {
            WebElement prevMonthButton = driver.findElement(By.xpath("//button[contains(@aria-label, 'Previous month')]"));
            prevMonthButton.click();
        }

        // Select the target day
        String targetDay = String.valueOf(targetDate.getDayOfMonth());
        WebElement dayElement = driver.findElement(By.xpath("//td[normalize-space()='" + targetDay + "']"));
        dayElement.click();

        System.out.println("Successfully selected: " + previousDateFormatted);

        // Close the browser
        driver.quit();
    }

    // Utility method to get a formatted date (e.g., "March 30, 2025")
    public static String getPreviousDate(int i) {
        LocalDate previousDate = LocalDate.now().minusDays(i);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
        return previousDate.format(formatter);
    }
}
