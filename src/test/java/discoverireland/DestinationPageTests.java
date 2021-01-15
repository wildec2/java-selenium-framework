package discoverireland;

import discoverireland.pageobjects.DestinationPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DestinationPageTests extends TestBase {

    private DestinationPage destinationPage;

    @BeforeMethod
    public void setUpEnvBeforeTest() {
        destinationPage = new DestinationPage(getDriver()).openDestinationsPage();
    }

    @Test(groups = "mobile", description = "sample destinations page test - mobile")
    public void mobileTest() {
        testPageHeading();
    }

    @Test(groups = "tablet", description = "sample destinations page test - tablet")
    public void tabletTest() {
        testPageHeading();
    }

    @Test(groups = "desktop", description = "sample destinations page test - desktop")
    public void desktopTest() {
        testPageHeading();
    }

    private void testPageHeading() {
        assertThat("page h1 incorrect", destinationPage.getHeading().getText(), is("More destinations to explore"));
    }

}
