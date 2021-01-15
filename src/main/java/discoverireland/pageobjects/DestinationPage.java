package discoverireland.pageobjects;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static discoverireland.constants.URLs.DESTINATIONS_PAGE;
import static discoverireland.helpers.WaitHelper.waitForPageLoad;
import static java.time.Duration.ofSeconds;

@Getter
public class DestinationPage extends PageObject {

    @FindBy(css = ".sc-11tmdj9-0")
    private WebElement heading;

    public DestinationPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver, ofSeconds(20)), this);
    }

    public DestinationPage openDestinationsPage() {
        driver.get(DESTINATIONS_PAGE);
        waitForPageLoad(driver);
        return new DestinationPage(driver);
    }

}
