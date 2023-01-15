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

    UserDto findById(@Param("id")String id);

    Integer getUserNoById (@Param("id")String id);

    void setNowStatus (@Param("userNo") int userNo, @Param("statusNo") int statusNo);

    StatusResponseDto getNowStatusAndPhone (Integer userNo);

    void cancelNowStatus (Integer userNo);

    void setStatusOn (@Param("userNo")Integer userNo);

    void setStatusOff (@Param("userNo")Integer userNo);

    Integer findByPhone(String phone);

    Integer getStatusNo (Integer userNo);

    Map<String,Object> getStatusOnAndPhone (Integer userNo);

    boolean getStatusOn (Integer userNo);

    Integer getNowStatus (Integer userNo);

    void updateLoginTime (@Param("id") String id, @Param("regTime")LocalDateTime regTime);


    UserDto findByIdAndSocial(@Param("id")String id, @Param("social")String social);

    Boolean getStatusState (Integer userNo);

    void setStatusState (Integer userNo);

    void cancelStatusState (Integer userNo);
}
