package it.angelic.growlroom.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UnitEnum {
	CELSIUS("°"), FAHRNEIT("f"), PERCENT("%"), LUMEN("Lux"), MILLIBAR("mb"), LITER("lt"), TURNED_ON("O");
	String unitSymbol;

	private UnitEnum(String unitSymbol) {
		this.unitSymbol = unitSymbol;
	}
	@JsonValue
	public String getUnitSymbol() {
		return unitSymbol;
	}
}
