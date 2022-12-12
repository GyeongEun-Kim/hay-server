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
    private String social;
    private LocalDateTime regTime;
    private Integer statusNo;
    private boolean statusOn;

}
