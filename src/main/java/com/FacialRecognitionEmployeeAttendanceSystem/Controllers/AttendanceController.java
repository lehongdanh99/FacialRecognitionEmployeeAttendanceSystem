package com.FacialRecognitionEmployeeAttendanceSystem.Controllers;

import com.FacialRecognitionEmployeeAttendanceSystem.Entities.Attendances;
import com.FacialRecognitionEmployeeAttendanceSystem.Exceptions.ResourceNotFoundException;
import com.FacialRecognitionEmployeeAttendanceSystem.Repositories.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/attendances")
public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @GetMapping("/")
    public List<Attendances> getAllAttendances(){
        return attendanceRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Attendances> getAttendanceById(@PathVariable(value = "id") Long attendanceId) throws ResourceNotFoundException{
        Attendances attendance = attendanceRepository.findById(attendanceId).orElseThrow(()->new ResourceNotFoundException("Attendance not found on id: "+attendanceId));
        return ResponseEntity.ok().body(attendance);
    }

    @PostMapping("/add")
    public Attendances create(@Validated @RequestBody Attendances Attendances) throws Exception{
        Long attendanceID = Attendances.getId();
        if(attendanceID!=null&&!"".equals(attendanceID)){
            Attendances tempAttendanceID = attendanceRepository.findByAttendanceID(attendanceID);
            if(tempAttendanceID!=null){
                throw new Exception("Attendance ID: "+attendanceID+" is already exist");
            }
        }
        return attendanceRepository.save(Attendances);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Attendances> update(@PathVariable(value = "id") Long attendanceId,
                                               @Validated @RequestBody Attendances attendanceDetails) throws Exception{

        Attendances attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("This attendance not found on:" + attendanceId));

        boolean isActive = attendance.isActive();
        if(isActive==false){
            throw new Exception("This attendance has already been disabled!");
        }

        final Attendances updateAttendance = attendanceRepository.save(attendanceDetails);

        return ResponseEntity.ok(updateAttendance);
    }

    @PutMapping("/disable/{id}")
    public ResponseEntity<Attendances> disable(@PathVariable(value = "id") Long attendanceId) throws Exception{

        Attendances Attendances = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found on: " + attendanceId));

        boolean isActive = Attendances.isActive();
        if(isActive==false)
        {
            throw new Exception("Attendance has already been disabled!");
        }
        Attendances.setActive(false);
        final Attendances updateAttendance = attendanceRepository.save(Attendances);

        return ResponseEntity.ok(updateAttendance);
    }

    @PutMapping("/enable/{id}")
    public ResponseEntity<Attendances> enable(@PathVariable(value = "id") Long attendanceId) throws Exception{

        Attendances Attendances = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found on:" + attendanceId));

        boolean isActive = Attendances.isActive();
        if(isActive==true)
        {
            throw new Exception("Attendance has not been disabled yet!");
        }
        Attendances.setActive(true);
        final Attendances updateAttendance = attendanceRepository.save(Attendances);

        return ResponseEntity.ok(updateAttendance);
    }
}
