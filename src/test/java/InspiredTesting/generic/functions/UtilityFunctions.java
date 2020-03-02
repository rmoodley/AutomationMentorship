package InspiredTesting.generic.functions;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class UtilityFunctions
{
    private String processName;
    public WebDriver driver;
    public String sStatus;

    public WebDriver getWebdriver()
    {
        return driver;
    }

    public void setWebDriver(WebDriver DriverTest)
    {
        driver = DriverTest;
    }

    public WebDriver initializeWedriver(String sdriverName, String strURL, DesiredCapabilities capabilities)
    {
        try
        {
            switch (sdriverName.toUpperCase())
            {
                case "CHROME":
                    try
                    {
                        System.setProperty("webdriver.chrome.driver","drivers/chromedriver.exe");
                        driver = new ChromeDriver();
                    }
                    catch(Exception e)
                    {
                        System.out.print("not a Mac machine");
                        System.setProperty("webdriver.chrome.driver","drivers/chromedriver.exe");
                        ChromeOptions options = new ChromeOptions();
                        options.addArguments("start-maximized");
                        driver = new ChromeDriver(options);
                    }
                    break;

                case "FIREFOX":
                    System.setProperty("webdriver.firefox.marionette","drivers/geckodriver.exe");
                    driver =  new FirefoxDriver();
                    break;

                case "IE":
                    System.setProperty("webdriver.ie.driver","drivers/IEDriverServer.exe");
                    driver = new InternetExplorerDriver();
                    break;
            }
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        }
        catch(Exception e)
        {
            System.out.print(e.getMessage());
        }
        return driver;
    }

    /*******************************************************************************General Function Area***********************************************************************/
    public void WindowsProcess(String processName)
    {
        this.processName = processName;
    }

    public void CloseRunningProcess() throws Exception
    {
        if (isRunning())
        {
            getRuntime().exec("taskkill /F /IM " + processName);
        }
    }

    private boolean isRunning() throws Exception
    {
        Process listTasksProcess = getRuntime().exec("tasklist");
        BufferedReader tasksListReader = new BufferedReader(
                new InputStreamReader(listTasksProcess.getInputStream()));

        String tasksLine;

        while ((tasksLine = tasksListReader.readLine()) != null)
        {
            if (tasksLine.contains(processName))
            {
                return true;
            }
        }
        return false;
    }

    private Runtime getRuntime()
    {
        return Runtime.getRuntime();
    }

    public void navigate( String baseUrl)
    {
        driver.get(baseUrl);
    }

    public String getCurrentTimeStamp()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return sdf.format(timestamp);
    }

    /********************************************************************Selenium Area************************************************************************

     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException */

    public void WaitForElement(String xPath, int intWait) throws SAXException, IOException, ParserConfigurationException
    {
        try
        {
            WebDriverWait wait;
            wait = new WebDriverWait(driver,intWait);
            wait.until(visibilityOfAllElementsLocatedBy(By.xpath(xPath)));
        }
        catch(Exception e)
        {
            System.out.println("Element "+xPath+ " NOT found.");
        }
    }

    /*****************************************************************************
     Function Name: ClickObject
     Description:	Click an object in an application using xpath and maximum wait time
     Date Created:	13/02/2020
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     ******************************************************************************/
    public void ClickObject(String xPath) throws SAXException, IOException, ParserConfigurationException
    {
        driver.findElement(By.xpath(xPath)).click();
    }

    /*****************************************************************************
     Function Name: EnterText
     Description:	Enter text to the application using xpath and maximum wait time
     Date Created:	13/02/2020
     ******************************************************************************/
    public void EnterText(String xPath, String sText) throws SAXException, IOException, ParserConfigurationException
    {
        driver.findElement(By.xpath(xPath)).sendKeys(sText);
    }

    /*****************************************************************************
     Function Name: SelectDropDown
     Description:	Select Dropdown to the application using xpath and maximum wait time
     Date Created:	13/02/2020
     ******************************************************************************/
    public void SelectDropDown(String xPath, String sType, String sChoice) throws SAXException, IOException, ParserConfigurationException
    {
        Select dropDown = new Select(driver.findElement(By.xpath(xPath)));
        try
        {
            switch (sType.toUpperCase())
            {
                case "INDEX":
                    int iChoice = Integer.parseInt(sChoice);
                    dropDown.selectByIndex(iChoice);
                    break;

                case "VALUE":
                    dropDown.selectByValue(sChoice);
                    break;

                case "VISIBLETEXT":
                    dropDown.selectByVisibleText(sChoice);
                    break;
            }
        }
        catch(Exception e)
        {
            System.out.print(e.getMessage());
        }
        driver.findElement(By.xpath(xPath)).sendKeys(sChoice);
    }

    /*****************************************************************************
     Function Name: WaitforProperty
     Description:	Wait for the property to appear using xpath and maximum wait time
     Date Created:	13/02/2020
     ******************************************************************************/
    public void waitforProperty(String xPath, int sWait) throws SAXException, IOException, ParserConfigurationException
    {
        WebDriverWait wait = new WebDriverWait(driver,sWait);

        //Get object properties from xpath
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath)));
    }

    //Generate Random String
    public String GenerateRandomString(int count)
    {
        return RandomStringUtils.randomAlphabetic(count).toLowerCase();
    }

    //Generate Random Integer
    public String GenerateRandomInt(int count)
    {
        return RandomStringUtils.randomNumeric(count);
    }

    //Generate Random Alpha Numeric
    public String GenerateAlphaNumeric(int count)
    {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    /*****************************************************************************
     Function Name: GetText
     Description:	Get text from the application using xpath
     Date Created:	13/02/2020
     ******************************************************************************/
    public String GetText(String xPath) throws SAXException, IOException, ParserConfigurationException
    {
        String strTextToReturn = null;

        //Get object properties from xpath
        strTextToReturn = driver.findElement(By.xpath(xPath)).getText();
        return strTextToReturn;
    }

    /*****************************************************************************
     Function Name: ClearObject
     Description:	Clear object on the application using xpath
     Date Created:	13/02/2020
     ******************************************************************************/
    public void ClearObject(String xPath) throws SAXException, IOException, ParserConfigurationException
    {
        driver.findElement(By.xpath(xPath)).clear();
    }

    /*****************************************************************************
     Function Name: CheckIfObjectExists
     Description:	Checks if an object exists using xpath
     Date Created:	13/02/2020
     ******************************************************************************/
    public boolean checkIfObjectExists(String xPath)
    {
        boolean exists = false;
        try
        {
            exists = (driver.findElement(By.xpath(xPath)) != null) || (driver.findElements(By.xpath(xPath)).isEmpty());
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            exists=false;
        }
        return exists;
    }

    /*****************************************************************************
     Function Name: CheckIfObjectIsDisplayed
     Description:	Checks if an object is displayed using xpath
     Date Created:	13/02/2020
     ******************************************************************************/
    public boolean checkIfObjectIsDisplayed(String xPath)
    {
        boolean exists = false;
        try
        {
            exists = driver.findElement(By.xpath(xPath)).isDisplayed() == true;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            exists=false;
        }
        return exists;
    }

    /*****************************************************************************
     Function Name: CompareStatus
     Description:	Compares Status
     Date Created:	13/02/2020
     ******************************************************************************/
    public boolean CompareStatus(int sStatusCode)
    {
        boolean status = false;
        try
        {
            if (sStatusCode==200)
            {
                status = true;
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            status=false;
        }
        return status;
    }

    /************************************************************End Selenium***************************************************************************************/

    /***************************************************************Robot*******************************************************************************************/

    //Press Shift and Tab
    public void PressEnter(int iteration) throws InterruptedException, AWTException
    {
        int i=1;
        while(i<=iteration)
        {
            Thread.sleep(1000);
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            i++;
        }
    }

    //Press Down Key on a page
    public void PressDownKey() throws InterruptedException, AWTException
    {
        Thread.sleep(5000);
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_PAGE_DOWN);
        robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
    }

    //Press Down Key on a page
    public void PressUpKey() throws InterruptedException, AWTException
    {
        Thread.sleep(5000);
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_PAGE_UP);
        robot.keyRelease(KeyEvent.VK_PAGE_UP);
    }

    //Press Down Key on a page
    public void RefreshPage() throws InterruptedException, AWTException
    {
        Thread.sleep(5000);
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_F5);
        robot.keyRelease(KeyEvent.VK_F5);
    }

    //Press Shift and Tab
    public void PressShiftTab(int iteration) throws InterruptedException, AWTException
    {
        int i=1;
        while(i<=iteration)
        {
            Thread.sleep(1000);
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_SHIFT);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_SHIFT);
            robot.keyRelease(KeyEvent.VK_TAB);
            i++;
        }
    }

    //Press Shift and Tab
    public void PressLeftArrow(int iteration) throws InterruptedException, AWTException
    {
        int i=1;
        while(i<=iteration)
        {
            Thread.sleep(1000);
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_LEFT);
            robot.keyRelease(KeyEvent.VK_LEFT);
            i++;
        }
    }

    public void pressTAB()throws AWTException, InterruptedException
    {
        Robot r = new Robot();
        r.keyPress(KeyEvent.VK_TAB);
        r.keyRelease(KeyEvent.VK_TAB);
        Thread.sleep(1000);
    }

    public void pressF2()throws AWTException, InterruptedException
    {
        Robot r = new Robot();
        r.keyPress(KeyEvent.VK_F2);
        r.keyRelease(KeyEvent.VK_F2);
        Thread.sleep(1000);
    }

    public void pressA()throws AWTException, InterruptedException
    {
        Robot r = new Robot();
        r.keyPress(KeyEvent.VK_A);
        r.keyRelease(KeyEvent.VK_A);
        Thread.sleep(1000);
    }

    public void pressTAB(int iterations)throws AWTException, InterruptedException
    {
        int i=1;
        while(i<=iterations)
        {
            Robot r = new Robot();
            r.keyPress(KeyEvent.VK_TAB);
            r.keyRelease(KeyEvent.VK_TAB);
            Thread.sleep(1000);
            i++;
        }
    }

    //Press Down Key on a page
    public void pressCtrlShiftA() throws InterruptedException, AWTException
    {
        Thread.sleep(5000);
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_SHIFT);
        robot.keyPress(KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_SHIFT);
        robot.keyRelease(KeyEvent.VK_A);
    }

    /*****************************************************************End Robot*************************************************************************************/

    public String[] xmlParser(String xmlPath, String tagName) throws SAXException, IOException, ParserConfigurationException
    {
        String[] element2 = new String[2];
        File fXmlFile = new File(xmlPath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        Document doc = dBuilder.parse(fXmlFile);

        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName(tagName);

        for (int temp = 0; temp < nList.getLength(); temp++)
        {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE)
            {
                Element eElement = (Element) nNode;

                String element = eElement.getElementsByTagName("identifier").item(0).getTextContent();
                String element1 = eElement.getElementsByTagName("Element").item(0).getTextContent();
                element2[0] = element;
                element2[1] = element1;
            }
        }
        return element2;
    }

    public void VerifyElement(String xPath, int intWait) throws Exception
    {
        WebDriverWait wait;
        wait = new WebDriverWait(driver, intWait);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath)));
    }

    public void VerifyElementNotDisplayed( String xPath, int intWait) throws Exception
    {
        WebDriverWait wait = new WebDriverWait(driver, intWait);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xPath)));
    }

    public void CheckStatus(Boolean StepStatus) throws Exception {
        if(!StepStatus)
        {
            throw new Exception("Object not found");
        }
    }
}