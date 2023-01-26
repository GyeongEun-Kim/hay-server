package sillenceSoft.schedulleCall.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sillenceSoft.schedulleCall.Dto.AllStatus;
import sillenceSoft.schedulleCall.Dto.StatusDto;
import sillenceSoft.schedulleCall.Dto.StatusResponseDto;
import sillenceSoft.schedulleCall.Dto.UserDto;
import sillenceSoft.schedulleCall.Repository.AccessRepository;
import sillenceSoft.schedulleCall.Repository.StatusRepository;
import sillenceSoft.schedulleCall.Repository.UserRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatusService {
    private final StatusRepository statusRepository;
    private final UserRepository userRepository;
    private final AccessRepository accessRepository;
    private final SHA_256 sha256;
    private final ScheduleService scheduleService;


    public ResponseEntity getAllStatus (Long userNo) {
        try {
            AllStatus allStatus = AllStatus.builder()
                    .allStatus(statusRepository.getAllStatus(userNo))
                    .StatusState(userRepository.getStatusState(userNo))
                    .build();

            Long nowStatus = userRepository.getNowStatus(userNo);
            //현재 상태글 존재 여부

            for (Map m : allStatus.getAllStatus()) {
                if (nowStatus != null) {
                    if (Long.valueOf(m.get("statusNo").toString()).equals(nowStatus)) {
                        m.put("selected", true);
                    }
                }
            }
            return new ResponseEntity<AllStatus>(allStatus, HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity addStatus (Long userNo, String newStatusMemo) {

        ResponseEntity responseEntity;
        StatusDto statusDto = StatusDto.builder()
                    .userNo(userNo)
                    .status(newStatusMemo)
                    .modDt(LocalDateTime.now())
                    .isFromSchedule(false)
                    .build();
        try {
            if (statusRepository.checkIfPresent(userNo,statusDto.getStatus(),false)== null) {
                statusRepository.addStatus(statusDto);
                responseEntity = new ResponseEntity(statusDto, HttpStatus.OK);
            }
            else {
                responseEntity = new ResponseEntity("이미 같은 상태글이 존재합니다.", HttpStatus.CONFLICT);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            responseEntity = new ResponseEntity(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }


    public ResponseEntity deleteStatus (Long statusNo) {
        try {
            StatusDto status = statusRepository.getStatus(statusNo);
            if (status != null) {
                statusRepository.deleteStatus( statusNo);
                return new ResponseEntity("success", HttpStatus.OK);
            }else {
                return new ResponseEntity("해당 상태글이 존재하지 않습니다", HttpStatus.NO_CONTENT);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity updateStatus (Long userNo, String status, Long statusNo) {
        try {
            if (statusRepository.getStatus(statusNo)==null) {
                return new ResponseEntity("수정하려는 상태글이 존재하지 않습니다",HttpStatus.NO_CONTENT);
            }
            else if (statusRepository.checkIfPresent(userNo, status, false)!=null)
                return new ResponseEntity("이미 같은 상태글이 존재합니다",HttpStatus.CONFLICT);
            else {
                statusRepository.updateStatus(status, statusNo, LocalDateTime.now());
                return new ResponseEntity("success", HttpStatus.OK);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity getOthersStatus (Long userNo, String accessUserPhone) {
//        //userNo유저가  accessUserPhone유저의 상태를 보려고 하는상황
        UserDto userDto = userRepository.getUserDto(userNo);
        //1. accessUser가 statusOn돼있어야함
        //2. accessUser가 userNo를 숨김해제해야함
        String encryptedUserPhone = userDto.getPhone();
        String encryptedAccessUserPhone = sha256.encrypt(accessUserPhone);

        UserDto accessUserDto = userRepository.getUserDtoByPhone(encryptedAccessUserPhone);
        if(accessUserDto== null) {
            return new ResponseEntity("가입하지 않은 사용자입니다.",HttpStatus.NO_CONTENT);
        }

        Boolean statusOn = accessUserDto.isStatusOn();
        //상태글 공개 여부 . 1이여야 볼 수 있음
        Long check = accessRepository.checkAccessOrNot(accessUserDto.getUserNo(), encryptedUserPhone );
        //access 여부 . 1이여야 볼 수 있음
        String statusState = accessUserDto.getStatusState();
        //상태글상태/ 스케줄 상태


        if (statusOn==true && check==1) {
            if(statusState.equals(1)) { //스케줄상태
                scheduleService.toScheduleStatus(accessUserDto.getUserNo());
            }
            StatusResponseDto nowStatusAndPhone = userRepository.getNowStatusAndPhone(accessUserDto.getUserNo());
            return new ResponseEntity(nowStatusAndPhone,HttpStatus.OK);
        }
        else {
            return new ResponseEntity("사용자의 상태글에 접근할 수 없습니다.",HttpStatus.NO_CONTENT);
        }

    }

    public ResponseEntity getAllOthersStatus (Long thisUserNo) {
        try {
            String phone = userRepository.getPhoneByUserNo(thisUserNo);
            List<StatusResponseDto> allOthersStatus = statusRepository.getAllOthersStatus(phone);
            return new ResponseEntity(allOthersStatus,HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
