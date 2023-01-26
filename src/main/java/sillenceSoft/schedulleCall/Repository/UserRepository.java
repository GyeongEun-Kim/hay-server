package sillenceSoft.schedulleCall.Repository;

import org.apache.catalina.User;
import org.apache.ibatis.annotations.*;

import org.springframework.stereotype.Repository;
import sillenceSoft.schedulleCall.Dto.StatusResponseDto;
import sillenceSoft.schedulleCall.Dto.UserDto;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface UserRepository {
    void login(@Param("userDto") UserDto userDto);


    StatusResponseDto getNowStatusAndPhone (Long userNo);


    void setNowStatus (@Param("userNo") Long userNo, @Param("statusNo") Long statusNo);

    void cancelNowStatus (Long userNo);


    Map<String,Object> getStatusOnAndPhone (Long userNo);


    Long getNowStatus (Long userNo);

    String getIdByUserNo (Long userNo);

    void updateLoginTime (@Param("id") String id,@Param("social")String social, @Param("regTime")LocalDateTime regTime);


    UserDto findByIdAndSocial(@Param("id")String id, @Param("social")String social);

    String getStatusState (Long userNo);

    void setStatusState (@Param("userNo") Long userNo, @Param("state") String state);

    String getPhoneByUserNo (Long userNo);

    UserDto getUserDtoByPhone (String phone);

    UserDto getUserDto (Long userNo);

    void updateUserDto (@Param("userDto") UserDto userDto);
}
