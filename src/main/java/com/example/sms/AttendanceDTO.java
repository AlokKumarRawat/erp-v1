package com.example.sms.dto;

import com.example.sms.Attendance;

import java.util.List;

public class AttendanceDTO {

    private List<Attendance> attendanceList;

    public List<Attendance> getAttendanceList() {
        return attendanceList;
    }

    public void setAttendanceList(List<Attendance> attendanceList) {
        this.attendanceList = attendanceList;
    }
}