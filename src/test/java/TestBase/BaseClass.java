package TestBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseClass {
	 public static WebDriver driver;  // Made static to ensure shared instance if needed
	    public Logger logger = LogManager.getLogger(getClass());
	    public Properties p;

	    @Parameters({"os", "browser"})
	    @BeforeClass
	    public void setup(@Optional("windows") String os, @Optional("chrome") String br) throws IOException {
	        // Load config.properties file
	    	FileInputStream fis = new FileInputStream("resources/config.properties");


	        p = new Properties();
	        p.load(fis);

	        // Browser setup
	        switch (br.toLowerCase()) {
	            case "chrome":driver = new ChromeDriver();break;
	            case "edge": driver = new EdgeDriver();break;
	            case "firefox":driver = new FirefoxDriver();break;
	            default: System.out.println("Invalid browser name...");
	                return;
	        }

	        driver.manage().deleteAllCookies();
	        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	        driver.get(p.getProperty("appURL1")); // Read URL from properties
	        driver.manage().window().maximize();
	    }

	    @AfterClass
	    public void tearDown() {
	        if (driver != null) {
	            driver.quit();
	        }
	    }
	    
	    // Utility methods
	    public String randomString() {
	        return RandomStringUtils.randomAlphabetic(5);
	    }

	    public String randomNumber() {
	        return RandomStringUtils.randomNumeric(10);
	    }

	    public String randomAlphaNumeric() {
	        String generatedString = RandomStringUtils.randomAlphabetic(3);
	        String generatedNumber = RandomStringUtils.randomNumeric(3);
	        return generatedString + "@" + generatedNumber;
	    }
	    
	    public String captureScreen(String tname) throws IOException{
	    	
	    	String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
	    	
	    	TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
	    	File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
	    	
	    	String targetFilePath=System.getProperty("user.dir")+"\\screenshots\\"+ tname + "_" + timeStamp + ".png";
	    	File targetFile=new File(targetFilePath);
	    	
	    	sourceFile.renameTo(targetFile);
	    	
	    	return targetFilePath;
	    	
	    }

}
