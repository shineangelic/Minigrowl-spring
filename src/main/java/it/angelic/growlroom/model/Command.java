package it.angelic.growlroom.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Singolo Comando reale quale spegni luici o set temperatura la chiave e` data anche dal disp a cui e` associato
 * 
 * @author Ale
 *
 */
@Entity
@Table(uniqueConstraints={
	    @UniqueConstraint(columnNames = {"parameter", "targetActuator"})
	}) 
public class Command implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2086213865524090687L;

	private String name;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private Long commandId;
	
	@JsonProperty("val")
	private String parameter;
	@JsonInclude(Include.NON_NULL)
	@Transient
	private Long idOnQueue;

	// dispositivo destinatario
 
	@JsonProperty("tgt")
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "targetActuator", nullable = false)
	//@JsonBackReference
	@JsonSerialize(using = IntActuatorCommandPidSerializer.class)
	@JsonDeserialize(using = ActuatorDeserializer.class)
	private Actuator targetActuator;

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "-tgt: " + targetActuator + " val:" + parameter;
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

	public Actuator getTargetActuator() {
		return targetActuator;
	}

	public void setTargetActuator(Actuator targetActuator) {
		this.targetActuator = targetActuator;
	}

	public Long getIdOnQueue() {
		return idOnQueue;
	}

	public void setIdOnQueue(Long idOnQueue) {
		this.idOnQueue = idOnQueue;
	}

	 

	public Long getCommandId() {
		return commandId;
	}

	public void setCommandId(Long commandId) {
		this.commandId = commandId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((commandId == null) ? 0 : commandId.hashCode());
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
		Command other = (Command) obj;
		if (commandId == null) {
			if (other.commandId != null)
				return false;
		} else if (!commandId.equals(other.commandId))
			return false;
		return true;
	}

}
