package sillenceSoft.schedulleCall.Service;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sillenceSoft.schedulleCall.Dto.UserDto;
import sillenceSoft.schedulleCall.Repository.UserRepository;

import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final SHA_256 sha256;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public String login(UserDto user) throws NoSuchAlgorithmException {

        if (userRepository.findById(user)==null) {
            userRepository.login(user); //회원가입
        }

        return jwtProvider.createToken(user.getId(),user.getPhone());

    }




}
