package utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//import com.sun.xml.bind.v2.schemagen.xmlschema.List;

import jobsApiTestingEntities.JobsData;

//import utilities.AppConstants;

public class ExcelJobsUtils {
	public static FileInputStream excelFile;
	public static XSSFWorkbook excelWbook;
	public static XSSFSheet excelWSheet;
	public static XSSFCell cell;
	public static XSSFRow row;

	// This method is to set the File path and to open the Excel file
	// , Pass Excel Path and Sheetname as Arguments to this method

	public static void setexcelFileInfo(String excel_File, String sheetname) throws Exception {

		// Open Excel File
		excelFile = new FileInputStream(excel_File);

		// Open Excel Workbook
		excelWbook = new XSSFWorkbook(excelFile);

		// Access the required test data sheet
		excelWSheet = excelWbook.getSheet(sheetname);
		// System.out.println(AppConstants.excel_Path + " " +
		// AppConstants.excel_FileName + " " + sheetname);
	}

	public static List<JobsData> getDataFromExcelSheet(String excel_File, String sheetname) throws Exception {
		try {
			setexcelFileInfo(excel_File, sheetname);

			int rowCount = getRowCount();

			List<JobsData> jobsdataList = new ArrayList<JobsData>();
			JobsData jobObject;
			for (int row = 1; row <= rowCount; row++) {
				jobObject = new JobsData();
				jobObject.JobId = getCellData(row, 0);
				jobObject.JobTitle = getCellData(row, 1);
				jobObject.JobLocation = getCellData(row, 2);
				jobObject.JobCompanyName = getCellData(row, 3);
				jobObject.JobType = getCellData(row, 4);
				jobObject.JobPostedTime = getCellData(row, 5);
				jobObject.JobDescription = getCellData(row, 6);

				jobsdataList.add(jobObject);
			}
			return jobsdataList;
		} catch (Exception e) {
			return new ArrayList<JobsData>();
		} finally {
			if (excelWbook != null)
				excelWbook.close();
			if (excelFile != null)
				excelFile.close();
		}
	}

	public static String[] getDataFromExcelSheetForDelete(String excel_File, String sheetname) throws IOException {
		try {
			setexcelFileInfo(excel_File, sheetname);

			int rowCount = getRowCount();

			String[] jobData = new String[rowCount];
			for (int i = 1; i <= rowCount; i++) {
				jobData[i - 1] = getCellData(i, 0);
			}
			return jobData;
		} catch (Exception e) {
			return new String[0];
		} finally {
			if (excelWbook != null)
				excelWbook.close();
			if (excelFile != null)
				excelFile.close();
		}

	}

	// This method is to read the test data from the Excel cell, in this we are
	// passing parameters as Row num and Col num
	private static int getRowCount() throws Exception {
		try {
			int rowCount = excelWSheet.getLastRowNum();
			return rowCount;
		} catch (Exception e) {
			return 0;
		}
	}

	public static String getCellData(int RowNum, int ColNum) throws Exception {
		try {
			cell = excelWSheet.getRow(RowNum).getCell(ColNum);
			String CellData;
			// if (ColNum == 0) {
			// CellData = cell.getRawValue();
			// double id = Cell.getNumericCellValue();
			// CellData = String.valueOf(id);
			// } else
			// CellData = cell.getStringCellValue();
			DataFormatter formatter = new DataFormatter();
			CellData = formatter.formatCellValue(cell);

			return CellData;
		} catch (Exception e) {
			return "";
		}
	}

	// This method is to write in the Excel cell, Row num and Col num are the
	// parameters
	public static void setCellData(Integer RowNum, int ColNum, String Result) throws Exception {
		try {
			row = excelWSheet.getRow(RowNum);
			cell = row.getCell(ColNum, MissingCellPolicy.RETURN_BLANK_AS_NULL);
			if (cell == null) {
				cell = row.createCell(ColNum);
				cell.setCellValue(Result);
			} else {
				cell.setCellValue(Result);
			}

			// Constant variables Test Data path and Test Data file name
			FileOutputStream fileOut = new FileOutputStream(AppConstants.excel_File);
			excelWbook.write(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (Exception e) {
			throw (e);

		} finally {
			if (excelWbook != null)
				excelWbook.close();
			if (excelFile != null)
				excelFile.close();
		}
	}

}
