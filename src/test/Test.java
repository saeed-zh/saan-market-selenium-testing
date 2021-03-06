package test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;

/**
 * Created by SAEED on 2015-12-31
 * for project saan-market-selenium-testing .
 */

public class Test {

    private WebDriver driver;
    private String baseUrl;

    private final StringBuffer verificationErrors = new StringBuffer();

    public void setUp() throws Exception {
        driver = new FirefoxDriver();
        baseUrl = "http://localhost:25557";
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
    }

    /**
     * this function test the project phase 3 specification
     * user attempt logIn with a valid username and password
     * search "galaxy" keyword and find "galaxy s6", then
     * choose it to adding to cart, and finally open the cart
     * and see the selected product
     * @throws Exception
     */
    public void test1() throws Exception {
        String username = "tempuser", password = "123", searchkey = "galaxy";
        try {
            FileInputStream inputStream = new FileInputStream(new File(".\\.\\inputData.xlsx"));

            // Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            // Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Cell> cellIterator = sheet.iterator().next().cellIterator();
            username = cellIterator.next().getStringCellValue();
            password = String.valueOf(((int) cellIterator.next().getNumericCellValue()));
            System.out.println(password);
            searchkey = cellIterator.next().getStringCellValue();
            inputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("beginning of test1");
        driver.get(baseUrl + "/Home/Index");
        // log in
        System.out.println("user being login");
        driver.findElement(By.xpath(".//*[@id='loginOrRegister']/a[1]")).click();
        driver.findElement(By.xpath(".//*[@id='inputUsernameRegister']")).sendKeys(username); // username
        driver.findElement(By.xpath(".//*[@id='inputPasswordRegister']")).sendKeys(password); // password
        driver.findElement(By.xpath(".//*[@id='loginModal']/div[2]/form/input")).click();      // submit login

        Assert.assertEquals(username, driver.findElement(By.xpath(".//*[@id='loginOrRegister']/a[1]")).getText());
        System.out.println("user login successfully");
        // search
        System.out.println("user being search");
        driver.findElement(By.xpath(".//*[@id='SearchString']")).sendKeys(searchkey); // type a search key for example galaxy
        driver.findElement(By.xpath(".//*[@id='stickyNavbar']/div/div/div/div[1]/div/div/form/button")).click();
        // add to cart
        System.out.println("search page is opening");
        System.out.println("adding galaxy s6 to cart");

        Actions action = new Actions(driver);
        WebElement mouseOverOnProduct = driver.findElement(By.xpath(".//*[@id='isotopeContainer']/div[2]/div/div[1]/div/img"));
        action.moveToElement(mouseOverOnProduct).build().perform();
        driver.findElement(By.xpath(".//*[@id='isotopeContainer']/div[2]/div/div[1]/div/div/a[2]")).click();

        // save name of the product
        System.out.println("verifying cart");
        String selectedProductName = driver.findElement(
                By.xpath(".//*[@id='isotopeContainer']/div[2]/div/div[2]/div[2]/h5")).getText();

        // verify cart content
        action.moveToElement(driver.findElement(By.xpath(".//*[@id='cartContainer']"))).build().perform();
        //driver.findElement(By.xpath(".//*[@id='cartContainer']")).click();
        List<WebElement> products = driver.findElement(
                By.xpath(".//*[@id='cartContainer']/div[2]")).findElements(By.className("item-in-cart"));
        // verify count of products in cart
        Assert.assertEquals(1,products.size());
        // verify name of product in list that must be equal with the selected product

        String curProductName = driver.findElement(
                By.xpath(".//*[@id='cartContainer']/div[2]/div[1]/div[2]/strong/a")).getText();
        System.out.println("product in list is: " + curProductName);
        Assert.assertEquals(curProductName, selectedProductName);
        System.out.println("test1 is complete successfully");

    }

    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    public static void main(String[] args) throws Exception {
        Test t = new Test();
        t.setUp();
        t.test1();
        t.tearDown();

        t.setUp();
        t.test2();
        t.tearDown();
    }

    /**
     * this function test the logIn functionality
     * it tries to login with fake and invalid username and password
     * and each time get an error message
    */
    private void test2() {
        String username1 = "tempuser2", password1 = "123",
                username2 = "tempuser2", password2 = "1234",
                username3 = "saeed", password3 = "1234";
        try {
            FileInputStream inputStream = new FileInputStream(new File(".\\.\\inputData.xlsx"));

            // Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            // Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(1);
            Iterator<Row> rowIterator = sheet.iterator();
            Iterator<Cell> cellIterator = rowIterator.next().cellIterator();
            username1 = cellIterator.next().getStringCellValue();
            password1 = String.valueOf(((int) cellIterator.next().getNumericCellValue()));

            cellIterator = rowIterator.next().cellIterator();
            username2 = cellIterator.next().getStringCellValue();
            password2 = String.valueOf(((int) cellIterator.next().getNumericCellValue()));

            cellIterator = rowIterator.next().cellIterator();
            username3 = cellIterator.next().getStringCellValue();
            password3 = String.valueOf(cellIterator.next().getStringCellValue());

            inputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("beginning of test2");
        driver.get(baseUrl + "/Home/Index");
        // log in
        System.out.println("user being login");
        driver.findElement(By.xpath(".//*[@id='loginOrRegister']/a[1]")).click();
        driver.findElement(By.xpath(".//*[@id='inputUsernameRegister']")).sendKeys(username1); // username
        driver.findElement(By.xpath(".//*[@id='inputPasswordRegister']")).sendKeys(password1); // password
        driver.findElement(By.xpath(".//*[@id='loginModal']/div[2]/form/input")).click();      // submit login

        String errorMessage = driver.findElement(By.xpath(".//*[@id='loginModal']/div[2]/form/div[1]/ul/li")).getText();

        Assert.assertEquals("نام کاربری یا رمز عبور اشتباه است.", errorMessage);
        System.out.println("user login unsuccessfully");

        // try again
        driver.findElement(By.xpath(".//*[@id='inputUsernameRegister']")).clear(); // clear username
        driver.findElement(By.xpath(".//*[@id='inputPasswordRegister']")).clear(); // clear password

        driver.findElement(By.xpath(".//*[@id='inputUsernameRegister']")).sendKeys(username2); // username
        driver.findElement(By.xpath(".//*[@id='inputPasswordRegister']")).sendKeys(password2); // password
        driver.findElement(By.xpath(".//*[@id='loginModal']/div[2]/form/input")).click();      // submit login

        errorMessage = driver.findElement(By.xpath(".//*[@id='loginModal']/div[2]/form/div[1]/ul/li")).getText();

        Assert.assertEquals("نام کاربری یا رمز عبور اشتباه است.", errorMessage);
        System.out.println("user login unsuccessfully again");

        // try again
        driver.findElement(By.xpath(".//*[@id='inputUsernameRegister']")).clear(); // clear username
        driver.findElement(By.xpath(".//*[@id='inputPasswordRegister']")).clear(); // clear password

        driver.findElement(By.xpath(".//*[@id='inputUsernameRegister']")).sendKeys(username3); // username
        driver.findElement(By.xpath(".//*[@id='inputPasswordRegister']")).sendKeys(password3); // password
        driver.findElement(By.xpath(".//*[@id='loginModal']/div[2]/form/input")).click();      // submit login

        errorMessage = driver.findElement(By.xpath(".//*[@id='loginModal']/div[2]/form/div[1]/ul/li")).getText();

        Assert.assertEquals("نام کاربری یا رمز عبور اشتباه است.", errorMessage);
        System.out.println("user login unsuccessfully again");

        System.out.println("test2 is complete successfully");

    }
}
