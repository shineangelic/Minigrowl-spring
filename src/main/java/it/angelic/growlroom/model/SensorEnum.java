package it.angelic.growlroom.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SensorEnum {

	TEMPERATURE('T'), HUMIDITY('H'), 
	LIGHT('L'), POWER('P'), MOISTURE('M'), WATER_RESERVE('W');
	private SensorEnum(char inVal) {
		this.typ = inVal;
	}

	char typ;

	
	@JsonValue
	public long getTyp() {
		return typ;
	}

	@JsonCreator
	public static SensorEnum forValues(@JsonProperty("typ") String unit) {
		
		for (SensorEnum distance : SensorEnum.values()) {
			if (distance.getTyp() == Character.valueOf(unit.charAt(0))) {
				return distance;
			}
		}

		return null;
	}
	 
}
