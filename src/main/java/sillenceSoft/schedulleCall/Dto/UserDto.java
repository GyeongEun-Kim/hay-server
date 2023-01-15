package sillenceSoft.schedulleCall.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import sillenceSoft.schedulleCall.Service.SHA_256;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;



@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private Integer userNo;
    private String id;
    private String phone;
    private String social; //소셜로그인 중류 0이면 구글 1이면 카카오
    private LocalDateTime regTime; //최근 로그인 시간
    private Integer statusNo;
    private boolean statusOn; //상태글 숨김 상태 여부. 0이면 숨김 1이면 공개
    private String statusState; //0이면 상태글상태, 1이면 스케줄상태

}
