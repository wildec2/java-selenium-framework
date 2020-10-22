package discoverireland;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import discoverireland.pageobjects.Homepage;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class HomepageTests extends TestBase {

    private Homepage homepage;

    @BeforeMethod
    public void setUpEnvBeforeTest() {
        homepage = new Homepage(getDriver()).openHomePage();
    }

    @Test(groups = "mobile", description = "sample test - mobile")
    public void mobileTest() {
        testPageHeading();
    }

    @Test(groups = "tablet", description = "sample test - tablet")
    public void tabletTest() {
        testPageHeading();
    }

    @Test(groups = "desktop", description = "sample test - desktop")
    public void desktopTest() {
        testPageHeading();
    }

    private void testPageHeading() {
        assertThat("page h1 incorrect", homepage.getHeading().getText(), is("Explore close to home"));
    }
}