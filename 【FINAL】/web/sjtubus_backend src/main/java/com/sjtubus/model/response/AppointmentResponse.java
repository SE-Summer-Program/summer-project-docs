package com.sjtubus.model.response;

import com.sjtubus.entity.Appointment;
import com.sjtubus.entity.Shift;

import java.util.List;

public class AppointmentResponse extends HttpResponse {
    private List<Appointment> appointmentList;

    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }

    public void setAppointmentList(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

}
