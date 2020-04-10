package it.angelic.growlroom.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Singolo Comando reale quale spegni luici o set temperatura
 * la chiave e` data anche dal disp a cui e` associato
 * @author Ale
 *
 */
@Entity
@IdClass(CompositeCommandKey.class)
public class Command implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2086213865524090687L;
	
	private String name;
	
	@JsonProperty("val")
	@Id
	private String parameter;
	
	private Long idOnQueue;

	//dispositivo destinatario
	@Id
	private int targetActuator;
	
	@Override
	public String toString() {
		return   this.getClass().getSimpleName() + "-" +  targetActuator+"->"+parameter ;
	}

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

	public Long getIdOnQueue() {
		return idOnQueue;
	}

	public void setIdOnQueue(Long idOnQueue) {
		this.idOnQueue = idOnQueue;
	}

}
