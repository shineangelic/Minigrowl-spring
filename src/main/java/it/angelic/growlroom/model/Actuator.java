package it.angelic.growlroom.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Actuator implements Serializable {

	private static final long serialVersionUID = 8169740067541126448L;
	@Id
	private Integer id;
	private ActuatorEnum typ;
	@JsonProperty("val")
	private String reading;
	@JsonInclude(Include.NON_NULL)
	private String humanName;
	private UnitEnum uinit;
	@JsonProperty("mode")
	private short mode;
	private Date timeStamp;
	@JsonProperty("err")
	private boolean errorPresent;
	
	@JsonProperty("bid")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "board_id", nullable = false)
	private Board board;

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

	public ActuatorEnum getTyp() {
		return typ;
	}

	public void setTyp(ActuatorEnum type) {
		this.typ = type;
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

	public String getHumanName() {
		return humanName;
	}

	public void setHumanName(String name) {
		this.humanName = name;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "-" + getId();
	}

	public short getMode() {
		return mode;
	}

	public void setMode(short mode) {
		this.mode = mode;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Actuator other = (Actuator) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
