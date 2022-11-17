package sillenceSoft.schedulleCall.Controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sillenceSoft.schedulleCall.Service.ScheduleService;

@RestController
@RequiredArgsConstructor
@Getter
@Setter
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping(value = "/schedule")
    public String readSchedule () {
        return "";
    } //

    @DeleteMapping("/schedule")
    public String deleteSchedule () {
        return "";
    }
}
