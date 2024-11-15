package okhttp;

import com.google.gson.Gson;
import dto.AuthRequestDto;
import dto.AuthResponseDto;
import dto.ErrorDto;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class LoginTestsOkhttp {
    Gson gson = new Gson();
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    @Test
    public void loginSuccess() throws IOException {
        AuthRequestDto auth = AuthRequestDto.builder()
                .username("mara@gmail.com")
                .password("Mmar123456$")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(auth), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/login/usernamepassword")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);
        AuthResponseDto responseDto = gson.fromJson(response.body().string(), AuthResponseDto.class);
        System.out.println(responseDto.getToken());

    }

    @Test
    public void loginWrongEmail() throws IOException {
        AuthRequestDto auth = AuthRequestDto.builder()
                .username("maragmail.com")
                .password("Mmar123456$")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(auth), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/login/usernamepassword")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 401);
        ErrorDto errorDto = gson.fromJson(response.body().string(),dto.ErrorDto.class);
        Assert.assertEquals(errorDto.getStatus(),401);
        Assert.assertEquals(errorDto.getMessage(),"Login or Password incorrect");
        Assert.assertEquals(errorDto.getPath(),"/v1/user/login/usernamepassword");


    }

    @Test
    public void loginWrongPassword() throws IOException {
        AuthRequestDto auth = AuthRequestDto.builder()
                .username("mara@gmail.com")
                .password("Mmar123")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(auth), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/login/usernamepassword")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 401);
        ErrorDto errorDto = gson.fromJson(response.body().string(),dto.ErrorDto.class);
        Assert.assertEquals(errorDto.getStatus(),401);
        Assert.assertEquals(errorDto.getMessage(),"Login or Password incorrect");
        Assert.assertEquals(errorDto.getPath(),"/v1/user/login/usernamepassword");

    }

    @Test
    public void loginUnregisteredUser() throws IOException {
        AuthRequestDto auth = AuthRequestDto.builder()
                .username("mara123@gmail.com")
                .password("Mmar123456$")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(auth), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/login/usernamepassword")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 401);
        ErrorDto errorDto = gson.fromJson(response.body().string(),dto.ErrorDto.class);
        Assert.assertEquals(errorDto.getStatus(),401);
        Assert.assertEquals(errorDto.getMessage(),"Login or Password incorrect");
        Assert.assertEquals(errorDto.getPath(),"/v1/user/login/usernamepassword");

    }
}
