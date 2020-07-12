package it.angelic.growlroom.model;

public class HourValuePair implements Comparable<HourValuePair> {

	private String hour;
	private String value;
	private String min;
	private String max;

	public HourValuePair(String id, String value) {
		super();
		this.hour = id;
		this.value = value;
		// brr arrivare ai quaranta e fare ste robe
		max = "" + Float.MIN_VALUE;
		min = "" + Float.MAX_VALUE;
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
		return hour.compareTo(o.getId());
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}
}
