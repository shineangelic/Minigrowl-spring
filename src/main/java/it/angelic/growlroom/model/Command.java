package it.angelic.growlroom.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Singolo Comando reale quale spegni luici o set temperatura
 * @author Ale
 *
 */
@Entity
public class Command implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2086213865524090687L;
	@Id
	private String name;
	@JsonProperty("val")
	private String parameter;

	//dispositivo destinatario
	private int targetActuator;

	public Command(String name, String parameter) {
		super();
		this.name = name;
		this.parameter = parameter;
	}

	public Command() {
		super();
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTargetActuator() {
		return targetActuator;
	}

	public void setTargetActuatorId(int targetActuator) {
		this.targetActuator = targetActuator;
	}

}
