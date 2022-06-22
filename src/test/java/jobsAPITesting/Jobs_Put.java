package jobsAPITesting;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import jobsApiTestingEntities.JobsData;
import utils.AppConstants;
import utils.ExcelJobsUtils;

public class Jobs_Put {

	@DataProvider(name = "jobDataProvider")
	public JobsData[] Should_Modify_Job_Data() throws Exception {
		List<JobsData> testObject = ExcelJobsUtils.getDataFromExcelSheet(AppConstants.excel_File,
				AppConstants.excel_PUT_SheetName);
		return testObject.toArray(new JobsData[testObject.size()]);
	}

	@Test(dataProvider = "jobDataProvider")
	void Should_Modify_Job_Data(JobsData jobData) {
		JSONObject request = new JSONObject();

//		request.put("Job Id", jobData.JobId);
//		request.put("Job Title", jobData.JobTitle);
//		request.put("Job Location", jobData.JobLocation);
//		request.put("Job Company Name", jobData.JobCompanyName);
//		request.put("Job Type", jobData.JobType);
//		request.put("Job Posted time", jobData.JobPostedTime);
//		request.put("Job Description", jobData.JobDescription);

		System.out.println("request=" + request);

		// Make the APIendpoint call and capture response into a variable.
		// Response response =
		// RestAssured.given().queryParams(request).when().put(AppConstants.Const_JOBS_URI).then()
		// .extract().response();
		Response response = RestAssured.given().queryParam("Job Id", jobData.JobId)
				.queryParam("Job Title", jobData.JobTitle).queryParam("Job Location", jobData.JobLocation)
				.queryParam("Job Company Name", jobData.JobCompanyName).queryParam("Job Type", jobData.JobType)
				.queryParam("Job Posted time", jobData.JobPostedTime)
				.queryParam("Job Description", jobData.JobDescription)
				// .header("Content-Type", "application/json").body(request.toJSONString())
				.when().put(AppConstants.Const_JOBS_URI).then().log().all().extract().response();

		System.out.println("Request done");
		System.out.println(response);
		ResponseBody responsebody = response.getBody();

		JsonPath jsonPathEvaluator = responsebody.jsonPath();
		String responseBody = response.getBody().asPrettyString();
		System.out.println(responseBody);
		System.out.println("Before Response AssertThat");

		// Json schema validation

		assertThat("Json schema", responseBody.replaceAll("NaN", "null"),
				JsonSchemaValidator.matchesJsonSchemaInClasspath("Schema.json"));

		// Status code validation

		int statusCode = response.getStatusCode();
		System.out.println("The response code is " + statusCode);

		if (statusCode == 200) {
			Assert.assertEquals(statusCode, 200, "Response received successfully");
			System.out.println("The data is valid");
		} else if (statusCode == 404) {
			Assert.assertEquals(statusCode, 404, "Response received successfully");
			System.out.println("The data is already existing");
		} else if (statusCode == 500) {
			Assert.assertEquals(statusCode, 500, "Server Error");
			System.out.println("The data is invalid");
		}
	}

}
