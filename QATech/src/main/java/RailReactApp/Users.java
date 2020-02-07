package RailReactApp;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;


import java.util.Date;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.Test;

public class Users {

	String baseUri = "https://showoff-rails-react-production.herokuapp.com/";
	String client_id = "277ef29692f9a70d511415dc60592daf4cf2c6f6552d3e1b769924b2f2e2e6fe";
	String client_secret = "d6106f26e8ff5b749a606a1fba557f44eb3dca8f48596847770beb9b643ea352";
	String token = "Bearer b530f9ad13a061b36aa342b255608e18960db09cfd977cab8c1de9a0f8226024";

	@Test
	public void CreateUser() {

		String b = "{ "+
				"\"client_id\": \""+client_id+"\", "+
				"\"client_secret\": \""+client_secret+"\", "+
				"\"user\": { "+
					"\"first_name\": \"new\", "+
					"\"last_name\": \"User1\", "+
					"\"password\": \"password\", "+
					"\"email\": \"newuser9@showoff.ie\", "+
					"\"image_url\": \"https://static.thenounproject.com/png/961-200.png\" "+
				"}"+
			"}";
		
		RestAssured.baseURI = baseUri;		
		given().
		header("Content-Type", "application/json").		
		body(b).	
		when().
		post("api/v1/users").
		then().assertThat().statusCode(200).and().		
		body("message", equalTo("Success"));
	}

	@Test
	public void ShowId() {
		RestAssured.baseURI = baseUri;		
		given().
		header("Authorization", "Bearer 58d0522437a51e08ffef821f3194947f201c8fe8695731aad866960b4e961ea6").
		when().
		get("api/v1/users/4").
		then().assertThat().statusCode(200).and().		
		body("message", equalTo("Success")).and().
		body("data.user.name", equalTo("G User B"));
	}
	
	@Test
	public void ChangePassword() {
		RestAssured.baseURI = baseUri;		
		given().
		header("Content-Type", "application/json").
		header("Authorization", "Bearer 58d0522437a51e08ffef821f3194947f201c8fe8695731aad866960b4e961ea6").	
		body("{ "+
				"\"user\" : { "+
				"\"current_password\" : \"password\", "+
				"\"new_password\" : \"password\" "+
			"} "+
		"}").
			when().
			post("api/v1/users/me/password").
			then().assertThat().statusCode(200).and().		
			body("message", equalTo("Success"));
		}

	@Test
	public void CheckEmail() {
		Response res = get(baseUri + "api/v1/users/email?email=test@showoff.ie&client_id=" + client_id
				+ "&client_secret=" + client_secret);
		int code = res.getStatusCode();

		Assert.assertEquals(code, 200);
	}
	
	@Test
	public void ResetPassword() {
		RestAssured.baseURI = baseUri;
		given().
		header("Content-Type", "application/json")
		.body("{ "+
			   "\"user\" : { "+
			       "\"email\" : \"michael@showoff.ie\" "+
			   "}, "+
			   "\"client_id\": \""+client_id+"\", "+
			   "\"client_secret\": \""+client_secret+"\" "+
			"}")
		.when().post("api/v1/users/reset_password").then().assertThat().statusCode(200).and()
		.body("message", equalTo("Password reset email sent to michael@showoff.ie. Please check your email address for further instructions."));
	}

}
