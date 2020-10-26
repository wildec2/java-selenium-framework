package discoverireland;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static java.lang.Boolean.parseBoolean;
import static java.util.Collections.singletonMap;
import static org.openqa.selenium.remote.CapabilityType.ACCEPT_INSECURE_CERTS;
import static org.openqa.selenium.remote.CapabilityType.ACCEPT_SSL_CERTS;

public class DriverSupplier {

    private static final String HEADLESS = System.getProperty("headless", "false");
    private static final String RUN_ON_GRID = System.getProperty("grid", "false");
    private static final String hubUrl = System.getProperty("hubUrl", "http://localhost:4444/wd/hub");

    private static final Supplier<WebDriver> chromeSupplier = () -> {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--incognito");
        chromeOptions.addArguments("--window-size=1360,1020");
        return getChromeDriver(chromeOptions);
    };

    private static final Supplier<WebDriver> chromeMobileEmulationSupplier = () -> {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--incognito");
        chromeOptions.setExperimentalOption("mobileEmulation", singletonMap("deviceName", System.getProperty("device", "iPhone 6")));
        return getChromeDriver(chromeOptions);
    };

    private static final Supplier<WebDriver> chromeTabletEmulationSupplier = () -> {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--incognito");
        chromeOptions.setExperimentalOption("mobileEmulation", singletonMap("deviceName", System.getProperty("device", "iPad")));
        return getChromeDriver(chromeOptions);
    };

    private static final Supplier<WebDriver> firefoxSupplier = () -> {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments("--window-size=1360,1020");
        return getFirefoxDriver(firefoxOptions);
    };

    private static final Supplier<WebDriver> edgeSupplier = () -> {
        WebDriverManager.edgedriver().setup();
        return new EdgeDriver();
    };

    @SneakyThrows
    private static WebDriver getChromeDriver(ChromeOptions chromeOptions) {
        WebDriverManager.chromedriver().setup();
        if (parseBoolean(HEADLESS)) {
            chromeOptions.addArguments("--no-sandbox");
            chromeOptions.addArguments("--headless");
            chromeOptions.setCapability(ACCEPT_SSL_CERTS, true);
            chromeOptions.setCapability(ACCEPT_INSECURE_CERTS, true);
        }
        if (parseBoolean(RUN_ON_GRID)) {
            chromeOptions.addArguments("--disable-dev-shm-usage");
            return new RemoteWebDriver(new URL(hubUrl), chromeOptions);
        }
        else {
            return new ChromeDriver(chromeOptions);
        }
    }

    @SneakyThrows
    private static WebDriver getFirefoxDriver(FirefoxOptions firefoxOptions) {
        WebDriverManager.firefoxdriver().setup();
        if (parseBoolean(HEADLESS)) {
            firefoxOptions.addArguments("--no-sandbox");
            firefoxOptions.addArguments("--headless");
            firefoxOptions.setCapability(ACCEPT_SSL_CERTS, true);
            firefoxOptions.setCapability(ACCEPT_INSECURE_CERTS, true);
        }
        if (parseBoolean(RUN_ON_GRID)) {
            firefoxOptions.addArguments("--disable-dev-shm-usage");
            return new RemoteWebDriver(new URL(hubUrl), firefoxOptions);
        }
        else {
            return new FirefoxDriver(firefoxOptions);
        }
    }

    private static final Map<String, Supplier<WebDriver>> MAP = new HashMap<>();

    static {
        MAP.put("chrome", chromeSupplier);
        MAP.put("chromeMobileEmulator", chromeMobileEmulationSupplier);
        MAP.put("chromeTabletEmulator", chromeTabletEmulationSupplier);
        MAP.put("firefox", firefoxSupplier);
        MAP.put("edge", edgeSupplier);
    }

    public static WebDriver getDriver(String browser) {
        return MAP.get(browser).get();
    }
}