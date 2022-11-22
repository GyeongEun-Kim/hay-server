package sillenceSoft.schedulleCall.Service;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@Component
public class GoogleUserInfo implements OAuthUserInfo{
    @Override
    public String getUserInfo(String token) {
        String reqURL = "https://oauth2.googleapis.com/tokeninfo?id_token="+token;
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

            id = element.getAsJsonObject().get("sub").getAsString();

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
