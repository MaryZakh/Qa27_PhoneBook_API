package restassured;

import dto.ContactDto;
import dto.GetAllContactsDto;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class GetAllContactsTestsRA {
    String token="eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibWFyYUBnbWFpbC5jb20iLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTczMjI2NTIyNCwiaWF0IjoxNzMxNjY1MjI0fQ.38mA49-C2OpzEjb7L05N_eHE9mpKgqy_bCkibn1a4Fk";
    String endpoint ="contacts";

    @BeforeMethod
    public void preCondition() {
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com/";
        RestAssured.basePath = "v1";
    }

    @Test
    public void GetAllContactsSuccess(){
       GetAllContactsDto contactsDto = given()
                .header("Authorization",token)
                .when()
                .get(endpoint)
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response()
                .as(GetAllContactsDto.class);
        List<ContactDto>list = contactsDto.getContacts();
        for (ContactDto contact:list){
            System.out.println(contact.getId());
            System.out.println(contact.getEmail());
        }
    }
}
