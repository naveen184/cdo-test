package com.test.dataprovider;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.aventstack.extentreports.Status;
import com.test.files.interaction.GetExcelData;
import com.test.logging.Logging;
import com.test.reporting.Reporting;
import com.test.ui.actions.BaseSteps;
import com.test.utils.SystemProperties;

public class ExcelDataProvider {

	 private ExcelDataProvider() {
		    throw new IllegalStateException("Utility class");
		  }
	
    private static String getCellValue(int rowNum, int colNum, XSSFSheet excelSheet) {

        String myDataValue = null;
        Row myRow = excelSheet.getRow(rowNum);
        int intCellType = myRow.getCell(colNum).getCellType();

        try {
            if (intCellType == 0) {
                int myIntDataValue = (int) myRow.getCell(colNum).getNumericCellValue();
                myDataValue = Integer.toString(myIntDataValue);
            } else {
                myDataValue = myRow.getCell(colNum).getStringCellValue();
            }

            return myDataValue;
        }

        catch (RuntimeException e) {
        	Logging.logReporter(Status.DEBUG, "Exception Caught : " + e);
            return "";
        }
    }

    /***
     * For data provider, that wants to retrieve all the rows data and loop (DDB) Data Driven Approach
     */
    public static Object[][] getDataProviderArray(String strFilePath, String strSheetName) {

        Object[][] tabArray = new Object[0][0];

        try {
            // Call reusable methods to open input stream and excel sheet
            FileInputStream fis = GetExcelData.openInputStream(strFilePath);
            XSSFSheet excelSheet = GetExcelData.openExcelSheet(fis, strSheetName);

            int ci = 0;
            int cj = 0;
            int rowCount = excelSheet.getLastRowNum();
            Row myRow = excelSheet.getRow(0);
            int colCount = myRow.getLastCellNum();
            int baseColumnsNumber = 2; // Helper columns (2) ScenarioName and Environment
            tabArray = new String[rowCount][colCount - baseColumnsNumber]; // start with a two elements element resize will be done later

            
            Reporting.logReporter(Status.DEBUG, "tabArray size = " + tabArray.length);

            int qtyOfItemsFound = 0;
            int environmentCol = 1;
            boolean flagForItemsFound = false;


            Reporting.logReporter(Status.DEBUG, "\n" + "rowCount= " + rowCount);
            for (int i = 0; i < rowCount; i++) {
                cj = 0;

                if (flagForItemsFound) {
                    ci = ci + 1;
                }

                for (int j = 2; j < colCount; j++, cj++) { // INDEX START FROM 0, THE ACTUAL DATA STARTS FROM COLUMN "C" = 2

                    String excelRowEnvironment = getCellValue(i + 1, environmentCol, excelSheet);
                    if (SystemProperties.EXECUTION_ENVIRONMENT.equals(excelRowEnvironment)) {

                        tabArray[ci][cj] = getCellValue(i + 1, j, excelSheet);

                        flagForItemsFound = true;
                    } else {
                        flagForItemsFound = false;
                        break;
                    }
                }

                if (flagForItemsFound) { // Resize the array with the real space + 1 extra
                    qtyOfItemsFound = qtyOfItemsFound + 1;
                }
            }

            // Always remove the unused space
            fis.close();
            tabArray = (Object[][]) BaseSteps.WebElementUtils.resizeArray(tabArray, qtyOfItemsFound);

            Reporting.logReporter(Status.DEBUG, "qtyOfItemsFound size = " + qtyOfItemsFound);
        }

        catch (IOException e) {
        	 Reporting.logReporter(Status.DEBUG, "Could not read the Excel sheet");
        	 Logging.logReporter(Status.DEBUG, "Exception Caught : " + e);
            
        }
        Reporting.logReporter(Status.DEBUG, "returning tabArray size = " + tabArray.length);

        return tabArray;
    }

    /***
     * For data provider, that wants to retrieve all the values in a Map and loop (DDB) Data Driven Approach
     */
    public static Object[][] getDataProviderMapForMultipleRows(String strFilePath, String strSheetName) throws IOException {
        FileInputStream fis = GetExcelData.openInputStream(strFilePath);
        XSSFSheet excelSheet = GetExcelData.openExcelSheet(fis, strSheetName);
        Row myRow = excelSheet.getRow(0);

        int rowCount = excelSheet.getLastRowNum(); // HEADER ROW RETURNS 0 ALSO EMTPY FIELD
        int colCount = myRow.getLastCellNum();

        Object[][] tabArray = new Object[rowCount][1];

        Reporting.logReporter(Status.DEBUG, "obj size = " + tabArray.length);
        boolean flagForItemsFound = false;
        int qtyOfItemsFound = 0;
        int environmentCol = 1;
        int ci = 0;
        String testDataConcatenate = SystemProperties.EXECUTION_ENVIRONMENT;
        String excelDataConcatenate = null;

        Reporting.logReporter(Status.DEBUG, "Searching Elements: \" + testDataConcatenate + \" in a total of [\" + rowCount + \"]");
        for (int i = 0; i <= rowCount; i++) { // STARTS FROM ROW O(HEADER) SO WE NEED ONE MORE ROUND

            Map<Object, Object> dataMap = new HashMap<>();
            excelDataConcatenate = getCellValue(i, environmentCol, excelSheet);

            if (testDataConcatenate.equals(excelDataConcatenate)) {

                for (int j = 0; j < colCount; j++) {
                    String iterationKey = getCellValue(0, j, excelSheet);
                    String iterationValue = getCellValue(i, j, excelSheet);

                    dataMap.put(iterationKey, iterationValue);
                }

                flagForItemsFound = true;

            } else {
                flagForItemsFound = false;
            }

            if (flagForItemsFound) { // Resize the array with the real space + 1 extra
                qtyOfItemsFound = qtyOfItemsFound + 1;
                tabArray[ci][0] = dataMap;
                ci = ci + 1;
            }
        }

        tabArray = (Object[][]) BaseSteps.WebElementUtils.resizeArray(tabArray, qtyOfItemsFound);
        
        Reporting.logReporter(Status.DEBUG, "final obj size = " + tabArray.length);
        

        return tabArray;
    }

    /**
     * For Data Provider to get single row data - Will look/retrieve the FIRST match for the concatenation of TestMethodName_Environment - Will not loop only retrieve single value
     * according to the testcaseRow to recover
     */
    public static Object[][] getDataProviderMapForUniqueRow(String strFilePath, String strSheetName, int iTestCaseRow) throws IOException {
        FileInputStream fis = GetExcelData.openInputStream(strFilePath);
        XSSFSheet excelSheet = GetExcelData.openExcelSheet(fis, strSheetName);
        Row myRow = excelSheet.getRow(0);

        int rowCount = 1; // As we only want to retrieve a single row
        int colCount = myRow.getLastCellNum();

        Object[][] obj = new Object[rowCount][1];
        Map<Object, Object> dataMap = new HashMap<>();

        for (int i = 0; i < rowCount; i++) {

            for (int j = 0; j < colCount; j++) {
                String iterationKey = getCellValue(0, j, excelSheet);
                String iterationValue = getCellValue(iTestCaseRow, j, excelSheet);

                dataMap.put(iterationKey, iterationValue);
            }

            obj[i][0] = dataMap;
        }

        return obj;
    }

    /**
     * Retrieve the row number that holds the testMethodName_Environment concatenation
     */
    public static int getRowContainsConcatTestNameAndEnvironment(String strFilePath, String strSheetName, String sTestCaseName, int testNameColNum) throws IOException {
        FileInputStream fis = GetExcelData.openInputStream(strFilePath);
        XSSFSheet excelSheet = GetExcelData.openExcelSheet(fis, strSheetName);
        boolean flagForItemFound = false;
        int i;
        int rowCount = getRowUsed(excelSheet);

        String testDataConcatenate = sTestCaseName + "_" + SystemProperties.EXECUTION_ENVIRONMENT;
        String excelDataConcatenate = null;

        Reporting.logReporter(Status.DEBUG, "Searching Element: " + testDataConcatenate + " in a total of [" + rowCount + "]");
        
        for (i = 0; i <= rowCount; i++) {
            excelDataConcatenate = getCellValue(i, testNameColNum, excelSheet) + "_" + getCellValue(i, testNameColNum + 1, excelSheet);

            if (testDataConcatenate.equals(excelDataConcatenate)) {
                flagForItemFound = true;
                break;
            } 
        }
        fis.close();

        if (flagForItemFound) {
            return i;
        } else {
            throw new UnsupportedOperationException("Element TC_ENVIRONMENT [" + testDataConcatenate + "] not found in excel sheet - will use first element by default.");
        }

    }

    private static int getRowUsed(XSSFSheet excelSheet) {
        try {
            return excelSheet.getLastRowNum();
        }

        catch (RuntimeException e) {
        	
        	 Reporting.logReporter(Status.DEBUG, e.getMessage());
             

            throw (e);
        }
    }
}
