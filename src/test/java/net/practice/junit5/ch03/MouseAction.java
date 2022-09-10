package net.practice.junit5.ch03;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MouseAction {
    String sutUrl ="https://bonigarcia.dev/selenium-webdriver-java/dropdown-menu.html";
    WebDriver driver;
    @BeforeEach
    void setUp(){
        driver = WebDriverManager.chromiumdriver().create();
    }
    @AfterEach
    void tearDown(){
        driver.quit();
    }
    @Test
    void testLeftMenu(){
        driver.get(sutUrl);
        WebElement leftMenu = driver.findElement(By.id("my-dropdown-1"));
        leftMenu.click();
        List <WebElement> listItem = driver.findElements(
                By.className("dropdown-item"));
        for (WebElement choiceItem: listItem
             ) {
            System.out.println(choiceItem.getText());
        }
    }
    @Test
    void testContextAndDoubleClick(){
        driver.get(sutUrl);
        WebElement dropdown2 = driver.findElement(By.id("my-dropdown-2"));
        //other than left click -> action
        Actions action = new Actions(driver);
        action.contextClick(dropdown2).build().perform();
        WebElement contextMenu2 = driver.findElement(By.id("context-menu-2"));
        assertThat(contextMenu2.isDisplayed()).isTrue();

        WebElement dropdown3 = driver.findElement(By.id("my-dropdown-3"));
        action.doubleClick(dropdown3).build().perform();
        WebElement contextMenu3 = driver.findElement(By.id("context-menu-3"));
        assertThat(contextMenu3.isDisplayed()).isTrue();
    }

}
