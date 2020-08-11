package org.example;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;

public class App

{
    static WebDriver wd;
    static int ssindex;

    public static void main( String[] args )
    {
        setupExtentReport();
        setupDriver();
        startTests();
        generateReport();

    }

    private static void generateReport() {
        closeDriver();
        extent.flush();
//        File newReport = new File("");
    }

    static ExtentTest currentTest;

    private static void startTests() {
        startNavigation();
        startSearch();
    }

    static ExtentHtmlReporter htmlReporter;
    static ExtentReports extent;
    private static void setupExtentReport() {
        htmlReporter = new ExtentHtmlReporter("index.html");
        extent  = new ExtentReports();

        extent.attachReporter(htmlReporter);
    }

    private static void startNavigation()  {
        createTest("Navigation_Test", "Basic navigation");
        log( "Navigating to Google Search Page");
        navigateTo("https://google.com");
        assertNavigation("//input[@name = 'q']", "Google Search Page");


        sendKeys("//input[@name = 'q']", "The Daily");
        log( "Clicking search button");
        click("//input[@name='btnK'][1]");
        assertClick("//h3[contains(text(),'The Daily - The New York Times')]", "Google Search Button");
        navigateTo("https://www.nytimes.com/column/the-daily");
        assertNavigation("//h1[contains(text(),'The Daily')]", "The Daily website");
    }

    private static void sendKeys(String xpath, String keys) {
        try {
                wd.findElement(By.xpath(xpath)).sendKeys(keys);
                passSendKeys("Successfully sent keys '"+keys+"' to xpath: " + xpath, keys);
        }
        catch (Exception e){
            log("Failed to send keys '"+keys+"' to xpath: " + xpath);
            couldNotFindElement( xpath);
        }
    }

    private static void passSendKeys(String message, String keys) {

        try {
            currentTest.pass(message,
                    MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath("-KEYSEND-SUCCESS: " + keys )).build());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void couldNotFindElement(String xpath) {

        try {
            currentTest.fail("Failed to find element: " + xpath,
                    MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath("-XPATH-NOT-FOUND: " + xpath )).build());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createTest(String testName, String description) {
        currentTest = extent.createTest(testName, description);
    }


    private static void startSearch() {
        createTest("Search Test", "Basic click and search");
        log("Clicking search button");
        click("//button[@data-test-id='search-button']");
        assertClick("//input[@data-testid='search-input']","The Daily Search Button");
        log("Searching for 'The Day That Shook Beirut'");
        sendKeys("//input[@data-testid='search-input']", "The Day That Shook Beirut");
        log("Clicking 'GO'");
        click("//button[@data-test-id='search-submit']");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertClick("//h4[contains(text(),'The Day That Shook Beirut')]", "The 'GO' button");
    }

    private static void log(String action) {
        currentTest.log(Status.INFO,action);
    }


    private static void assertNavigation(String path, String message)  {
        if(wd.findElement(By.xpath(path)).isDisplayed()){
            try {
                currentTest.pass("Navigate to: " + message,
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath("-NAVIGATION-SUCCESS: " + message )).build());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try {
                currentTest.fail("Navigate to: " + message,
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath("-NAVIGATION-FAILURE: " + message )).build());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static void  assertClick(String path, String component)  {
        try {
            if (wd.findElement(By.xpath(path)).isDisplayed()) {
                try {
                    currentTest.pass("Successfully clicked: " + component,
                            MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath("-CLICK-SUCCESS: " + component)).build());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    currentTest.fail("Failed to click: " + component,
                            MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath("-CLICK-FAILURE: " + component)).build());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e ){

            try {
                currentTest.fail("Failed to find: " + component + "trace: " + e,
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath("-CLICK-FAILURE: " + component)).build());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }
    }

    private static String screenshotPath(String name) {
        try {
            ssindex++;
            String screenShotPath = GetFullPageScreenShot.capture(wd, ssindex + name);
            return screenShotPath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void navigateTo(String url) {
        wd.navigate().to(url);
    }


    private static void click(String path) {
        wd.findElement(By.xpath(path)).click();
    }

    private static void setupDriver() {
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/chromedriver/chromedriver.exe");
        wd = new ChromeDriver();
        wd.manage().window().maximize();
    }
    private static void closeDriver() {
        wd.close();
        wd.quit();
    }
}
