package org.example;

import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.File;

public class GetFullPageScreenShot {

    public static String capture(WebDriver driver, String screenShotName) throws Exception
    {
        Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);
        String dest = System.getProperty("user.dir") + "/ErrorScreenshots/" + screenShotName + ".png";
        ImageIO.write(screenshot.getImage(),"PNG",new File(dest));
        return dest;
    }
}
