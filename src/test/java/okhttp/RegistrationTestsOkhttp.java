package okhttp;

import com.google.gson.Gson;
import dto.AuthRequestDto;
import dto.AuthResponseDto;
import dto.ErrorDto;
import okhttp3.*;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class RegistrationTestsOkhttp {

    Gson gson = new Gson();

    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    OkHttpClient client = new OkHttpClient();


    @Test
    public void registrationSuccess() throws IOException {

        int i = (int)(System.currentTimeMillis()/1000)%3600;

        AuthRequestDto auth = AuthRequestDto.builder().username("mara"+i+"@gmail.com").password("Mmar123456$").build();

        RequestBody requestBody = RequestBody.create(gson.toJson(auth),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(),200);
        AuthResponseDto responseDto=
                gson.fromJson(response.body().string(),AuthResponseDto.class);
        String  token = responseDto.getToken();
        System.out.println(token);
    }
    @Test

    public void registrationTestWrongEmail() throws IOException {
        int i = (int) ((System.currentTimeMillis()/1000)%3600);
        AuthRequestDto authDTO = AuthRequestDto.builder()
                .username("margagmail.com")
                .password("Mmar123456$")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(authDTO), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 400);

        ErrorDto errorDTO = gson.fromJson(response.body().string(), ErrorDto.class);
        Assert.assertEquals(errorDTO.getStatus(), 400);
        Assert.assertEquals(errorDTO.getError(), "Bad Request");
        Assert.assertEquals(errorDTO.getMessage().toString(), "{username=must be a well-formed email address}");
    }

    @Test
    public void registrationTestWrongPassword() throws IOException {
        int i = (int) ((System.currentTimeMillis()/1000)%3600);
        AuthRequestDto authDTO = AuthRequestDto.builder()
                .username("mara@gmail.com")
                .password("Mmar123")
                .build();

        RequestBody body = RequestBody.create(gson.toJson(authDTO),JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 400);

        ErrorDto errorDTO = gson.fromJson(response.body().string(), ErrorDto.class);
        System.out.println(errorDTO.getMessage().toString());
        Assert.assertEquals(errorDTO.getStatus(), 400);
        Assert.assertEquals(errorDTO.getError(), "Bad Request");
        Assert.assertEquals(errorDTO.getMessage().toString(), "{password= At least 8 characters; Must contain at least 1 uppercase letter, 1 lowercase letter, and 1 number; Can contain special characters [@$#^&*!]}");
    }

    @Test
    public void registrationTestRegisteredUser() throws IOException {
        AuthRequestDto authDTO = AuthRequestDto.builder()
                .username("mara@gmail.com")
                .password("Mmar123456$")
                .build();

        RequestBody body = RequestBody.create(gson.toJson(authDTO),JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 409);

        ErrorDto errorDTO = gson.fromJson(response.body().string(), ErrorDto.class);
        Assert.assertEquals(errorDTO.getStatus(), 409);
        System.out.println(errorDTO);
        Assert.assertEquals(errorDTO.getError(), "Conflict");
        Assert.assertEquals(errorDTO.getMessage().toString(), "User already exists");

    }
}
