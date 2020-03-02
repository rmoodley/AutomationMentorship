package InspiredTesting.application.specific.functions.GUI;

 import InspiredTesting.generic.functions.DataFunctions;
 import InspiredTesting.generic.functions.Reporting;
 import InspiredTesting.generic.functions.UtilityFunctions;
 import InspiredTesting.generic.functions.commonBase;
 import org.apache.poi.ss.usermodel.Sheet;

public class GUI_Mentorship extends commonBase {

    public String reportMessage;
    public Boolean sStatus;

    public String sURL;
    public String sSearchItem;
    public String sSortBySelection;
    public String sItemsPerPageSelection;

    public GUI_Mentorship(DataFunctions data, UtilityFunctions utils, Reporting repo)
    {
        this.data = data;
        this.utils = utils;
        this.repo = repo;
    }

    public void NavigateToURL(Sheet sheet, int i) throws Exception
    {
        //Gets sURL using the URL column in Excel
        sURL = data.getCellData("URL",i,sheet, null,null, data.getDataType());

        //Navigates to URL
        utils.navigate(sURL);

        //Set Step report message
        reportMessage = "Step 1 - Navigate To URL";

        //Report on Step status to ExtentReport
        sStatus = repo.ExtentLogPassFail("//*[@id='search_query_top']", reportMessage +" - Successful", reportMessage +" - Unsuccessful", false,utils);

        //Checks Step status
        utils.CheckStatus(sStatus);
    }

    public void SearchForItem(Sheet sheet, int i) throws Exception
    {
        //Gets sSearchItem using the SearchItem column in Excel
        sSearchItem = data.getCellData("SearchItem",i,sheet, null,null, data.getDataType());

        //Enter text for item, in search field
        utils.EnterText("//*[@id='search_query_top']",sSearchItem);

        //Click on the search icon
        utils.ClickObject("//*[@id='searchbox']/button");

        //Set Step report message
        reportMessage = "Step 2 - Search for Item";

        //Report on Step status to ExtentReport
        sStatus = repo.ExtentLogPassFail("//*[@id='center_column']/ul/li[1]/div/div[1]/div/a[1]/img", reportMessage +" - Successful", reportMessage +" - Unsuccessful", false,utils);

        //Checks Step status
        utils.CheckStatus(sStatus);
    }

    public void SortBySelection(Sheet sheet, int i) throws Exception
    {
        //Gets sSortBySelection using the SortBy column in Excel
        sSortBySelection = data.getCellData("SortBy",i,sheet, null,null, data.getDataType());

        //Select Sort By
        utils.SelectDropDown("//*[@id=\"selectProductSort\"]","VisibleText",sSortBySelection);

        //Set Step report message
        reportMessage = "Step 3 - Sort By Selection";

        //Report on Step status to ExtentReport
        sStatus = repo.ExtentLogPassFail("//*[@id='center_column']/ul/li[1]/div/div[1]/div/a[1]/img", reportMessage +" - Successful", reportMessage +" - Unsuccessful", false,utils);

        //Checks Step status
        utils.CheckStatus(sStatus);
    }

    public void ItemsPerPageSelection(Sheet sheet, int i) throws Exception
    {
        //Gets sItemsPerPageSelection using the ItemsPerPage column in Excel
        sItemsPerPageSelection = data.getCellData("ItemsPerPage",i,sheet, null,null, data.getDataType());

        //Select Items per Page
        utils.SelectDropDown("//*[@id=\"nb_item\"]","Value",sItemsPerPageSelection);

        //Set Step report message
        reportMessage = "Step 4 - Items per Page Selection";

        //Report on Step status to ExtentReport
        sStatus = repo.ExtentLogPassFail("//*[@id='center_column']/ul/li[1]/div/div[1]/div/a[1]/img", reportMessage +" - Successful", reportMessage +" - Unsuccessful", false,utils);

        //Checks Step status
        utils.CheckStatus(sStatus);
    }

    public void ClickFirstItem() throws Exception
    {
        //Click first item
        utils.ClickObject("//*[@id='center_column']/ul/li[1]/div/div[1]/div/a[1]/img");

        //Set Step report message
        reportMessage = "Step 5 - Click First Item";

        //Checks if Add Cart button is displayed
    	if (utils.checkIfObjectIsDisplayed("//*[@id=\"add_to_cart\"]/button/span"))
    	{
    		//Report on Step status to ExtentReport
            sStatus = repo.ExtentLogPassFail("//*[@id=\"add_to_cart\"]/button/span", reportMessage +" - Successful", reportMessage +" - Unsuccessful", false,utils);
    	}
    	else
    	{
    		//Report on Step status to ExtentReport
            sStatus = repo.ExtentLogPassFail("//*[@id='search_query_top']", reportMessage +" - Successful - No Stock", reportMessage +" - Unsuccessful", false,utils);
    	}
        
        //Checks Step status
        utils.CheckStatus(sStatus);
    }

    public void AddToCart() throws Exception
    {
    	//Checks if Add Cart button is displayed
    	if (utils.checkIfObjectIsDisplayed("//*[@id=\"add_to_cart\"]/button/span"))
    	{
            //Add to Cart
            utils.ClickObject("//*[@id=\"add_to_cart\"]/button/span");
    	}
        //Checks if the Low stock prompt pop up appears
        else if (utils.checkIfObjectIsDisplayed("//*[@id=\"product_confirmation_modal\"]/div[2]/button[1]"))
        {
            //Add to Cart
            utils.ClickObject("//*[@id=\"product_confirmation_modal\"]/div[2]/button[1]");
        }

        //Set Step report message
        reportMessage = "Step 6 - Add to Cart";

        //Report on Step status to ExtentReport
        sStatus = repo.ExtentLogPassFail("//*[@id='search_query_top']", reportMessage +" - Successful", reportMessage +" - Unsuccessful", false,utils);

        //Checks Step status
        utils.CheckStatus(sStatus);
    }

    public void ContinueShopping() throws Exception
    {
    	//Checks if Continue button is displayed
    	if (utils.checkIfObjectIsDisplayed("//*[@id=\"layer_cart\"]/div[1]/div[2]/div[4]/span/span"))
    	{
        	//Click Continue Shopping
            utils.ClickObject("//*[@id=\"layer_cart\"]/div[1]/div[2]/div[4]/span/span");
    	}

        //Set Step report message
        reportMessage = "Step 7 - Continue Shopping";

        //Report on Step status to ExtentReport
        sStatus = repo.ExtentLogPassFail("//*[@id='search_query_top']", reportMessage +" - Successful", reportMessage +" - Unsuccessful", false,utils);

        //Checks Step status
        utils.CheckStatus(sStatus);
    }
}
