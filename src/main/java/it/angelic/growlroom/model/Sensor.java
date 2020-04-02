package it.angelic.growlroom.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
@Entity
public class Sensor {
	@Id
	private Integer id;
	private SensorEnum typ;
	private String val;
	private UnitEnum uinit;
	private Date timeStamp;
	private boolean err;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SensorEnum getTyp() {
		return typ;
	}

	public void setTyp(SensorEnum type) {
		this.typ = type;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String reading) {
		this.val = reading;
	}

	public UnitEnum getUinit() {
		return uinit;
	}

	public void setUinit(UnitEnum uinit) {
		this.uinit = uinit;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public boolean isErr() {
		return err;
	}

	public void setErr(boolean errorPresent) {
		this.err = errorPresent;
	}

}
