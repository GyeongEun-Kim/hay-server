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

    Long getUserNoById (@Param("id")String id);

    void setNowStatus (@Param("userNo") Long userNo, @Param("statusNo") Long statusNo);

    StatusResponseDto getNowStatusAndPhone (Long userNo);

    void cancelNowStatus (Long userNo);

    void setStatusOn (@Param("userNo")Long userNo);

    void setStatusOff (@Param("userNo")Long userNo);

    Long findByPhone(String phone);

    Long getStatusNo (Long userNo);

    Map<String,Object> getStatusOnAndPhone (Long userNo);

    boolean getStatusOn (Long userNo);

    Long getNowStatus (Long userNo);

    String getIdByUserNo (Long userNo);

    void updateLoginTime (@Param("id") String id,@Param("social")String social, @Param("regTime")LocalDateTime regTime);


    UserDto findByIdAndSocial(@Param("id")String id, @Param("social")String social);

    String getStatusState (Long userNo);

    void setStatusState (Long userNo);

    void cancelStatusState (Long userNo);

    String getPhoneByUserNo (Long userNo);

    Long getUserNoByPhone (String phone);
}
