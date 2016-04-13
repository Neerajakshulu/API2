/**
* The ProfileTest program is an entry point for running Profile API test cases.
* This class initializes app name, excel file path which are utilized by AbstractBase class.  
*
* @author  Janardhan
* @version 1.0
* @since   2015-08-31 
*/
package com.thomsonreuters.automation.profile;

import org.apache.commons.lang.StringUtils;
import org.testng.annotations.Test;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.relevantcodes.extentreports.LogStatus;
import com.thomsonreuters.automation.common.AbstractBase;
import com.thomsonreuters.automation.common.RowData;


public class ProfileTest extends AbstractBase {

	/**
	 * This method is entry point for testing. Initializes excel file path and app name. 
	 * Calls runTests method for executing test cases specified in the excel file.  
	 * 
	 * @return 		Nothing
	 * @throws 		Exception
	 * 
	 */
	@Test(priority = 1)
	public void profileTest() throws Exception {
		testDataExcelPath = "src/test/test-data/ProfileTestData.xlsx";
		appName = "1PPROFILE";
		runTests();
	}

	/**
	 * This method is entry point for testing on priority.  
	 * This method tests for profile summary length = 1500 or not and updates test status.
	 * 
	 * @return 		Nothing
	 * @throws 		Exception
	 * 
	 */
	@Test
	public void testSummaryMaxLength() throws Exception {
		logger.info("Entered Profile testSummaryMaxLength method...");
		appName = "1PPROFILE";
		rowData = new RowData();
		rowData.setTestName("OPQA-497_1");
		rowData.setHost("1PPROFILE");
		rowData.setDescription(
				"Verify that update user profile summary with exceeds Max length and verify that API should truncate to 1500 characters");
		rowData.setApiPath("/users/user/(SYS_USER1)");
		rowData.setMethod("PUT");
		rowData.setHeaders("X-1P-User=(SYS_USER1)||Content-Type=application/json");
		rowData.setBody(
				"{\"summary\":\"Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with   Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length Summary test with max length\"}");
		rowData.setValidations("status=200||hits.hits._source.firstName=Mohana");
		rowData.setStore("hits.hits._id");
		rowData.setQueryString("");
		testReporter = reporter.startTest(rowData.getTestName(), rowData.getDescription()).assignCategory(appName);
		ExecuteTest(rowData);
		reporter.endTest(testReporter);
		logger.info("Entered Profile testSummaryMaxLength method...");
	}

	private void ExecuteTest(RowData rowData) throws Exception {
		Response response = null;
		String sheetName = "Profile";
		String responseJson = null;
		String statusCode = null;
		boolean testSuccess = false;
		logger.debug("row data=" + rowData.toString());
		logger.debug("Real host=" + appHosts.get(rowData.getHost()));
		/*
		 * If mandatory information like test case name, host, api path and valid http method are not provided then skip
		 * those tests and update the status as fail.
		 */
		if (StringUtils.isNotBlank(rowData.getTestName()) && StringUtils.isNotBlank(rowData.getHost())
				&& StringUtils.isNotBlank(rowData.getApiPath()) && isSupportedMethod(rowData.getMethod())) {
			// If any of the dependency test failed then don't proceed.
			if (isDependencyTestsPassed(rowData.getDependencyTests())) {
				logger.info("-----------------------------------------------------------------------");
				logger.info("Starting test:" + rowData.getTestName());
				response = getAPIResponce();
				responseJson = response.asString();
				statusCode = String.valueOf(response.getStatusCode());
				// Validate the response with expected data
				// testSuccess = testSummaryMaxLengthvalidateResponse(rowData.getValidations(), responseJson,
				// statusCode);
				if (statusCode.equals("200")) {
					JsonPath jsonPath = new JsonPath(responseJson);
					String summary = jsonPath.getString("summary");
					if (summary.length() == 1500) {
						logger.info("summary text was truncated to 1500 characters");
						testReporter.log(LogStatus.INFO, "summary text was truncated to 1500 characters");
						testSuccess = true;
					}
				}
				// Save API response to file
				saveAPIResponse(responseJson, sheetName, rowData.getTestName());
				if (!testSuccess) {
					testReporter.log(LogStatus.FAIL, "FAIL");
					throw new Exception("Validation Failed");
				} else {
					testReporter.log(LogStatus.PASS, "PASS");
				}

				logger.info("End execution of test:" + rowData.getTestName());
				logger.info("-----------------------------------------------------------------------");
			}
		}
	}
}
