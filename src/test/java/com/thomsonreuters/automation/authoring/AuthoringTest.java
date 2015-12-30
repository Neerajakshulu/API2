package com.thomsonreuters.automation.authoring;

import org.apache.commons.lang.StringUtils;
import org.testng.annotations.Test;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.relevantcodes.extentreports.LogStatus;
import com.thomsonreuters.automation.common.AbstractBase;
import com.thomsonreuters.automation.common.RowData;

/**
 * Test the Authoring APIs
 */
public class AuthoringTest extends AbstractBase {

	@Test
	public void authoringTest() throws Exception {
		testDataExcelPath = "src/test/test-data/AuthoringTestData.xlsx";
		appName = "1PAUTHORING";
		Thread.sleep(20000);
		runTests();
	}
	
	
	/*comments size which should be 2500 characters*/
	@Test(priority = 1)
	public void testCommentMaxLength() throws Exception {
		logger.info("Entered Authoring testCommentMaxLength method...");
		appName = "1PAUTHORING";
		rowData = new RowData();
		rowData.setTestName("OPQA-682");
		rowData.setHost("1PAUTHORING");
		rowData.setDescription("Verify that create comment with Max length and verify comment count");
		rowData.setApiPath("/comments");
		rowData.setMethod("POST");
		rowData.setQueryString("");
		rowData.setHeaders("X-1P-User=(SYS_USER1)||Content-Type=application/json");
		rowData.setBody(	
				"{\"targetType\":\"wos\",\"targetId\":\"468387744WOS1\",\"content\":\"Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length Comment test with max length\"}");
		rowData.setStore("comments.id");
		testReporter=reporter.startTest(rowData.getTestName(), rowData.getDescription()).assignCategory(appName);
		ExecuteTest(rowData);
		reporter.endTest(testReporter);
		logger.info("Ennding Authoring testCommentMaxLength method...");
	}
	

	private void ExecuteTest(RowData rowData) throws Exception {
		Response response = null;
		String sheetName = "Authoring";
		String responseJson = null;
		String statusCode = null;
		boolean testSuccess = false;

		logger.debug("row data=" + rowData.toString());
		logger.debug("Real host=" + appHosts.get(rowData.getHost()));
		/*
		 * If mandatory information like test case name, host, api path and
		 * valid http method are not provided then skip those tests and update
		 * the status as fail.
		 */
		if (StringUtils.isNotBlank(rowData.getTestName()) && StringUtils.isNotBlank(rowData.getHost())
				&& StringUtils.isNotBlank(rowData.getApiPath()) && isSupportedMethod(rowData.getMethod())) {
			// If any of the dependency test failed then don't proceed.
			if (isDependencyTestsPassed(rowData.getDependencyTests())) {
				response=getAPIResponce();
				
				responseJson = response.asString();
				statusCode = String.valueOf(response.getStatusCode());
				// Validate the response with expected data
//				testSuccess = testSummaryMaxLengthvalidateResponse(rowData.getValidations(), responseJson, statusCode);
				if(statusCode.equals("200")){
				JsonPath jsonPath = new JsonPath(responseJson);
				String content = jsonPath.getString("comments.content");
				int oldCount = Integer.parseInt(dataStore.get("OPQA-344_1_counterValue"));
				int newCount =Integer.parseInt(jsonPath.getString("size"));
				if ((content.substring(1, content.length()-1)).length() == 2500 && newCount == oldCount+1 ) {
					logger.info("Comment content was truncated to 2500 characters");
					logger.info("Comment size was increased ");
					testReporter.log(LogStatus.INFO, "Comment content was truncated to 2500 characters");
					testReporter.log(LogStatus.INFO, "Comment size was increased");
					testSuccess = true;
				}
				}
				// Save API response to file
				saveAPIResponse(responseJson, sheetName, rowData.getTestName());

				if (!testSuccess) {
					testReporter.log(LogStatus.FAIL, "FAIL");
					throw new Exception("Validation Failed");
				}else{
					storeDependentTestsData(responseJson, rowData.getStore(), rowData.getTestName());
					testReporter.log(LogStatus.PASS, "PASS" );
				}

				logger.info("End execution of test:" + rowData.getTestName());
				logger.info("-----------------------------------------------------------------------");
			}
		}
	}
}
	
	
	
	

