package sillenceSoft.schedulleCall.Repository;

import org.apache.catalina.User;
import org.apache.ibatis.annotations.*;

import org.springframework.stereotype.Repository;
import sillenceSoft.schedulleCall.Dto.UserDto;

@Mapper
public interface UserRepository {
    void login(@Param("userDto") UserDto userDto);

    UserDto findById(@Param("id")String id);

}
