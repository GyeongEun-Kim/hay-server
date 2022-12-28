package sillenceSoft.schedulleCall.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sillenceSoft.schedulleCall.Dto.UserDto;
import sillenceSoft.schedulleCall.Dto.UserRequestDto;
import sillenceSoft.schedulleCall.Repository.UserRepository;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class UserService {

    private final SHA_256 sha256;
    private final UserRepository userRepository;
    private final GoogleUserInfo googleUserInfo;
    private final KakaoUserInfo kakaoUserInfo;

    public UserDto login(UserRequestDto userRequestDto) throws NoSuchAlgorithmException {
        String id = sha256.encrypt(getIdBySocial(userRequestDto));
        //String id = sha256.encrypt("테스트입니다아"); //로컬테스트용
        UserDto userDto = userRepository.findById(id);

        if (userDto==null) { //신규회원인 경우
            userDto = UserDto.builder()
                    .id(id)
                    .phone(sha256.encrypt(userRequestDto.getPhone()))
                    .social(userRequestDto.getSocial())
                    .statusNo(null)
                    .regTime(LocalDateTime.now())
                    .statusOn(true)
                    .build();
             userRepository.login(userDto);//회원가입
        }

        return userDto;
    }

    public String getIdBySocial ( UserRequestDto userRequestDto) {
        String id = "";
        if (userRequestDto.getSocial().equals("0")) { //구글로그인
            id = googleUserInfo.getUserInfo(userRequestDto.getToken());
        }
        else if (userRequestDto.getSocial().equals("1")) { //카카오로그인
            id = kakaoUserInfo.getUserInfo(userRequestDto.getToken());
        }
        //System.out.println("id = " + id);
        return id;
    }


    public String setNowStatus (Integer userNo, Integer statusNo) {
        String msg;
        try{
            userRepository.setNowStatus(userNo,statusNo);
            msg="success";
        }
        catch (Exception e) {
            e.printStackTrace();
            msg="fail to set status";
        }
        return msg;

    }



    }
