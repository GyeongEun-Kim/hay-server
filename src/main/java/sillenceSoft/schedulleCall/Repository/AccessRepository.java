package sillenceSoft.schedulleCall.Repository;

import org.apache.ibatis.annotations.Mapper;

import org.springframework.stereotype.Repository;
import sillenceSoft.schedulleCall.Dto.UserDto;

import java.util.List;


@Mapper
public interface AccessRepository  {
    List<UserDto> getAccessList (Integer userNo);

}
