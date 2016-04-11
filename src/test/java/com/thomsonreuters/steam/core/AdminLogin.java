package com.thomsonreuters.steam.core;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.path.xml.XmlPath.*;
import java.util.Map;

import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.path.xml.element.Node;
import com.jayway.restassured.specification.RequestSpecification;

import static com.jayway.restassured.specification.ProxySpecification.host;

public class AdminLogin extends SteamAbstractBase{
	public static String login(){

		//baseURI = "http://10.226.32.121:5000/esti/xrpc";
		baseURI = "http://10.205.140.204:5000/esti/xrpc";
		String UserName="Neon_Test_Admin@tr.com";
		String Password="1234qwer$";
		String response ;

		String myEnvelope = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"+
				" <request xmlns=\"http://www.isinet.com/xrpc40\">"+
				" <fn name=\"STEAMAdminAPI.loginAdmin\">"+
				"<list>"+
				" <val name=\"Username\">"+UserName+"</val>"+
				"<val name=\"Password\">"+Password+"</val>"+
				"<map name=\"clientInfo\">"+
				" <val name=\"COMPONENT\">ESTI</val>"+
				"<val name=\"VERSION\">1</val>"+
				"<val name=\"ENVIRONMENT\">steam</val>"+
				"</map>"+
				"</list>"+
				"</fn>"+
				"</request>";

		try{
			
			// local is Y <=> this block to be executed locally 
			if(local.equalsIgnoreCase("Y")){
				response = given().body( myEnvelope ).when().post(baseURI).thenReturn().asString();
			}else{//execute in Jenkins with proxy setup
				response = given().proxy(host("squid.oneplatform.build").withPort(3128)).body( myEnvelope ).when().post( baseURI ).thenReturn().asString();
			}
			
			Node fnResponse = with(response).get("response.fn[0]");
			Map hm=(Map) fnResponse.attributes();
			String rc = (hm.get("rc")).toString();
			if(rc.equalsIgnoreCase("ok")){
				SID = with(response).get("response.fn.val");
			}
		}catch(Exception ex){
			logger.info("====== Exception in getting SID ===== "+ex.getMessage());
			ex.printStackTrace();
		}
		
		return SID;
	}
}
