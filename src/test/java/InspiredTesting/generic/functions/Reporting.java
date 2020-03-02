package InspiredTesting.generic.functions;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.aventstack.extentreports.markuputils.Markup;
import com.codoid.products.exception.FilloException;
import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.Status;

//import com.relevantcodes.extentreports.ExtentReports;
//import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public class Reporting
{
    public static ExtentHtmlReporter htmlReporter;
    public static ExtentReports extent;
    public static ExtentTest extentTest;
    
    public String timeStamp;

    //public static ExtentTest logger;

    public Boolean sStatus;

    public ExtentReports getExtent()
    {
        return extent;
    }

    public void setExtent(ExtentReports extentTest)
    {
        extent = extentTest;
    }

    public ExtentTest getExtentTest()
    {
        return extentTest;
    }

    public void setExtentTest(ExtentTest extentTest)
    {
        Reporting.extentTest = extentTest;
    }

    public ExtentReports initializeExtentReports(String sReportName, String sAppend, UtilityFunctions utils)
    {
        if (sAppend.toUpperCase().equals("TRUE"))
        {
            htmlReporter = new ExtentHtmlReporter("report/"+sReportName+".html");
            htmlReporter.setAppendExisting(true);
        }
        else
        {
            htmlReporter = new ExtentHtmlReporter("report/"+sReportName+utils.getCurrentTimeStamp()+".html");
        }
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        return extent;
    }

    public void ExtentLogPassDB(String sMessagePass, ExtentTest logger) throws Exception
    {
        logger.pass(sMessagePass);
    }

    public void ExtentLogInfoDB(String sMessagePass, ExtentTest logger) throws Exception
    {
        logger.info(sMessagePass);
    }

    public Boolean ExtentLogPassFail(String xPath, String sMessagePass, String sMessageFail, Boolean Screenshot, UtilityFunctions utils) throws Exception
    {
        if (utils.checkIfObjectIsDisplayed(xPath))
        {
            ExtentLogPass( sMessagePass, Screenshot, utils);
            sStatus = true;
        }
        else
        {
            ExtentLogFail( sMessageFail, Screenshot,utils);
            sStatus = false;
        }
        return sStatus;
    }

    public void ExtentLogPass( String sMessagePass, Boolean Screenshot, UtilityFunctions utils) throws Exception
    {
        if (Screenshot)
        {
            String fileName=takeScreenShot("ExtentLogPass", utils);
            getExtentTest().pass(sMessagePass, MediaEntityBuilder.createScreenCaptureFromPath(fileName).build());
        }
        else
        {
            getExtentTest().pass(sMessagePass);
        }
    }

    public void ExtentLogFail( String sMessageFail, Boolean Screenshot, UtilityFunctions utils) throws Exception
    {
        if (Screenshot)
        {
            String fileName=takeScreenShot("ExtentLogFail", utils);
            getExtentTest().fail(sMessageFail, MediaEntityBuilder.createScreenCaptureFromPath(fileName).build());
        }
        else
        {
            getExtentTest().fail(sMessageFail);
        }
    }

    public void ExtentLogPassAPI( String sMessagePass, UtilityFunctions utils) throws Exception
    {
            getExtentTest().pass(sMessagePass);
    }

    public void ExtentLogFailAPI( String sMessageFail, UtilityFunctions utils) throws Exception
    {
            getExtentTest().fail(sMessageFail);
    }

    public void ExtentLogInfo( String sMessageInfo, Boolean Screenshot, UtilityFunctions utils) throws Exception
    {
        if (Screenshot)
        {
            String fileName=takeScreenShot("ExtentLogFail", utils);
            getExtentTest().info(sMessageInfo, MediaEntityBuilder.createScreenCaptureFromPath(fileName).build());
            extentTest.addScreenCaptureFromPath(fileName);
        }
        else
        {
            getExtentTest().info(sMessageInfo);
        }
    }

    public Boolean ExtentLogPassFailAPI(int sStatusCode, String sMessagePass, String sMessageFail, UtilityFunctions utils) throws Exception
    {
        if (utils.CompareStatus(sStatusCode))
        {
            ExtentLogPassAPI(sMessagePass, utils);
            sStatus = true;
        }
        else
        {
            ExtentLogFailAPI(sMessageFail, utils);
            sStatus = false;
        }
        return sStatus;
    }

    public String takeScreenShot(String FileName, UtilityFunctions utils) throws Exception
    {
        String fileName="Empty";
        try
        {
            String sDefaultPath = System.getProperty("user.dir");
            sDefaultPath = sDefaultPath.replace("batch", "");
            File scrFile = ((TakesScreenshot)utils.getWebdriver()).getScreenshotAs(OutputType.FILE);

            sDefaultPath = sDefaultPath.replace("/","\\");

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            fileName =sDefaultPath+"\\screenshots\\"+FileName+timeStamp+".png";

            FileUtils.copyFile(scrFile, new File(fileName));
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
        return fileName;
    }

    public void WriteExcelTestStatus(String sStatus, int i, String filePath, DataFunctions data) throws SQLException, InvalidFormatException, FilloException, IOException
    {

        String[] Column = {"Status"};
        
        //Get Current Timestamp
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

        switch(sStatus)
        {
            case "Failed" :
                String[] Data0 = {"Failed_"+timeStamp};
                data.writeData(Column, i, Data0, filePath, data.getDataType(), null);
                break;
            case "Passed" :
                String[] Data1 = {"Passed_"+timeStamp};
                data.writeData(Column, i, Data1, filePath, data.getDataType(), null);
                break;
        }
    }

    public void WriteExcelResponseCode(String sResponseCode, int i, String filePath, DataFunctions data) throws SQLException, InvalidFormatException, FilloException, IOException
    {
        String[] Column = {"ResponseCode"};

        String[] Data = {sResponseCode};
        data.writeData(Column, i, Data, filePath, data.getDataType(), null);
    }

    public void WriteExcelResponseBody(String sResponseBody, int i, String filePath, DataFunctions data) throws SQLException, InvalidFormatException, FilloException, IOException
    {
        String[] Column = {"ResponseBody"};

        String[] Data = {sResponseBody};
        data.writeData(Column, i, Data, filePath, data.getDataType(), null);
    }
}
