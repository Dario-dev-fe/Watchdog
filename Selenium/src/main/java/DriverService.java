import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class DriverService {

    private final AtomicBoolean running = new AtomicBoolean(false);
    private volatile WebDriver driver;
    private volatile Thread watchDogThread;

    public synchronized void startAutomation(String url) {
        stopAutomation();
        driver = getWebDriver();
        driver.get(url);
        driver.manage().window().maximize();
        running.set(true);
        watchDogThread = startWatchDog(driver, running);
    }

    public synchronized void stopAutomation() {
        running.set(false);
        if (watchDogThread != null && watchDogThread.isAlive()) {
            watchDogThread.interrupt();
        }
        watchDogThread = null;

        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception ignored) {
            }
            driver = null;
        }
    }

    private static WebDriver getWebDriver() {
        WebDriver localDriver = new ChromeDriver();
        localDriver.manage().timeouts().implicitlyWait(Duration.parse("PT10S"));
        return localDriver;
    }

    private static Thread startWatchDog(WebDriver localDriver, AtomicBoolean isRunning) {
        String mainWindow = localDriver.getWindowHandle();
        Thread watchDog = new Thread(() -> {
            while (isRunning.get()) {
                try {
                    Set<String> handles = localDriver.getWindowHandles();
                    for (String handle : handles) {
                        if (!handle.equals(mainWindow)) {
                            try {
                                localDriver.switchTo().window(handle);
                                localDriver.close();
                            } catch (Exception ignored) {
                            }
                        }
                    }
                    localDriver.switchTo().window(mainWindow);
                    Thread.sleep(300);
                } catch (InterruptedException interrupted) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception ignored) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException interrupted) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }, "watchdog-thread");
        watchDog.setDaemon(true);
        watchDog.start();
        return watchDog;
    }
}