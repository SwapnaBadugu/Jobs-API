package jobsAPITesting;

import org.testng.annotations.Test;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import jobsApiTestingEntities.JobsData;
import utils.AppConstants;
import utils.ExcelJobsUtils;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

//https://www.liquid-technologies.com/online-json-to-schema-converter
public class Jobs_Post {

	@DataProvider(name = "jobdataProvider")
	public JobsData[] Should_Post_Job_Data() throws Exception {
		List<JobsData> testObject = ExcelJobsUtils.getDataFromExcelSheet(AppConstants.excel_File,
				AppConstants.excel_POST_SheetName);
		return testObject.toArray(new JobsData[testObject.size()]);
	}

	@Test(dataProvider = "jobdataProvider")
	void Should_Post_New_Jobs(JobsData jobData) throws Exception {

		JSONObject request = new JSONObject();

		request.put("Job Id", jobData.JobId);
		request.put("Job Title", jobData.JobTitle);
		request.put("Job Location", jobData.JobLocation);
		request.put("Job Company Name", jobData.JobCompanyName);
		request.put("Job Type", jobData.JobType);
		request.put("Job Posted time", jobData.JobPostedTime);
		request.put("Job Description", jobData.JobDescription);

		System.out.println("request=" + request);

		

		// Make the APIendpoint call and capture response into a variable.
		Response response = RestAssured.given().header("Content-Type", "application/json").body(request.toJSONString())
				.when().post(AppConstants.Const_JOBS_URI).then().log().all().extract().response();
		ResponseBody responsebody = response.getBody();

		JsonPath jsonPathEvaluator = responsebody.jsonPath();
		String responseBody = response.getBody().asPrettyString();
		System.out.println(responseBody);
		// Json schema validation

		assertThat("Json schema", 
				responseBody.replaceAll("NaN", "null"),
				JsonSchemaValidator.matchesJsonSchemaInClasspath("Schema.json"));

		System.out.println("JSON Schema Validation is successful");

		// Status code validation

		int statusCode = response.getStatusCode();
		System.out.println("The response code is " + statusCode);

		if (statusCode == 200) {
			Assert.assertEquals(statusCode, 200, "Response received successfully");
			System.out.println("The data is valid");
		} else if (statusCode == 409) {
			Assert.assertEquals(statusCode, 409, "Response received successfully");
			System.out.println("The data is already existing");

		} else if (statusCode == 500) {
			Assert.assertEquals(statusCode, 500, "Response received successfully");
			System.out.println("The data is invalid");
		}
	}

}
