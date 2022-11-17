package sillenceSoft.schedulleCall.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sillenceSoft.schedulleCall.Repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public String signup() {

        //return userRepository.findBy();
        return "";
    }

    public String login() {

        //return userRepository.findBy();
        return "";
    }



}
