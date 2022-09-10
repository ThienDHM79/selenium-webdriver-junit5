package net.practice.junit5.ch03;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.netty.util.internal.logging.InternalLogger;
import org.apache.commons.logging.LogFactory;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

public class WebFormLocator {
    WebDriver driver;
    String sutUrl ="https://bonigarcia.dev/selenium-webdriver-java/web-form.html";
    SoftAssertions sa = new SoftAssertions();
    static final Logger log = getLogger(lookup().lookupClass());

    @BeforeEach
    void setUp(){ driver = WebDriverManager.chromiumdriver().create();}
    @AfterEach
    void tearDown(){ driver.quit();}

    @Test
    void shouldVisible(){
        driver.get(sutUrl);
        //text input
        WebElement inputByClass = driver.findElement(By.tagName("input"));
        sa.assertThat(inputByClass.getAttribute("type")).isEqualTo("text");
        //password
        WebElement pwByAtt = driver.findElement(By.xpath("//*[@name='my-password']"));
        sa.assertThat(pwByAtt.getAttribute("type")).isEqualTo("password");
        //textarea
        WebElement textAreaCss = driver.findElement(By.cssSelector("textarea[name='my-textarea']"));
        sa.assertThat(textAreaCss.getAttribute("rows")).isEqualTo(3);
        //disabled input
        WebElement disInputAtt = driver.findElement(By.xpath("//*[@name='my-disabled']"));
        sa.assertThat(disInputAtt.isEnabled()).isFalse();
        //readonly input
        WebElement readInputAtt = driver.findElement(By.xpath("//input[@readonly]"));
        assertThat(readInputAtt.isEnabled()).isTrue();
    }
    @Test
    void testDatePicker(){
        //get current date
        LocalDate curDate = LocalDate.now();
        String dateFormat = curDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        System.out.println(dateFormat);
        //click on datepicker
        driver.get(sutUrl);
        WebElement dateByXPath = driver.findElement(By.name("my-date"));
        dateByXPath.click();
        //click on month - use var currentYear
        int curYear = curDate.getYear();
        WebElement yearDisplayByXpath = driver.findElement(By.xpath("//th[@class='datepicker-switch']"));
        yearDisplayByXpath.click();
        //click on lef arrow use relative locator
        //no need cuz same year
        //click on month
        String strMonth = curDate.format(DateTimeFormatter.ofPattern("MMM"));
        WebElement mthByXPath = driver.findElement(By.cssSelector("span[class$=focused]"));
        /* cannot select the text
        mthByXPath = driver.findElement(By.xpath(
                String.format("//span[text(),'%s']", strMonth)
            ));
         */
        mthByXPath.click();
        //click on present date of that month
        WebElement dateNum = driver.findElement(
                By.xpath("//td[@class='day' and text()='"
                        + curDate.getDayOfMonth()
                        + "']"));
        dateNum.click();
        //get final date on the input text
        WebElement inputDateByXpath = driver.findElement(By.xpath("//input[@name='my-date']"));
        String dateSelected = inputDateByXpath.getAttribute("value");
        //assert expected same as selected MM/dd/yyyy
        assertThat(dateSelected).isEqualTo(dateFormat);
    }
    @Test
    void testFileUpload() throws IOException {
        driver.get(sutUrl);
        WebElement inputFile = driver.findElement(By.name("my-file"));
        Path tempFile = Files.createTempFile("tempfiles", ".tmp");
        String filename = tempFile.toAbsolutePath().toString();
        //log from LoggerFactory
        log.debug("Using temp file {} in file upload", filename);
        inputFile.sendKeys(filename);

        driver.findElement(By.tagName("form")).submit();
        assertThat(driver.getCurrentUrl()).isNotEqualTo(sutUrl);
    }
    @Test
    void testNavigation(){
        //checkbox and radio button
        driver.get(sutUrl);
        WebElement checkbox2 = driver.findElement(By.id("my-check-2"));
        checkbox2.click();
        assertThat(checkbox2.isSelected()).isTrue();

        WebElement checkbox1 =driver.findElement(By.id("my-check-1"));
        //co check thi isSelected = true
        assertThat(checkbox1.isSelected()).isTrue();

        WebElement radio1 = driver.findElement(By.id("my-radio-1"));

    }
    @Test
    void testReturn(){
        driver.get(sutUrl);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        WebElement returnLink = driver.findElement(By.xpath("//a[starts-with(@href,'./index')]"));
        returnLink.click();
        String curUrl = driver.getCurrentUrl().toString();
        String expectUrl = "https://bonigarcia.dev/selenium-webdriver-java/index.html";
        assertThat(curUrl).isEqualTo(expectUrl);
    }
    @Test
    void testDropSelect(){
        driver.get(sutUrl);
        WebElement dropdown = driver.findElement(By.name("my-select"));
        dropdown.click();
        WebElement option = driver.findElement(By.cssSelector("select option[value='3']"));
        option.click();
        //how get visible text
        Select select = new Select(driver.findElement(By.name("my-select")));
        String optionLabel = "Three";
        select.selectByVisibleText(optionLabel);
        assertThat(select.getFirstSelectedOption().getText()).isEqualTo(optionLabel);
        //get value only
        assertThat(dropdown.getAttribute("value")).isEqualTo("3");
    }
    @Test
    void testDropDataList(){
        driver.get(sutUrl);
        //click to input data list
        WebElement dropDataList = driver.findElement(By.name("my-datalist"));
        dropDataList.click();
        //xpath de lay gia tri
        WebElement option = driver.findElement(By.xpath("//datalist/option[2]"));
        String optionValue = option.getAttribute("value");
        //input kboard gtri vao
        dropDataList.sendKeys(optionValue);
        //check gia tri lay va gia tri expect
        assertThat(optionValue).isEqualTo("New York");

    }



}
