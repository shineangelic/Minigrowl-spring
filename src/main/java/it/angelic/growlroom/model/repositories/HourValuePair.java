package it.angelic.growlroom.model.repositories;

public class HourValuePair implements Comparable<HourValuePair> {

	private String hour;
	private String value;

	 

	public HourValuePair(String id, String value) {
		super();
		this.hour = id;
		this.value = value;
	}

	public String getId() {
		return hour;
	}

	public void setId(String id) {
		this.hour = id;
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
