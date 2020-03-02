package InspiredTesting.api.tests;

import InspiredTesting.generic.functions.commonBase;

//TestNG
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

//JUnit
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.Assert;

public class Test_API extends commonBase
{
    //Declarations
    public String filePath;
    public String sStatus;

    public static String sDefaultPath;

    @BeforeClass
    //@Before
    public void beforeMethod() throws Exception
    {
        try
        {
            //Sets file path to Excel Spreadsheet
            filePath = "/TestData/API/TestAPI.xlsx";

            //Initialize Spreadsheet and WebDriver
            setupAPI(filePath);
        }
        catch(Exception e)
        {
            //Outputs error message
            System.out.println("Unable to initilize the driver");
        }
    }

    @Test
    public void TestAPI() throws Exception
    {
        //Sets intWait to 2 Seconds
        int intWait = 2000;

        try
        {
            //Initialize Function Library
            initialiseFunctions();

            //Get the total number of rows from the Excel Spreadsheet (excluding headers)
            iRow = data.rowCount(sheet, record, resultset, data.getDataType())-1;

            //Get Framework file path
            sDefaultPath = System.getProperty("user.dir");
            sDefaultPath = sDefaultPath.replace("batch", "");

            //Run a For-Do loop, starting at 1, which will run until the end of the Excel Spreadsheet
            for (int i = 1 ;i<=iRow;i++)
            {
                //Set sStatus to null for the iteration
                sStatus = null;

                //Create an extent test
                repo.setExtentTest(repo.getExtent().createTest(data.getCellData("Scenario",i,sheet, null,null, sDataType)));

                try
                {
                    api1.GetWeatherDetails(sheet, i, filePath);
                    
                    //Sets Test Status to Passed
                    sStatus = "Passed";

                    //Updates the Excel spreadsheet Status column to Pass
                    repo.WriteExcelTestStatus(sStatus, i, sDefaultPath+filePath, data);
                }
                catch(Exception e)
                {
                    //Sets Test Status to Failed
                    sStatus = "Failed";

                    //Updates the Excel spreadsheet Status column to Failed
                    repo.WriteExcelTestStatus(sStatus, i, sDefaultPath+filePath, data);

                    //Updates the Extent Report with Exception Error message
                    repo.getExtentTest().fail(e);

                    //Outputs Exception error message to the test run
                    System.out.print(e.getMessage());

                    //Continues the Test run
                    continue;
                }
            }
            if (sStatus.contains("Failed"))
            {
                //Sends an Assert fail to the Test Framework
                Assert.fail();
            }
        }
        catch(Exception e)
        {
            //Outputs Exception error message to the report
            repo.getExtentTest().fail(e);

            //Outputs Exception error message to the test run
            System.out.print(e.getMessage());
        }
    }

    @AfterMethod
    //@After
    public void afterMethod() throws Exception
    {
        //Close the Extent Report
        repo.getExtent().flush();

        //Quit WebDriver
        Collapse();
    }
}