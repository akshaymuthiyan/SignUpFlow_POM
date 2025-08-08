package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import TestBase.BaseClass;
import pageObjects.SignUpPage;
import pageObjects.MyAccountPage;
import utilities.DataProviders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class TC_SignUpData extends BaseClass {

    private static final Logger logger = LogManager.getLogger(TC_SignUpData.class);

    @Test(dataProvider = "LoginData", dataProviderClass = DataProviders.class, groups = "Datadriven")
    public void verify_signupDDT(String firstName, String lastName, String email, String password, String confirmPassword) {

        logger.info(" Starting TC_SignUpData test for: " + email);

        try {
            // Step 1: Generate unique email to avoid duplicates
            String uniqueEmail = email;
            if (email.contains("@")) {
                String[] parts = email.split("@");
                uniqueEmail = parts[0] + "+" + System.currentTimeMillis() + "@" + parts[1];
                logger.info("Using unique email: " + uniqueEmail);
            }

            // Step 2: Navigate to Signup page
            driver.get(p.getProperty("appURL1") + "/index.php?route=account/register");
            logger.info("Navigated to Signup page");

            // Step 3: Fill signup form
            SignUpPage sp = new SignUpPage(driver);
            sp.clickCreateAccountLink();
            sp.setFirstName(firstName);
            sp.setLastName(lastName);
            sp.setEmail(uniqueEmail);
            sp.setPassword(password);
            sp.setConfirmPassword(confirmPassword);
            sp.clickCreateAccount();
            sp.clickWelcomeDropdown();
            sp.clickSignOutLink();
            logger.info("Filled form and clicked Create Account");

            // Optional: Handle dropdown after signup
            sp.clickWelcomeDropdown();

            // Step 4: Check if signup succeeded
            MyAccountPage macc = new MyAccountPage(driver);
            boolean isSignedUp = macc.isMyAccountPageExists();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            if (password.equals(confirmPassword)) {
                if (isSignedUp) {
                    logger.info(" Signup successful as expected");

                    WebElement welcomeMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//span[contains(text(),'Welcome')]")));
                    String welcomeText = welcomeMessage.getText();
                    logger.info("Welcome text: " + welcomeText);

                    Assert.assertTrue(welcomeText.toLowerCase().contains("welcome"), "Welcome message not found.");

                    welcomeMessage.click();
                    
                    WebElement WelcomeTestAdminDropdown = wait.until(
                            ExpectedConditions.elementToBeClickable(By.xpath("body div[class='page-wrapper'] header[class='page-header'] div[class='panel wrapper'] div[class='panel header'] ul:nth-child(1)")));
                    WelcomeTestAdminDropdown.click();
                    logger.info("Welcome, Test Admin");
                    
                    
                    WebElement signOutLink = wait.until(
                        ExpectedConditions.elementToBeClickable(By.linkText("Sign Out")));
                    signOutLink.click();

                    logger.info(" Clicked on Sign Out");

                    wait.until(ExpectedConditions.titleContains("Customer Logout"));
                    String logoutTitle = driver.getTitle();
                    logger.info("Logout page title: " + logoutTitle);
                    Assert.assertTrue(logoutTitle.contains("Customer Logout"), "Logout not confirmed via title");

                    logger.info(" Logout confirmed");
                } else {
                    logger.error(" Signup failed even though passwords matched");
                    captureErrorDetails();
                    Assert.fail("Expected signup to succeed, but it failed.");
                }
            } else {
                if (isSignedUp) {
                    logger.error(" Signup succeeded despite password mismatch!");
                    performLogoutAfterMismatch();
                    Assert.fail("Expected signup to fail due to password mismatch, but it succeeded.");
                } else {
                    logger.info(" Signup failed as expected due to password mismatch.");
                }
            }

        } catch (Exception e) {
            logger.error(" Exception during signup test: " + e.getMessage(), e);
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }

        logger.info(" Finished TC_SignUpData test for: " + email);
    }

    private void captureErrorDetails() {
        try {
            logger.info("Page title after signup: " + driver.getTitle());
            logger.info("Current URL: " + driver.getCurrentUrl());

            List<WebElement> errors = driver.findElements(By.cssSelector(".alert-danger, .error, .validation-advice"));
            if (!errors.isEmpty()) {
                for (WebElement error : errors) {
                    logger.warn(" Error Message: " + error.getText());
                }
            } else {
                logger.warn(" No error messages found on UI, but signup failed.");
            }

        } catch (Exception e) {
            logger.error(" Exception while capturing error details: " + e.getMessage());
        }
    }

    private void performLogoutAfterMismatch() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement welcomeMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[contains(text(),'Welcome')]")));
            welcomeMessage.click();

            WebElement signOutLink = wait.until(
                ExpectedConditions.elementToBeClickable(By.linkText("Sign Out")));
            signOutLink.click();

            logger.info(" Logged out after unexpected signup success (password mismatch case)");
        } catch (Exception e) {
            logger.error("Failed to logout after unexpected signup success: " + e.getMessage());
        }
    }
}
