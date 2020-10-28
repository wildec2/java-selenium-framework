package discoverireland;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

public class TestBase {

    private WebDriver driver;

    @BeforeMethod
    @Parameters("Browser")
    public void setUpEnvBeforeTest(@Optional("chrome") String browser, Method method) {
        Test test = method.getAnnotation(Test.class);

        String device = test.groups()[0];
        switch (device) {
            case "mobile":
                driver = DriverSupplier.getDriver("chromeMobileEmulator");
                break;
            case "desktop":
                driver = DriverSupplier.getDriver(browser);
                break;
            case "tablet":
                driver = DriverSupplier.getDriver("chromeTabletEmulator");
                break;
            default:
                throw new IllegalArgumentException("Invalid device type for test: " + device);
        }
    }

    protected WebDriver getDriver() {
        return driver;
    }

    @AfterMethod(alwaysRun = true)
    public void cleanEnvAfterTest(ITestResult result) {
            if (getDriver() != null) {
                getDriver().quit();
            }
    }
}