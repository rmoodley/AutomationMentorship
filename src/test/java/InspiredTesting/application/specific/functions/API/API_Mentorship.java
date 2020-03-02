package InspiredTesting.application.specific.functions.API;

import InspiredTesting.generic.functions.DataFunctions;
import InspiredTesting.generic.functions.Reporting;
import InspiredTesting.generic.functions.UtilityFunctions;
import InspiredTesting.generic.functions.commonBase;
import org.apache.poi.ss.usermodel.Sheet;

//Rest Assured
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class API_Mentorship extends commonBase {

    //Declarations
    public String sBaseURI;
    public String sCity;

    public String reportMessage;
    public Boolean sStatus;

    public API_Mentorship(DataFunctions data, UtilityFunctions utils, Reporting repo)
    {
        this.data = data;
        this.utils = utils;
        this.repo = repo;
    }

    public void GetWeatherDetails(Sheet sheet, int i, String filePath) throws Exception
    {
        //Gets sURL using the URL column in Excel
        sBaseURI = data.getCellData("BaseURI",i,sheet,null,null,"Excel");

        //SET the REST API Endpoint or Specify the base URL to RESTful web service
        RestAssured.baseURI = sBaseURI;

        //Get the RequestSpecification of the request that you want to sent
        //to the server. The server is specified by the BaseURI that we have
        //specified in the above step
        // Create an HTTP Request for this API
        RequestSpecification httpRequest = RestAssured.given();

        //Gets sURL using the URL column in Excel
        sCity = data.getCellData("City",i,sheet,null,null,"Excel");

        //Make a request to the server by specifying the method Type and the method URL
        //This will return the Response from the server. Store the response in a variable.
        //Lets call the API now
        Response response = httpRequest.request(Method.GET,"/"+sCity);

        //Get Response Code
        int statusCode = response.getStatusCode();
        String sStatusCode = Integer.toString(statusCode);

        //Updates the Excel spreadsheet ResponseCode column
        repo.WriteExcelResponseCode(sStatusCode, i, sDefaultPath+filePath, data);


        //Now let us print the body of the message to see what response
        //we have received from the server
        String responseBody = response.getBody().asString();

        //Updates the Excel spreadsheet ResponseBody column
        repo.WriteExcelResponseBody(responseBody , i, sDefaultPath+filePath, data);

        //Set Step report message
        reportMessage = "Get Weather Details Status Code";

        //Report on Step status to ExtentReport
        sStatus = repo.ExtentLogPassFailAPI(statusCode, reportMessage +" - Successful", reportMessage +" - Unsuccessful", utils);

        //Checks Step status
        utils.CheckStatus(sStatus);
    }
}
