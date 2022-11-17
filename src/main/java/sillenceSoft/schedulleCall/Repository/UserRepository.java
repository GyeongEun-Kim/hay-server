package sillenceSoft.schedulleCall.Repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sillenceSoft.schedulleCall.Dto.UserDto;


@Mapper
public interface UserRepository {

    @Insert("insert into User (id, password, phone, token, regTime, nowStatus) values " +
            "#{id}, #{password}, #{phone}, #{token}, #{regTime}, #{nowStatus}")
    void signup(UserDto userDto);

}
