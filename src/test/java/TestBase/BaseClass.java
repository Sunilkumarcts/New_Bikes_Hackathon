package TestBase;
 
import java.io.File;

import java.io.IOException;

import java.text.SimpleDateFormat;

import java.time.Duration;

import java.util.Date;

import java.util.ResourceBundle;
 
import org.apache.commons.io.FileUtils;

import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;

import org.openqa.selenium.OutputType;

import org.openqa.selenium.TakesScreenshot;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.chrome.ChromeOptions;

import org.openqa.selenium.edge.EdgeDriver;

import org.openqa.selenium.edge.EdgeOptions;

import org.testng.annotations.*;

import org.testng.asserts.SoftAssert;
 
import Utilities.ExcelUtilities;
 
public class BaseClass {

	public static WebDriver driver;

	public static Logger logger;

	public static ResourceBundle rb = ResourceBundle.getBundle("config");

	public static String fileName = "Data";

	public static ExcelUtilities excelUtilities = new ExcelUtilities();

	public static SoftAssert sa=new SoftAssert();

	@BeforeTest(groups = { "sanity" })

	@Parameters({ "browser" })

	public void setUpEnvi(String br) throws IOException {
 
		logger = LogManager.getLogger(this.getClass());
 
		switch (br.toLowerCase()) {

		case "chrome":

			ChromeOptions chromeOpt = new ChromeOptions();

			chromeOpt.addArguments("disable-notifications");

			driver = new ChromeDriver(chromeOpt);

			break;

		case "edge":

			EdgeOptions edgeOpt = new EdgeOptions();

			edgeOpt.addArguments("disable-notifications");

			driver = new EdgeDriver(edgeOpt);

			break;

		default:

			System.out.println("No browser is matching");

			return;

		}
 
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

		driver.get(getURL());

		driver.manage().window().maximize();
 
	}
 
	public String getURL() {

		return rb.getString("url");

	}
 
	public void screenshot(String fileName) throws IOException {

		TakesScreenshot srcshot = (TakesScreenshot) driver;

		File src = srcshot.getScreenshotAs(OutputType.FILE);

		FileUtils.copyFile(src, new File(".\\screenshots\\" + fileName + ".png"));

	}
 
	public String captureScreen(String file) throws IOException {

		String timeStamp = new SimpleDateFormat("yyyyMMddHHss").format(new Date());

		TakesScreenshot takeScreenshot = (TakesScreenshot) driver;

		File srcFile = takeScreenshot.getScreenshotAs(OutputType.FILE);
 
		String targetFilePath = System.getProperty("user.dir") + "\\ExtentReportScreenshots\\" + file + "_" + timeStamp

				+ ".png";

		File targetFile = new File(targetFilePath);

		srcFile.renameTo(targetFile);

		return targetFilePath;

	}


	public static void sleep(long time) {

		try {

			Thread.sleep(time);

		} catch (InterruptedException e) {

			e.printStackTrace();

		}

	}
 
	@AfterTest(groups = { "regression" })

	public void tearDown() {

		driver.quit();

	}
 
}