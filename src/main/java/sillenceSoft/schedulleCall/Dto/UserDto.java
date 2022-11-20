package sillenceSoft.schedulleCall.Dto;

import lombok.*;
import sillenceSoft.schedulleCall.Service.SHA_256;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
@Setter
public class UserDto {

    private Integer userNo;
    private String id;
    private String phone;
    private String social;
    private LocalDateTime regTime;
    private String nowStatus;

    //생성자
    public UserDto (String id, String phone, String social) throws NoSuchAlgorithmException {
        SHA_256 sha256 = new SHA_256();
        this.id = sha256.encrypt(id);
        this.phone =sha256.encrypt(phone);
        this.social = social;
        this.regTime = LocalDateTime.now();
        this.nowStatus = "empty"; // 초기상태 , 아무 상태가 없을 때
    }


}
