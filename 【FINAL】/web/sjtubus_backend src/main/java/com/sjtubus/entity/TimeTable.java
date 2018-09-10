package com.sjtubus.entity;

import java.util.List;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "timeTable")
public class TimeTable {
	
	public TimeTable() {}
	
    @Id
    private String id ;
    private String station;
    private Double latitude;
    private Double longitude;
    private String type;
    private String image_url;
    private List<String> AntiClockLoop;
    private List<String> AntiClockNonLoop;
    private List<String> ClockLoop;
    private List<String> ClockNonLoop;
    private List<String> VacAntiClockLoop;
    private List<String> VacAntiClockNonLoop;
    private List<String> VacClockLoop;
    private List<String> VacClockNonLoop;

    public String getId() {
    	return this.id;
    }
    public void setId(String id){
    	this.id = id;
    }
    public String getStation() {
    	return this.station;
    }
    public void setStation(String station) {
    	this.station = station;
    }
    public String getType() {
    	return this.type;
    }
    public void setType(String type) {
    	this.type = type;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public List<String> getAntiClockLoop() {
        return AntiClockLoop;
    }

    public void setAntiClockLoop(List<String> antiClockLoop) {
        AntiClockLoop = antiClockLoop;
    }

    public List<String> getAntiClockNonLoop() {
        return AntiClockNonLoop;
    }

    public void setAntiClockNonLoop(List<String> antiClockNonLoop) {
        AntiClockNonLoop = antiClockNonLoop;
    }

    public List<String> getClockLoop() {
        return ClockLoop;
    }

    public void setClockLoop(List<String> clockLoop) {
        ClockLoop = clockLoop;
    }

    public List<String> getClockNonLoop() {
        return ClockNonLoop;
    }

    public void setClockNonLoop(List<String> clockNonLoop) {
        ClockNonLoop = clockNonLoop;
    }

    public List<String> getVacAntiClockLoop() {
        return VacAntiClockLoop;
    }

    public void setVacAntiClockLoop(List<String> vacAntiClockLoop) {
        VacAntiClockLoop = vacAntiClockLoop;
    }

    public List<String> getVacAntiClockNonLoop() {
        return VacAntiClockNonLoop;
    }

    public void setVacAntiClockNonLoop(List<String> vacAntiClockNonLoop) {
        VacAntiClockNonLoop = vacAntiClockNonLoop;
    }

    public List<String> getVacClockLoop() {
        return VacClockLoop;
    }

    public void setVacClockLoop(List<String> vacClockLoop) {
        VacClockLoop = vacClockLoop;
    }

    public List<String> getVacClockNonLoop() {
        return VacClockNonLoop;
    }

    public void setVacClockNonLoop(List<String> vacClockNonLoop) {
        VacClockNonLoop = vacClockNonLoop;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
