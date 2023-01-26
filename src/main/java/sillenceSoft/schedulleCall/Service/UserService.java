package sillenceSoft.schedulleCall.Service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sillenceSoft.schedulleCall.Dto.UserDto;
import sillenceSoft.schedulleCall.Dto.UserRequestDto;
import sillenceSoft.schedulleCall.JwtAuthenticationFilter;
import sillenceSoft.schedulleCall.Repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class UserService {

    private final SHA_256 sha256;
    private final UserRepository userRepository;
    private final GoogleUserInfo googleUserInfo;
    private final KakaoUserInfo kakaoUserInfo;
    private final JWTProvider jwtProvider;
    private final JwtAuthenticationFilter filter;

    public UserDto login(UserRequestDto userRequestDto) throws NoSuchAlgorithmException {
        //String id = sha256.encrypt(getIdBySocial(userRequestDto));
        String id = sha256.encrypt("iii"); //로컬테스트용
        UserDto userDto = userRepository.findByIdAndSocial(id, userRequestDto.getSocial());

        if (userDto == null) { //신규회원인 경우
            userDto = UserDto.builder()
                    .id(id)
                    .phone(sha256.encrypt(userRequestDto.getPhone()))
                    .social(userRequestDto.getSocial())
                    .statusNo(null)
                    .regTime(LocalDateTime.now())
                    .statusOn(true) //default: 공개상태
                    .statusState("0") //default :상태글상태
                    .build();
            userRepository.login(userDto);//회원가입
        }
        else { // 기존 회원인 경우 로그인시간 update
            userRepository.updateLoginTime(id, userRequestDto.getSocial(), LocalDateTime.now());
        }
        return userDto;
    }

    public String getIdBySocial(UserRequestDto userRequestDto) {
        String id = "";
        if (userRequestDto.getSocial().equals("0")) { //구글로그인
            id = googleUserInfo.getUserInfo(userRequestDto.getToken());
        } else if (userRequestDto.getSocial().equals("1")) { //카카오로그인
            id = kakaoUserInfo.getUserInfo(userRequestDto.getToken());
        }
        return id;
    }


    public ResponseEntity setNowStatus(Long userNo, Long statusNo, String statusState) {
        Boolean msg;
        try {
            Long nowStatus = userRepository.getStatusNo(userNo); //현재 상태글
            if (nowStatus == null) {
                userRepository.setNowStatus(userNo, statusNo);
                msg=true;
            } else {
                if (statusState.equals("1")) {
                    if (statusNo == null) {
                        userRepository.cancelNowStatus(userNo);
                        msg=false;
                    } else {
                        userRepository.setNowStatus(userNo, statusNo);
                        msg=true;
                    }
                } else {
                    if (nowStatus.equals(statusNo)) {
                        userRepository.cancelNowStatus(userNo);
                        msg= false;
                    } else {
                        userRepository.setNowStatus(userNo, statusNo);
                        userRepository.cancelStatusState(userNo);
                        msg=true;
                    }
                }
            }
            return new ResponseEntity(msg, HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);


            }


//            if (nowStatus ==null) {
//                userRepository.setNowStatus(userNo, statusNo);
//                msg = true;
//            }
//            else if (nowStatus.equals(statusNo)){
//                userRepository.cancelNowStatus(userNo); // 현재 상태글 해제
//                msg = false;
//            }
//            else { //다른 산태글 설전된견누
//                if (userRepository.getStatusState(userNo).equals("1")) { //스케줄 상태
//                    userRepository.cancelStatusState(userNo);
//                    if (statusNo==null)
//                        userRepository.cancelNowStatus(userNo);
//                    else
//                        userRepository.setNowStatus(userNo, statusNo);
//                }
//                else  {
//                    userRepository.setNowStatus(userNo, statusNo);
//                }
//                msg =true;
//            }
//            return new ResponseEntity(msg,HttpStatus.OK);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }


    }


    public ResponseEntity jwtCheck(HttpServletRequest request, HttpServletResponse response)  {
        try {
            String refreshToken = jwtProvider.getRefreshToken(request).orElse("empty");
            if (refreshToken.equals("empty")) { //리프레시 토큰 자체가 오지않은경우
                return new ResponseEntity("리프레시 토큰을 보내주세요.", HttpStatus.NO_CONTENT);
            }
            else { //리프레시 토큰이 존재
                refreshToken = refreshToken.substring(7);
                if (jwtProvider.validTokenAndReturnBody(refreshToken) != null) {//유효하면
                    Claims claims = jwtProvider.validTokenAndReturnBody(refreshToken);
                    Long userNo = Long.valueOf(claims.get("userNo").toString());
                    String social = claims.get("social").toString();

                    //DB에 로그인 시간 업데이트
                    LocalDateTime now = LocalDateTime.now();
                    String id = userRepository.getIdByUserNo(userNo);
                    userRepository.updateLoginTime(id,social,now);

                    /// 토큰 발급 (access, refresh 둘다 재발급)
                    String newAccessToken = jwtProvider.createAccessToken(userNo,social);
                    String newRefreshToken = jwtProvider.createRefreshToken(userNo,social);
                    /// 헤더에 엑세스, 리프레시 토큰 추가
                    jwtProvider.setHeaderAccessToken(newAccessToken, response);
                    jwtProvider.setHeaderRefreshToken(newRefreshToken, response);

                    filter.setAuthentication(newAccessToken);
                    return new ResponseEntity("success", HttpStatus.OK);
                    }
                else { //리프레시 토큰 유효x
                    return new ResponseEntity("리프레시 토큰 만료. 로그인 페이지로 돌아갑니다.", HttpStatus.UNAUTHORIZED);
                    }
                }
            }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity getStatusOn (Long userNo) {
        try {
            return new ResponseEntity(userRepository.getStatusOnAndPhone(userNo), HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity statusShow (Long userNo) {
        String msg;
        try {
            if (userRepository.getStatusOn(userNo)==true)
                userRepository.setStatusOff(userNo);
            else userRepository.setStatusOn(userNo);
            msg="success";
            return new ResponseEntity(msg, HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            msg=e.toString();
            return new ResponseEntity(msg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
