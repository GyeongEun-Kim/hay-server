package sillenceSoft.schedulleCall.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sillenceSoft.schedulleCall.Repository.AccessRepository;

@Service
@RequiredArgsConstructor
public class AccessService {
    private final AccessRepository accessRepository;

    public String canAccess () {
        //accessRepository.save();
        return "";
    }

    public String cannotAccess () {

        //accessRepository.delete();
        return "";
    }

}
