package restassured;

import dto.ContactDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DeleteContactByIdRA {

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibWFyYUBnbWFpbC5jb20iLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTczMjI2NTIyNCwiaWF0IjoxNzMxNjY1MjI0fQ.38mA49-C2OpzEjb7L05N_eHE9mpKgqy_bCkibn1a4Fk";
    String endpoint_add = "contacts";
    String id;


    @BeforeMethod
    public void preCondition() {
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com/";
        RestAssured.basePath = "v1";
int i = new Random().nextInt(1000)+1000;
        ContactDto contactDto = ContactDto.builder()
                .name("Maya")
                .lastName("Dow")
                .address("Haifa")
                .email("maya"+i+"@gmail.com")
                .phone("1234556"+i)
                .description("Maya Dow")
                .build();
        //get id from "message" : "Contact was added! ID: 47ee5012-839f-42b5-905a-9ec7cba66995"
        String message = given()
                .body(contactDto)
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when()
                .post(endpoint_add)
                .then()
                .assertThat().statusCode(200)
                .extract()
                .path("message");
        String[] all = message.split(": ");
        id = all[1];


    }

    @Test
    public void deleteContactByIdSuccess(){
        given()
                .header("Authorization",token)
                .when()
                .delete("contacts/"+id)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message",equalTo("Contact was deleted!"));

    }

    @Test
    public void deleteContactByIdWrongToken(){
        given()
                .header("Authorization","kdfgdg35")
                .when()
                .delete("contacts/"+id)
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("error",equalTo("Unauthorized"));

    }

}
