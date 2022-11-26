package sillenceSoft.schedulleCall.login;


import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class social {
    @Test
    public void JsonParsingTest () {
        String result = "{\n" +
                "    \"iss\": \"https://accounts.google.com\",\n" +
                "    \"azp\": \"762276850144-ee66ruvhui9ja26obfo9caj456rmufn0.apps.googleusercontent.com\",\n" +
                "    \"aud\": \"762276850144-jjs9jgnphf4pualsk7nem2tsoqv4e7m6.apps.googleusercontent.com\",\n" +
                "    \"sub\": \"105328303472081545460\",\n" +
                "    \"name\": \"사일런스소프트\",\n" +
                "    \"picture\": \"https://lh3.googleusercontent.com/a/ALm5wu0IvsG2aQ7JSkLCe80kni7IPXrbL8mkWTdYdUvg=s96-c\",\n" +
                "    \"given_name\": \"소프트\",\n" +
                "    \"family_name\": \"사일런스\",\n" +
                "    \"locale\": \"ko\",\n" +
                "    \"iat\": \"1668854853\",\n" +
                "    \"exp\": \"1668858453\",\n" +
                "    \"alg\": \"RS256\",\n" +
                "    \"kid\": \"27b86dc6938dc327b204333a250ebb43b32e4b3c\",\n" +
                "    \"typ\": \"JWT\"\n" +
                "}";

        JsonElement element = JsonParser.parseString(result);
        String id = element.getAsJsonObject().get("sub").getAsString();

        Assertions.assertThat(id.equals("105328303472081545460"));
    }
}
