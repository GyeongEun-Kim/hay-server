package sillenceSoft.schedulleCall.Repository;

import org.apache.catalina.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sillenceSoft.schedulleCall.Dto.UserDto;


@Mapper
public interface UserRepository {

    @Insert("insert into User (id, phone, token, regTime, nowStatus) values " +
            "#{id}, #{phone}, #{token}, #{regTime}, #{nowStatus}")
    @Options(useGeneratedKeys = true, keyProperty = "userNo")
    void login(UserDto userDto);

    @Select ("select * where id=#{id}")
    UserDto findById(UserDto userDto);

}
