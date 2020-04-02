package it.angelic.growlroom.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Actuator implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8169740067541126448L;
	private Integer id;
	private ActuatorEnum type;
	private String reading;
	private UnitEnum uinit;
	private Date timeStamp;
	private boolean errorPresent;
	private Set<Command> supportedCommands;
	
	public Actuator() {
		super();
		errorPresent = false;
		supportedCommands = new HashSet<>();
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public ActuatorEnum getType() {
		return type;
	}
	public void setType(ActuatorEnum type) {
		this.type = type;
	}
	public String getReading() {
		return reading;
	}
	public void setReading(String reading) {
		this.reading = reading;
	}
	public UnitEnum getUinit() {
		return uinit;
	}
	public void setUinit(UnitEnum uinit) {
		this.uinit = uinit;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	public boolean isErrorPresent() {
		return errorPresent;
	}
	public void setErrorPresent(boolean errorPresent) {
		this.errorPresent = errorPresent;
	}
	
	
	
	
	
	
	
}
