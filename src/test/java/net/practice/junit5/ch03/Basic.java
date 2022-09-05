package net.practice.junit5.ch03;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;

import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class Basic {
    WebDriver driver;
    //soft assertion for group multi assert
    SoftAssertions sa = new SoftAssertions();
    String sutUrl = "https://bonigarcia.dev/selenium-webdriver-java/";
    String expectedTitle = "Hands-On Selenium WebDriver with Java";
    String header = "Hands-On Selenium WebDriver with Java";
    String footer = "Copyright © 2021-2022 Boni García";
    @BeforeEach
    void setup(){
        driver = WebDriverManager.chromiumdriver().create();
    }
    @AfterEach
    void teardown(){
        driver.quit();
    }
    @Test
    void shouldSameUrl(){
        driver.get(sutUrl);

        sa.assertThat(driver.getCurrentUrl()).isEqualTo(sutUrl);
        System.out.println(driver.getCurrentUrl().toString());
        sa.assertThat(driver.getTitle()).isEqualTo(expectedTitle);
        System.out.println(driver.getTitle());
        sa.assertThat(driver.getPageSource()).containsIgnoringCase("</html");
        System.out.println(driver.getPageSource().contains("</html>"));
    }
    @Test
    void shouldCorrectHeadFoot(){
        driver.get(sutUrl);
        WebElement headerByTag = driver.findElement(By.tagName("h1"));
        sa.assertThat(headerByTag.getText().toString()).isEqualTo(header);

        WebElement footerByXpath = driver.findElement(By.cssSelector("footer div span.text-muted"));
        sa.assertThat(footerByXpath.getText()).isEqualTo(footer);
    }
    @Test
    void shouldCorrectChapter(){
        driver.get(sutUrl);
        //element of chap3
        /*
        WebElement chapTitByCss = driver.findElement(By.cssSelector(".row .card:nth-child(1) .card-title"));
        System.out.println(chapTitByCss.getText());
        WebElement chapTitByTag = driver.findElement(By.tagName("h5"));
        sa.assertThat(chapTitByTag.getText()).isEqualTo("Chapter 3. WebDriver Fundamentals");

        List<WebElement> itemInChapByCss = driver.findElements(By.cssSelector(".row .card:nth-child(1) .a"));
        sa.assertThat(itemInChapByCss.size()).isEqualTo(8);
         */
        //use list to loop through all chap
        List<WebElement> chapTitByXPaths = driver.findElements(By.xpath("//h5[@class='card-title']"));
        List chapList = RequiredList();
        for (int i = 0; i < chapTitByXPaths.size(); i++
             ) {
            sa.assertThat(chapTitByXPaths.get(i).getText()).isEqualTo(chapList.get(i));
            System.out.println(chapTitByXPaths.get(i).getText());
        }
    }
    //need check all chap title
    public List<String> RequiredList(){
        List chapList = new ArrayList();
        chapList.add("Chapter 3. WebDriver Fundamentals");
        chapList.add("Chapter 4. Browser-Agnostic Features");
        chapList.add("Chapter 5. Browser-Specific Manipulation");
        chapList.add("Chapter 7. The Page Object Model (POM)");
        chapList.add("Chapter 8. Testing Framework Specifics");
        chapList.add("Chapter 9. Third-Party Integrations");

        return chapList;
    }
    //need check for card chap3
    public List<String> chap3Link(){
        List aList = new ArrayList();
        aList.add("Web form");

        return aList;
    }
    @Test
    void checkCssButton(){
        driver.get(sutUrl);
        driver.manage().window().maximize();
        Actions action = new Actions(driver);
        List<WebElement> btnByXPaths = driver.findElements(By.xpath("//*[@class='card-body']/a"));

        for (int i = 0; i < btnByXPaths.size(); i++) {
            action.moveToElement(btnByXPaths.get(i)).build().perform();
            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            String cssRGBA = btnByXPaths.get(i).getCssValue("background-color");
            String cssHex = Color.fromString(cssRGBA).asHex();
            System.out.println(cssHex);
            sa.assertThat(cssHex.toString()).isEqualTo("#0d6efd");
        }
    }




}
