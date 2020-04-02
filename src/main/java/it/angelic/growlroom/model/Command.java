package it.angelic.growlroom.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Command {
	@Id
	private String actuatorId;
	private CommandEnum cmdType;
	private String parameter;

	public Command(String actuatorId, CommandEnum cmdType, String parameter) {
		super();
		this.actuatorId = actuatorId;
		this.cmdType = cmdType;
		this.parameter = parameter;
	}
	
	

	public Command() {
		super();
	}



	public String getActuatorId() {
		return actuatorId;
	}

	public void setActuatorId(String actuatorId) {
		this.actuatorId = actuatorId;
	}

	public CommandEnum getCmdType() {
		return cmdType;
	}

	public void setCmdType(CommandEnum cmdType) {
		this.cmdType = cmdType;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

}
