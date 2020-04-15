package it.angelic.growlroom.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum ActuatorEnum {
	FAN('F'), LIGHT('L'), HUMIDIFIER('M'), HVAC('H'), OUTTAKE('O');

	char typ;

	private ActuatorEnum(char inVal) {
		this.typ = inVal;
	}

	// @JsonValue
	public long getTyp() {
		return typ;
	}

	@JsonCreator
	public static ActuatorEnum forValues(@JsonProperty("name") String unit) {

		for (ActuatorEnum distance : ActuatorEnum.values()) {
			if (distance.getTyp() == Character.valueOf(unit.charAt(0))) {
				return distance;
			}
		}

		return null;
	}
}
