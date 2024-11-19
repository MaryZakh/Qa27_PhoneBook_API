package okhttp;

import com.google.gson.Gson;
import dto.ContactDto;
import dto.ErrorDto;
import dto.MessageDto;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class DeleteContactByIDOkhttp {
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibWFyYUBnbWFpbC5jb20iLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTczMjI2NTIyNCwiaWF0IjoxNzMxNjY1MjI0fQ.38mA49-C2OpzEjb7L05N_eHE9mpKgqy_bCkibn1a4Fk";
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");


    String id;

    @BeforeMethod
    public void preCondition() throws IOException {
        //create contact
        int i = new Random().nextInt(1000) + 1000;
        ContactDto contactDto = ContactDto.builder()
                .name("Maya")
                .lastName("Dow")
                .address("Haifa")
                .email("maya" + i + "@gmail.com")
                .phone("1234556" + i)
                .description("Maya Dow")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(contactDto), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        MessageDto messageDto = gson.fromJson(response.body().string(), MessageDto.class);
        String message = messageDto.getMessage();

        //get id from "message" : "Contact was added! ID: 47ee5012-839f-42b5-905a-9ec7cba66995"
        String[] all = message.split(": ");
        //id =""
        id = all[1];
        System.out.println(id);

    }

    @Test
    public void deleteContactByIdSuccess() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/" + id)
                .delete()
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 200);
        MessageDto responseDto = gson.fromJson(response.body().string(), MessageDto.class);
        System.out.println(responseDto.getMessage());
        Assert.assertEquals(responseDto.getMessage(), "Contact was deleted!");

    }

    @Test
    public void deleteContactByIdWrongToken() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/c1be6e7b-f4c4-49e7-89d8-af29b895db97")
                .delete()
                .addHeader("Authorization", "ghfdf355")
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 401);
        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        Assert.assertEquals(errorDto.getError(),"Unauthorized");


    }

    @Test
    public void deleteContactByIdNotFound() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/"+123)
                .delete()
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 400);
        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        Assert.assertEquals(errorDto.getError(),"Bad Request");
       // System.out.println(errorDto.getMessage());
        Assert.assertEquals(errorDto.getMessage(),"Contact with id: 123 not found in your contacts!");


    }

}
//ce75867f-070e-4b31-ab9f-45c3ce3e6f2b
//a@a
//==============================
//        8b520d2e-1a71-4a4a-a24d-4fb0bf551bd7
//b@b
//==============================
//c1be6e7b-f4c4-49e7-89d8-af29b895db97
//c@c
//==============================
//d99c32ff-278f-4328-9dfa-5b08dae81400
//Bart@gmail.com
//==============================
//        5b8ef2a9-4b9f-4eb3-90ba-cccb1c80545c
//Bart@gmail.com
//==============================
//ed44c04a-9528-4e41-85eb-f7c4bcf70dfb
//Bart@gmail.com
//==============================