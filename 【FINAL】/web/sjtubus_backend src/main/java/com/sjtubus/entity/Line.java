package com.sjtubus.entity;

import java.util.List;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "line")
public class Line {
	public Line() {}
	
    @Id
    private String id ;
    private String name;
    private String nameCN;
    private List<String> station;
    
    public String getId() {
    	return this.id;
    }
    public void setId(String id){
    	this.id = id;
    }
    public String getName() {
    	return this.name;
    }
    public void setName(String name) {
    	this.name = name;
    }
    public List<String> getStation() {
    	return this.station;
    }
    public void setStation(List<String> station) {
    	this.station = station;
    }
    public String getNameCN() {
        return nameCN;
    }
    public void setNameCN(String nameCN) {
        this.nameCN = nameCN;
    }
}
