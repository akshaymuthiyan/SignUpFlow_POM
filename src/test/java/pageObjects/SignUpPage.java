package pageObjects;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SignUpPage {
    public WebDriver driver;

    public SignUpPage(WebDriver driver) {
        this.driver = driver;
    }


    By lnkCreateAccount = By.linkText("Create an Account");
    By txtFirstName = By.id("firstname");
    By txtLastName = By.id("lastname");
    By txtEmail = By.id("email_address");
    By txtPassword = By.id("password");
    By txtConfirmPassword = By.id("password-confirmation");
    By btnCreateAccount = By.cssSelector("button[title='Create an Account']");
    By lnkWeclomeTestAdminDropdown = By.linkText("Welcome, Test Admin! ");
    By lnkSignOut = By.linkText("Sign Out");

    By errorMessages = By.cssSelector(".mage-error, .message-error");


    public void clickCreateAccountLink() {
        driver.findElement(lnkCreateAccount).click();
    }

    public void setFirstName(String fname) {
        driver.findElement(txtFirstName).sendKeys(fname);
    }

    public void setLastName(String lname) {
        driver.findElement(txtLastName).sendKeys(lname);
    }

    public void setEmail(String email) {
        driver.findElement(txtEmail).sendKeys(email);
    }

    public void setPassword(String pwd) {
        driver.findElement(txtPassword).sendKeys(pwd);
    }

    public void setConfirmPassword(String cpwd) {
        driver.findElement(txtConfirmPassword).sendKeys(cpwd);
    }

    public void clickCreateAccount() {
        driver.findElement(btnCreateAccount).click();
    }

    public void clickWelcomeDropdown() {
        driver.findElement(By.xpath("//span[contains(text(),'Welcome')]")).click();
    }
    
    public void clickSignOutLink() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

   
        WebElement welcomeDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Welcome')]")));
        welcomeDropdown.click();


        WebElement signOutLink = wait.until(ExpectedConditions.elementToBeClickable(lnkSignOut));
        signOutLink.click();
    }



    public void waitForSignupResult() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.or(
            ExpectedConditions.titleIs("My Account"),
            ExpectedConditions.presenceOfElementLocated(errorMessages)
        ));
    }

    
    public boolean isSignupSuccessful() {
        return driver.getTitle().equals("My Account");
    }

    
    public List<WebElement> getSignupErrors() {
        return driver.findElements(errorMessages);
    }
}
