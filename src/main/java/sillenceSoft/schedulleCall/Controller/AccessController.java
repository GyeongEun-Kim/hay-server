package sillenceSoft.schedulleCall.Controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import sillenceSoft.schedulleCall.Service.AccessService;

@Controller
@RequiredArgsConstructor
@Getter
@Setter
public class AccessController {
    private final AccessService accessService;

    @PostMapping("/access")
    public String canAccess () {
        return "";
    }

    @PostMapping("/notaccess")
    public String cannotAccess () {
        return "";
    }
}
