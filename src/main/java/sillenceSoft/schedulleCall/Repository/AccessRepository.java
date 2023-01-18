package sillenceSoft.schedulleCall.Repository;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import sillenceSoft.schedulleCall.Dto.AccessListResponseDto;
import sillenceSoft.schedulleCall.Dto.UserDto;

import java.util.List;


@Mapper
public interface AccessRepository  {
    List<String> getAccessList (Long userNo);
    void saveAccess (@Param(value = "userNo") Long userNo, @Param(value = "accessUserPhone") String accessUserPhone);
    void deleteAccess (@Param(value = "userNo")Long userNo, @Param(value = "accessUserPhone")String accessUserPhone);
    Long checkAccessOrNot (@Param(value="userNo") Long userNo, @Param(value="accessUserPhone") String accessUserPhone );
}
