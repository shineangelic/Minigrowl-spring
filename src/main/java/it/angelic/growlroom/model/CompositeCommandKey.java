package it.angelic.growlroom.model;

import java.io.Serializable;

public class CompositeCommandKey implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1674102942826153532L;
	private String name;
	private int targetActuator;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		CompositeCommandKey other = (CompositeCommandKey) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (targetActuator != other.targetActuator)
			return false;
		return true;
	}
	
	
}