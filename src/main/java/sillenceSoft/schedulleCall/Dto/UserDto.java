package sillenceSoft.schedulleCall.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Integer userNo;
    private String Id;
    private String password;
    private String phone;
    private String token;
    private LocalDateTime regTime;
    private String nowStatus;

    //생성자
    public UserDto (String Id, String password, String phone, String token) {
        this.Id = Id;
        this.password= password;
        this.phone =phone;
        this.token = token;
        this.regTime = LocalDateTime.now();
        this.nowStatus = "empty"; // 초기상태 , 아무 상태가 없을 때
    }

}
