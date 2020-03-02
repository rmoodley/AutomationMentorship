package InspiredTesting.generic.functions;

import InspiredTesting.application.specific.functions.API.API_Mentorship;
import InspiredTesting.application.specific.functions.GUI.GUI_Mentorship;

import com.codoid.products.fillo.Recordset;

import java.sql.ResultSet;

import org.apache.poi.ss.usermodel.Sheet;

public abstract class commonBase
{
    protected UtilityFunctions utils = new UtilityFunctions();
    protected Reporting repo = new Reporting();
    protected DataFunctions data = new DataFunctions();

    protected API_Mentorship api1;
    protected GUI_Mentorship app0;

    protected int iRow;
    protected Sheet sheet;
    protected Recordset record;
    protected ResultSet resultset;
    protected String sDataType;

    public static String sDefaultPath;

    protected void initialiseFunctions()
    {
        api1 = new API_Mentorship(data, utils, repo);
        app0 = new GUI_Mentorship(data, utils, repo);
    }

    public void setup(String Location)
    {
        try
        {
            sDefaultPath = System.getProperty("user.dir");
            sDefaultPath = sDefaultPath.replace("batch", "");
            data.GetEnvironmentVariables();
            sDataType = data.getDataType();
            repo.setExtent(repo.initializeExtentReports(data.getReportName(), data.getAppendReport(), utils));

            switch (data.getDataType().toUpperCase())
            {
                case "EXCEL": sheet = data.ReadExcel(sDefaultPath+Location,"Sheet1");
                    iRow = data.rowCount(sheet, record, resultset, sDataType)-1;
                    break;

                case "SQLSERVER":resultset = data.ConnectAndQuerySQLServer(data.getDBHost(), data.getDBUsername(),data.getDBPassword(), "Select * from  [BookFlights].[dbo].[BookFlights]");
                    iRow = data.rowCount(sheet, record, resultset, sDataType);
                    resultset = data.ConnectAndQuerySQLServer(data.getDBHost(), data.getDBUsername(),data.getDBPassword(), "Select * from  [BookFlights].[dbo].[BookFlights]");
            }
            utils.setWebDriver(utils.initializeWedriver(data.getCellData("Browser",1,sheet,null,null,sDataType), null,  null));
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void setupAPI(String Location)
    {
        try
        {
            sDefaultPath = System.getProperty("user.dir");
            sDefaultPath = sDefaultPath.replace("batch", "");
            data.GetEnvironmentVariables();
            sDataType = data.getDataType();
            repo.setExtent(repo.initializeExtentReports(data.getReportName(), data.getAppendReport(), utils));

            switch (data.getDataType().toUpperCase())
            {
                case "EXCEL": sheet = data.ReadExcel(sDefaultPath+Location,"Sheet1");
                    iRow = data.rowCount(sheet, record, resultset, sDataType)-1;
                    break;

                case "SQLSERVER":resultset = data.ConnectAndQuerySQLServer(data.getDBHost(), data.getDBUsername(),data.getDBPassword(), "Select * from  [BookFlights].[dbo].[BookFlights]");
                    iRow = data.rowCount(sheet, record, resultset, sDataType);
                    resultset = data.ConnectAndQuerySQLServer(data.getDBHost(), data.getDBUsername(),data.getDBPassword(), "Select * from  [BookFlights].[dbo].[BookFlights]");
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void Collapse()
    {
        if (utils.getWebdriver()!= null)
            utils.getWebdriver().quit();
    }
}
