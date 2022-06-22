package jobsAPITesting;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.AppConstants;
import utils.ExcelJobsUtils;

public class Jobs_Delete {
	@DataProvider(name = "jobdataProvider")
	public String[] Should_Delete_Job_Data() throws Exception {
		String[] testObject = ExcelJobsUtils.getDataFromExcelSheetForDelete(AppConstants.excel_File,
				AppConstants.excel_DELETE_SheetName);
		return testObject;
	}

	@Test(dataProvider = "jobdataProvider")
	void Should_Pass_Delete_Job(String JobID) {

		// Make the APIendpoint call and capture response into a variable.
		Response response = RestAssured.given().queryParam("Job Id", JobID).delete(AppConstants.Const_JOBS_URI).then()
				.extract().response();
		;

		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 200, "Request Deleted successfully");
	}

	@Test
	void Should_Fail_Delete_Job() {

		// Make the APIendpoint call and capture response into a variable.
		Response response = RestAssured.given().queryParam("Job Id", "0").delete(AppConstants.Const_JOBS_URI);

		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 404, "Job Exists");

	}

}
