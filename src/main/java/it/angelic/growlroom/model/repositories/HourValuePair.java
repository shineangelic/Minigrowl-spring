package it.angelic.growlroom.model.repositories;

public class HourValuePair implements Comparable<HourValuePair> {

	private String id;
	private String value;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int compareTo(HourValuePair o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
