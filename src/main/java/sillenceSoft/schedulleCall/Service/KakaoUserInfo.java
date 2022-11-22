package sillenceSoft.schedulleCall.Service;


import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpClient;
import java.util.HashMap;

@Component
public class KakaoUserInfo implements OAuthUserInfo{

    @Override
    public String getUserInfo(String token) {
        String reqURL = "https://kapi.kakao.com/v1/user/";
        String id = null;
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            //    요청에 필요한 Header에 포함될 내용

            conn.setRequestProperty("Authorization", "Bearer " + token);

            int responseCode = conn.getResponseCode(); //요청 전송
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            id = element.getAsJsonObject().get("id").getAsString();

//            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
//            JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
//
//            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
//            String email = kakao_account.getAsJsonObject().get("email").getAsString();
//
//            userInfo.put("nickname", nickname);
//            userInfo.put("email", email);

        } catch (
                IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return id;

    }


    @Override
    public void checkTokenValidity() {

    }

    @Override
    public void extendTokenTime() {

    }
}
