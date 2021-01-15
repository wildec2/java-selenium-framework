package discoverireland;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

public class TestBase {

    @BeforeMethod
    @Parameters("Browser")
    public void setUpEnvBeforeTest(@Optional("chrome") String browser, Method method) {
        Test test = method.getAnnotation(Test.class);

        WebDriver driver;

        String device = test.groups()[0];
        switch (device) {
            case "mobile":
                setDriver(DriverSupplier.getDriver("chromeMobileEmulator"));
                driver = getDriver();
                break;
            case "desktop":
                setDriver(DriverSupplier.getDriver(browser));
                driver = getDriver();
                break;
            case "tablet":
                setDriver(DriverSupplier.getDriver("chromeTabletEmulator"));
                driver = getDriver();
                break;
            default:
                throw new IllegalArgumentException("Invalid device type for test: " + device);
        }
    }

    protected WebDriver getDriver() {
        return DriverFactory.getInstance().getDriver();
    }

    private void setDriver(WebDriver driver) {
        DriverFactory.getInstance().setDriver(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void cleanEnvAfterTest(ITestResult result) {
            if (getDriver() != null) {
                DriverFactory.getInstance().closeBrowser();
            }
    }
}