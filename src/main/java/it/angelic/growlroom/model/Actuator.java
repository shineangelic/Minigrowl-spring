package it.angelic.growlroom.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "board_id", "pid" }) })
public class Actuator implements Serializable {

	private static final long serialVersionUID = 8169740067541126448L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long actuatorId;
	
	@JsonProperty("id")
	private Integer pid;
	
	private ActuatorEnum typ;
	
	@JsonProperty("val")
	private String reading;
	
	private UnitEnum uinit;

	@JsonProperty("mode")
	private short mode;

	private Date timeStamp;

	@JsonProperty("err")
	private boolean errorPresent;

	@JsonProperty("bid")
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "board_id", nullable = false)
	private Board board;

	@JsonProperty("cmds")
	@OneToMany(mappedBy = "targetActuator", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Command> supportedCommands;

	public Actuator() {
		super();
	}

	public Actuator(String fromJackson) {
		super();
		this.actuatorId = Long.valueOf(fromJackson);
		errorPresent = false;
		supportedCommands = new ArrayList<>();
	}

	/**
	 * Serve a jackson
	 * 
	 * @param actuatorId
	 */
	public Actuator(Long actuatorId) {
		super();
		this.actuatorId = actuatorId;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer id) {
		this.pid = id;
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

	public List<Command> getSupportedCommands() {
		return supportedCommands;
	}

	public void setSupportedCommands(List<Command> supportedCommands) {
		this.supportedCommands = supportedCommands;
	}

	@Override
	public String toString() {
		StringBuilder stb = new StringBuilder(this.getClass().getSimpleName());
		stb.append("-").append(getActuatorId());
		if (board != null)
			stb.append(" - board ").append(board.getBoardId());
		stb.append(" - pid ").append(pid).append("");
		return stb.toString();
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

	public Long getActuatorId() {
		return actuatorId;
	}

	public void setActuatorId(Long actuatorId) {
		this.actuatorId = actuatorId;
	}

	public Command containsCommand(Command toExecurte) {
		for (Command command : supportedCommands) {
			if (toExecurte.getTargetActuator().getActuatorId().equals(command.getTargetActuator().getActuatorId())
					&& toExecurte.getParameter().equals(command.getParameter()))
				return command;
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actuatorId == null) ? 0 : actuatorId.hashCode());
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
		if (actuatorId == null) {
			if (other.actuatorId != null)
				return false;
		} else if (!actuatorId.equals(other.actuatorId))
			return false;
		return true;
	}

}
