package sillenceSoft.schedulleCall.Repository;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import sillenceSoft.schedulleCall.Dto.StatusDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Mapper
public interface StatusRepository  {

    List<Map<String,String>> getAllStatus (@Param("userNo") int userNo);
    void addStatus (@Param("statusDto")StatusDto statusDto);
}
