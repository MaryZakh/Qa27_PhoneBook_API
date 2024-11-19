package restassured;


import dto.AuthRequestDto;
import dto.AuthResponseDto;
import dto.ErrorDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;


public class LoginTestsRA {

    String endpoint ="user/login/usernamepassword";
    @BeforeMethod
    public void preCondition() {
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com/";
        RestAssured.basePath = "v1";
    }

    @Test
    public void loginSuccess() {
        AuthRequestDto authRequestDto = AuthRequestDto.builder()
                .username("mara@gmail.com")
                .password("Mmar123456$")
                .build();

       AuthResponseDto responseDto = given()
                .body(authRequestDto)
                .contentType("application/json")
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response()
                .as(AuthResponseDto.class);
        System.out.println(responseDto.getToken());

    }

    @Test
    public void loginWrongEmail(){
        AuthRequestDto authRequestDto = AuthRequestDto.builder()
                .username("maragmail.com")
                .password("Mmar123456$")
                .build();

      ErrorDto errorDto =  given()
                .body(authRequestDto)
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(401)
                .extract()
                .response()
                .as(ErrorDto.class);
        Assert.assertEquals(errorDto.getMessage(),"Login or Password incorrect");

    }

    @Test
    public void loginWrongEmailFormat(){
        AuthRequestDto authRequestDto = AuthRequestDto.builder()
                .username("maragmail.com")
                .password("Mmar123456$")
                .build();

       given()
                .body(authRequestDto)
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("message",containsString("Login or Password incorrect"))
                .assertThat().body("path",equalTo("/v1/user/login/usernamepassword"));

    }
}
