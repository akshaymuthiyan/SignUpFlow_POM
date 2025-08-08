package pageObjects;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MyAccountPage {

    WebDriver driver;
    WebDriverWait wait;
    Logger logger = LogManager.getLogger(MyAccountPage.class);  // Logging for debugging

    public MyAccountPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));  // Increased wait time
        PageFactory.initElements(driver, this);
    }


    @FindBy(xpath = "//*[contains(text(),'My Account') or contains(text(),'Welcome')]")
    WebElement msgHeading;


    @FindBy(xpath = "//a[contains(@href,'logout') and text()='Logout']")
    WebElement lnkLogout; 
    public boolean isMyAccountPageExists() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement heading = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(), 'My Account')]")));
            return heading.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickLogout() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(lnkLogout));  // Wait until element is clickable
            lnkLogout.click();
            logger.info("Clicked on Logout");
        } catch (Exception e) {
            logger.error("Logout action failed: " + e.getMessage());
        }
    }

    public void setImplicitWait() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }
}
