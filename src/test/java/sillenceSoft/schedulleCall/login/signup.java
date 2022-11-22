package sillenceSoft.schedulleCall.login;

import org.apache.catalina.User;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sillenceSoft.schedulleCall.Dto.UserDto;
import sillenceSoft.schedulleCall.Dto.UserRequestDto;
import sillenceSoft.schedulleCall.Repository.UserRepository;
import sillenceSoft.schedulleCall.Service.SHA_256;
import sillenceSoft.schedulleCall.Service.UserService;

import java.security.NoSuchAlgorithmException;

@AutoConfigureMockMvc
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class signup {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Test
    public void test () throws NoSuchAlgorithmException {
        //given
        UserRequestDto userRequestDto = new UserRequestDto("token", "01012345678", "0");
        UserRequestDto userRequestDto2 = new UserRequestDto("token", "01098945645", "1");


        //when
        UserDto login = userService.login(userRequestDto);
        Assertions.assertThat(userRepository.findById(login.getId())!=null);
        UserDto login2 = userService.login(userRequestDto2);


        //then
        Assertions.assertThat(login.getId().equals(login2.getId()));


    }
}
