package sillenceSoft.schedulleCall.Repository;

import org.apache.catalina.User;
import org.apache.ibatis.annotations.*;

import org.springframework.stereotype.Repository;
import sillenceSoft.schedulleCall.Dto.UserDto;

import java.util.Optional;

@Mapper
public interface UserRepository {
    void login(@Param("userDto") UserDto userDto);

    UserDto findById(@Param("id")String id);

    int getUserNoById (@Param("id")String id);

    void setNowStatus (@Param("userNo") int userNo, @Param("statusNo") int statusNo);



}
