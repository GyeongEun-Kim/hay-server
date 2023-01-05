package sillenceSoft.schedulleCall.Service;

import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class SHA_256 {

    private  final Charset UTF_8 = StandardCharsets.UTF_8;
    private  final String OUTPUT_FORMAT = "%-20s:%s";

    public byte[] digest(byte[] input, String algorithm) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        byte[] result = md.digest(input);
        return result;
    }

    public  String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public String encrypt(String text) {

        //String algorithm = "SHA-256"; // if you perfer SHA-2
        String algorithm = "SHA3-256";

        //String pText = "Hello World";
//        System.out.println(String.format(OUTPUT_FORMAT, "Input (string)", pText));
//        System.out.println(String.format(OUTPUT_FORMAT, "Input (length)", pText.length()));

        byte[] shaInBytes = digest(text.getBytes(UTF_8), algorithm);
        return bytesToHex(shaInBytes);
        // fixed length, 32 bytes, 256 bits.



    }
//    public String encrypt(String password) throws NoSuchAlgorithmException {
//        MessageDigest md = MessageDigest.getInstance("SHA-256");
//        md.update(password.getBytes());
//
//        return bytesToHex(md.digest());
//    }
//
//    private String bytesToHex(byte[] bytes) {
//        StringBuilder builder = new StringBuilder();
//        for (byte b : bytes) {
//            builder.append(String.format("%02x", b));
//        }
//        return builder.toString();
//    }

}
