package sillenceSoft.schedulleCall.Controller;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class ScheduleContentController {
    @GetMapping(value = "/schedule")
    public void getMySchedule (Authentication authentication) {
        Claims principal = (Claims)authentication.getPrincipal();
        String id = (String) principal.get("id");
    }

    @PostMapping(value = "/schedule")
    public void addSchedule (Authentication authentication, String jsonString ) {
        Claims principal = (Claims)authentication.getPrincipal();
        String id = (String) principal.get("id");

    }

    @DeleteMapping(value = "schedule")
    public void deleteSchedule (Authentication authentication,@RequestParam Integer scheduleNo) {
        Claims principal = (Claims)authentication.getPrincipal();
        String id = (String) principal.get("id");
    }
}
