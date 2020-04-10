package it.angelic.growlroom.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Actuator implements Serializable {

	private static final long serialVersionUID = 8169740067541126448L;
	@Id
	private Integer id;
	@JsonProperty("name")
	private ActuatorEnum type;
	@JsonProperty("val")
	private String reading;
	private String name;
	private UnitEnum uinit;
	private Date timeStamp;
	@JsonProperty("err")
	private boolean errorPresent;

	@JsonProperty("cmds")
	@ElementCollection(targetClass = HashSet.class)
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

	public Set<Command> getSupportedCommands() {
		return supportedCommands;
	}

	public void setSupportedCommands(Set<Command> supportedCommands) {
		this.supportedCommands = supportedCommands;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "-" + getId();
	}

}
