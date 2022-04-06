package com.test.ui.actions;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.KeyboardSimulatorType;
import org.openqa.selenium.winium.WiniumDriver;
import org.openqa.selenium.winium.WiniumDriverService;

public class WebDriverUtils {

	// instance of singleton class
	private static WebDriverUtils driverUtils=null;
	private WiniumDriver windriver=null;
	private WiniumDriverService service=null;
    private WebDriver driver;

    // Constructor
    private WebDriverUtils(){
    	System.setProperty("webdriver.ie.driver","src" + File.separator + "main" + File.separator + "resources" + File.separator + "drivers" + File.separator + "IEDriverServer.exe");
		driver= new InternetExplorerDriver();
		
    }

    // TO create instance of class
    public static WebDriverUtils getInstanceOfWebDriverUtils(){
        if(driverUtils==null){
        	driverUtils = new WebDriverUtils();
        }
        return driverUtils;
    }
    
    
    // To get driver
    public WebDriver getDriver()
    {
    	return driver;
    }
    
    public WiniumDriver getWiniumDriver() throws IOException
    
       {

    	DesktopOptions option = new DesktopOptions();
    	option.setDebugConnectToRunningApp(true);
    	option.setKeyboardSimulator(KeyboardSimulatorType.BasedOnWindowsFormsSendKeysClass);
    	
		File driverpath = new File("src/main/resources/tools/Winium.Desktop.Driver.exe");
		System.setProperty("webdriver.winium.desktop.driver","src/main/resources/tools/Winium.Desktop.Driver.exe");
		service = new WiniumDriverService.Builder().usingDriverExecutable(driverpath).usingPort(9999)
				.withVerbose(true).withSilent(false).buildDesktopService();
		//	service.start();
	//	windriver = new WiniumDriver(new URL ("http://localhost:9999"), option);
		windriver = new WiniumDriver(service, option);
		return windriver;
		
		
		}
    
    public static void quitServerProcess() throws IOException, InterruptedException 
    
    {
		try
		
		{
			Process process = Runtime.getRuntime().exec("taskkill /F /IM Winium.Desktop.Driver.exe");
			Thread.sleep(500);
			process.waitFor();
			process.destroy();
			Thread.sleep(500);
		} 
		catch (InterruptedException e)
		
		{
			e.printStackTrace();
		}
		
	}
    
  
    
    private static void startServer()
    
    {
		try
		
		{
			Runtime runTime = Runtime.getRuntime();
			runTime.exec("src\\main\\resources\\tools\\Winium.Desktop.Driver.exe");
			Thread.sleep(3000);

		} catch (IOException e)
		
		{
			e.printStackTrace();
		} catch (InterruptedException e)
		
		{
			// TODO: handle exception
		}
	}
    
    
}