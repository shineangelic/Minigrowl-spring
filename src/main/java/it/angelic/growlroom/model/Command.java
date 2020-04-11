package it.angelic.growlroom.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Singolo Comando reale quale spegni luici o set temperatura la chiave e` data anche dal disp a cui e` associato
 * 
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
	@JsonInclude(Include.NON_NULL)
	private Long idOnQueue;

	// dispositivo destinatario
	@Id
	private int targetActuator;

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "-" + targetActuator + "->" + parameter;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((parameter == null) ? 0 : parameter.hashCode());
		result = prime * result + targetActuator;
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
		if (parameter == null) {
			if (other.parameter != null)
				return false;
		} else if (!parameter.equals(other.parameter))
			return false;
		if (targetActuator != other.targetActuator)
			return false;
		return true;
	}

}
