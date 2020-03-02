package InspiredTesting.generic.functions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

public class DataFunctions
{
    private String sWebURL;
    private String sUserID;
    private String sPassword;

    private String sTestMURL;
    private String sTestManagement;
    private String sTestMUsername;
    private String sTestMPassword;
    private String sDomain;
    private String sProject;

    private String sDBHost;
    private String sDBUsername;
    private String sDBPassword;

    private String sReportName;
    private String sAppendReport;

    private String sDataLocation;
    private String sDataType;

    private String sBrowser;
    private String sBrowserDrivers;

    private String sApi;

    public Connection connect;
    public java.sql.Connection conn = null;
    public Sheet sheet;
    public Workbook workbook;

    int fillonum = 1;

    ArrayList<String> lines = new ArrayList<String>();

    public String getWebURL()
    {
        return sWebURL;
    }
    public String getWebUserName()
    {
        return sUserID;
    }
    public String getWebPassword()
    {
        return sPassword;
    }

    public String getTestMURL()
    {
        return sTestMURL;
    }
    public String getTestManagement()
    {
        return sTestManagement;
    }
    public String getTestMUsername()
    {
        return sTestMUsername;
    }
    public String getTestMPassword()
    {
        return sTestMPassword;
    }
    public String getDomain()
    {
        return sDomain;
    }
    public String getProject()
    {
        return sProject;
    }

    public String getDBHost()
    {
        return sDBHost;
    }
    public String getDBUsername()
    {
        return sDBUsername;
    }
    public String getDBPassword()
    {
        return sDBPassword;
    }

    public String getReportName()
    {
        return sReportName;
    }
    public String getAppendReport()
    {
        return sAppendReport;
    }

    public String getDataLocation()
    {
        return sDataLocation;
    }
    public String getDataType()
    {
        return sDataType;
    }

    public String getBrowser()
    {
        return sBrowser;
    }
    public String getBrowserDrivers()
    {
        return sBrowserDrivers;
    }

    public String getAPI()
    {
        return sApi;
    }

    /*****************************************************************************
     Function Name: GetEnvironmentVariables
     Description:	Gets environment variables from the config json file
     Date Created:	13/02/2020
    *******************************************************************************/
    public void GetEnvironmentVariables() throws IOException, ParseException
    {
        File f1=null;
        FileReader fr=null;

        JSONParser parser = new JSONParser();
        try {
            f1 = new File("config/Environment.json");
            fr = new FileReader(f1);
            Object obj;
            obj = parser.parse(fr);
            JSONObject jsonObject = (JSONObject) obj;

            sWebURL = (String) jsonObject.get("weburl");
            sUserID = (String) jsonObject.get("webusername");
            sPassword = (String) jsonObject.get("webpassword");

            sTestMURL = (String) jsonObject.get("testurl");
            sTestManagement = (String) jsonObject.get("testmanagement");
            sTestMUsername = (String) jsonObject.get("testmanagememntusername");
            sTestMPassword = (String) jsonObject.get("testmanagmentpassword");
            sDomain = (String) jsonObject.get("domain");
            sProject = (String) jsonObject.get("project");

            sDBHost = (String) jsonObject.get("dbhost");
            sDBUsername = (String) jsonObject.get("dbusername");
            sDBPassword = (String) jsonObject.get("dbpass");

            sReportName = (String) jsonObject.get("reportname");
            sAppendReport = (String) jsonObject.get("appendreport");

            sDataLocation = (String) jsonObject.get("datalocation");
            sDataType = (String) jsonObject.get("datatype");

            sBrowser = (String) jsonObject.get("browser");
            sBrowserDrivers = (String) jsonObject.get("browserdrivers");

            sApi = (String) jsonObject.get("API");
        }
        finally
        {
            try
            {
                fr.close();
            }
            catch(IOException ioe)
            {
                ioe.printStackTrace();
            }
        }
    }

    //Read and Write data from/To a TextFile
    public String ReadTextFile(String filePath)throws IOException
    {
        BufferedReader input = null;
        FileReader file = new FileReader(filePath);
        input = new BufferedReader(file);

        String value = input.readLine();
        input.close();

        return value;
    }

    public void WriteTextFile(String filePath, String outputData)throws IOException
    {
        Writer output = null;
        File file = new File(filePath);
        output = new BufferedWriter(new FileWriter(file));

        output.write(outputData);
        //output.write("\r\n");
        output.close();
    }

    public Sheet ReadExcel(String FILE_NAME, String strSheetName) throws IOException
    {
        FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
        workbook = new XSSFWorkbook(excelFile);
        sheet = workbook.getSheet(strSheetName);
        return sheet;
    }

    public Recordset ConnectFillo(String path, String Query) throws FilloException
    {
        Fillo fillo=new Fillo();

        Recordset record;

        connect=fillo.getConnection(path);
        record=connect.executeQuery(Query);
        return record;
    }

    public String getCellData(String strColumn, int iRow, Sheet sheet, Recordset record,ResultSet resultset, String Type ) throws Exception
    {
        String sValue = null;
        switch (Type.toUpperCase())
        {
            case "EXCEL":
                Row row = sheet.getRow(0);
                for (int i =0;i<columnCount(sheet);i++)
                {
                    if (row.getCell(i).getStringCellValue().trim().equals(strColumn))
                    {
                        Row raw = sheet.getRow(iRow);
                        Cell cell = raw.getCell(i);
                        DataFormatter formatter = new DataFormatter();
                        sValue = formatter.formatCellValue(cell);
                        break;
                    }
                }
                break;

            case "SQLSERVER":
                if (iRow == fillonum)
                {
                    if(resultset.next())
                    {
                        fillonum = iRow+1;
                        sValue=resultset.getString(strColumn);
                    }
                }
                else
                {
                    sValue=resultset.getString(strColumn);
                }
                break;
        }
        return sValue;
    }

    public int rowCount(Sheet sheet, Recordset record, ResultSet resultset, String Type) throws Exception
    {
        int count = 0;
        switch (Type.toUpperCase())
        {
            case "EXCEL":
                count = sheet.getPhysicalNumberOfRows();
                break;
            case "SQLSERVER":
                int i = 0;
                while (resultset.next())
                {
                    i++;
                }
                count = i;
        }
        return count;
    }

    public int columnCount(Sheet sheet) throws Exception
    {
        return sheet.getRow(0).getLastCellNum();
    }

    public void writeData(String[] sColumn,int Row,String[] sData,String filepath,  String Type, String sQuery) throws IOException, InvalidFormatException, FilloException, SQLException
    {
        switch (Type.toUpperCase())
        {
            case "EXCEL":
                int CoulmnNo = 0 ;
                filepath = filepath.replace("/","\\");
                FileInputStream file = new FileInputStream(filepath);
                Workbook wb = WorkbookFactory.create(file);
                sheet = wb.getSheetAt(0);

                org.apache.poi.ss.usermodel.Cell cell = null;
                Row row = sheet.getRow(0);

                for (int c = 0;c<sColumn.length; c++)
                {
                    for( int i=0; i<row.getLastCellNum();i++)
                    {
                        if(row.getCell(i).getStringCellValue().trim().equals(sColumn[c]))
                        {
                            CoulmnNo=i;
                            Row raw = sheet.getRow(Row);
                            cell = raw.createCell(CoulmnNo);
                            cell.setCellValue(sData[c]);
                            break;
                        }
                    }
                }

                FileOutputStream fileOut = new FileOutputStream(filepath);
                wb.write(fileOut);
                fileOut.close();

                break;

            case "SQLSERVER":
                Statement st = conn.createStatement();
                st.execute(sQuery);
        }
    }

    public ResultSet ConnectAndQuerySQLServer(String sDBURL, String sUserName,String  sPassword,  String sQuery)
    {
        ResultSet rs = null;

        try
        {
            String dbURL = sDBURL;
            String user = sUserName;
            String pass = sPassword;
            conn = DriverManager.getConnection(dbURL, user, pass);
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sQuery);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return rs;
    }

    public long generateRandom()
    {
        return (long)(Math.random()*100000 + 3333300000L);
    }

    public String updateData(String[] ColumnValue, String[] SearchValue,String xmlPath) throws IOException
    {
        String sBody = null;
        FileInputStream inputStream = new FileInputStream(xmlPath);
        try
        {
            for(int i=0; i<SearchValue.length; i++)
            {
                if (sBody.contains(SearchValue[i]))
                {
                    sBody = sBody.replace(SearchValue[i], ColumnValue[i]);
                }
            }
        }
        finally
        {
            inputStream.close();
        }
        return sBody;
    }

    public String ReadDataJson(String xmlPath) throws IOException, ParseException
    {
        File f1=null;
        FileReader fr=null;

        JSONParser parser = new JSONParser();
        try
        {
            f1 = new File(xmlPath);
            fr = new FileReader(f1);
            Object obj = parser.parse(fr);
            JSONObject jsonObject = (JSONObject) obj;
            String sBody = jsonObject.toString();
            return sBody;
        }
        finally
        {
            try
            {
                lines.clear();
                fr.close();
            }
            catch(IOException ioe)
            {
                ioe.printStackTrace();
            }
        }
    }

    public String ReadData(String xmlPath) throws IOException
    {
        String sBody = null;
        FileInputStream inputStream = new FileInputStream(xmlPath);
        try
        {
        }
        finally
        {
            inputStream.close();
        }
        return sBody;
    }

    public String validateData(String[] data,String outputData, int icount, String sPath, String format, String sRunStatus, Reporting repo) throws Exception
    {
        String sOutput = null;

        if (format.equals("JSON")) {

            for(int v = 0; v < data.length; v++)
            {
                if (data[v].contains("NotNull"))
                {
                    String [] dataHold =data[v].split("_");
                    String [] hold =outputData.split(",");
                    for (int l = 0; l < hold.length;l++)
                    {
                        if(hold[l].contains(dataHold[0]))
                        {
                            String[]dataCheckNotNulll = hold[l].split(":");
                            if(dataCheckNotNulll[1].trim().isEmpty()||dataCheckNotNulll[1].trim().equals(null) )
                            {
                                if (sOutput== null)
                                {
                                    sOutput =  data[v];
                                }
                                else {
                                    sOutput = sOutput + "," + data[v];
                                }
                            }
                        }
                    }

                }

                else if(outputData.contains(data[v]) ==false)
                {
                    if (sOutput == null)
                    {
                        sOutput =  data[v];
                    }
                    else
                    {
                        sOutput = sOutput + "," +data[v];
                    }
                }
            }
        }
        else
        {
            for (int n = 1; n < data.length;n++)
            {
                String [] dataValue =data[n].split("_");

                String sData =xmlParser(outputData, data[0], dataValue[0]);

                if(sData.equals(dataValue[1]) == false)
                {
                    if (sOutput == null)
                    {
                        sOutput =dataValue[0];
                    }
                    else
                    {
                        sOutput =sOutput + "," + dataValue[0];
                    }
                }
            }
        }

        String[]sColumn={"ValidationStatus","ValidationResults"};

        if(sOutput ==null)
        {
            sRunStatus = sRunStatus+ " Passed";
            String[]sData3={"Passed","Validation Successful"};
            writeData(sColumn, icount, sData3, sPath,"Excel",null);
        }
        else
        {
            sRunStatus = sRunStatus+ " Failed";
            String[]sData3={"Failed", "Validation failed for "+ sOutput};
            writeData(sColumn, icount, sData3, sPath, "Excel", null);
        }
        return sRunStatus;
    }

    public String xmlParser(String outputData, String tagName, String fieldName) throws SAXException, IOException, ParserConfigurationException
    {
        String element = null;
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(outputData));
        org.w3c.dom.Document doc = dBuilder.parse(is);

        doc.getDocumentElement().normalize();

        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

        NodeList nList = doc.getElementsByTagName(tagName);

        System.out.println("----------------------------");

        for (int temp = 0; temp < nList.getLength(); temp++)
        {
            Node nNode = nList.item(temp);

            System.out.println("\nCurrent Element :" + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE)
            {
                Element eElement = (Element) nNode;

                element = eElement.getElementsByTagName(fieldName).item(0).getTextContent();
            }
        }
        return element;
    }
}
