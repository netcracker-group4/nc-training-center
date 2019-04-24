package ua.com.nc.dto;

import ua.com.nc.service.impl.AttendanceServiceImpl;

import java.util.List;

public class AttendanceDto{
    public List<AttendanceServiceImpl.TrainerDto> trainers;

    public AttendanceDto(List<AttendanceServiceImpl.TrainerDto> trainerDto){
        this.trainers = trainerDto;
    }
}