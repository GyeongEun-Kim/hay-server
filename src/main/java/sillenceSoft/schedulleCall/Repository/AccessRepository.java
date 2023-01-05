package sillenceSoft.schedulleCall.Repository;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import sillenceSoft.schedulleCall.Dto.UserDto;

import java.util.List;


@Mapper
public interface AccessRepository  {
    List<UserDto> getAccessList (Integer userNo);
    void saveAccess (@Param(value = "userNo") Integer userNo, @Param(value = "accessUserNo") Integer accessUserNo);
    void deleteAccess (@Param(value = "userNo")Integer userNo, @Param(value = "accessUserNo")Integer accessUserNo);
    Integer checkAccessOrNot (@Param(value="phone") String phone, @Param(value="userNo") Integer userNo );
}
