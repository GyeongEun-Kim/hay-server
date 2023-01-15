package sillenceSoft.schedulleCall.Repository;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import sillenceSoft.schedulleCall.Dto.AccessListResponseDto;
import sillenceSoft.schedulleCall.Dto.UserDto;

import java.util.List;


@Mapper
public interface AccessRepository  {
    List<String> getAccessList (Integer userNo);
    void saveAccess (@Param(value = "userNo") Integer userNo, @Param(value = "accessUserPhone") String accessUserPhone);
    void deleteAccess (@Param(value = "userNo")Integer userNo, @Param(value = "accessUserPhone")String accessUserPhone);
    Integer checkAccessOrNot (@Param(value="phone") String phone, @Param(value="userNo") Integer userNo );
}
