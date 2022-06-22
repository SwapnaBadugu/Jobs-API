package jobsAPITesting;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import utils.AppConstants;
import static org.hamcrest.MatcherAssert.assertThat;

public class Jobs_Get {

	@Test
	void Should_Get_All_Jobs() {

		// Make the APIendpoint call and capture response into a variable.
		Response response = RestAssured.get(AppConstants.Const_JOBS_URI).then().extract().response();

		// read the response status code.
		int statusCode = response.getStatusCode();

		// read the response body. convert it to string format.
		String responsebody = response.getBody().asString();

		// Printing the responseBody and statusCode
		System.out.println("Job details: " + responsebody);
		System.out.println("Status Code: " + statusCode);

		// Validate the GetallAPIMethod
		Assert.assertEquals(statusCode, 200, "Response Received Successfully");

		Reporter.log("Response Received Successfully");
		assertThat("Json schema", responsebody.replaceAll("NaN", "null"),
				JsonSchemaValidator.matchesJsonSchemaInClasspath("Schema.json"));

		System.out.println("Json schema validation is Successful");

	}

	@Test
	void Should_Not_Get_All_Jobs() {

		// Make the APIendpoint call and capture response into a variable.
		Response response = RestAssured.given().when().get(AppConstants.Const_JOBS_URI + "/jobs123").then().log().all()
				.extract().response();

		// Make the APIendpoint call and capture response into a variable.
		int statuscode = response.getStatusCode();

		// Validate the GetallAPIMethod with invalid endpoint and printing the status
		// code
		Assert.assertEquals(statuscode, 404);
		System.out.println("The response code is" + statuscode);

		Reporter.log("Response recieved successfully");

	}

}
