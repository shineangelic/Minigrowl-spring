package it.angelic.growlroom.model.mongo;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import it.angelic.growlroom.model.Sensor;
import it.angelic.growlroom.model.SensorEnum;
import it.angelic.growlroom.model.UnitEnum;
import lombok.Data;

@Data
@Document(collection = "sensors")
public class SensorLog {

	@Transient
	public static final String SEQUENCE_NAME = "users_sequence";


	@Id
	private Long logId;

	private Integer id;
	private SensorEnum typ;
	private Float val;
	private UnitEnum uinit;
	private Date timeStamp;
	private boolean err;
	
	

	public SensorLog() {
		super();
	}

	public SensorLog(Sensor updated) {
		id = updated.getId();
		typ = updated.getTyp();
		val = Float.valueOf(updated.getVal());
		timeStamp = updated.getTimeStamp();
		err = updated.isErr();
	}

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

	public Float getVal() {
		return val;
	}

	public void setVal(Float reading) {
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

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "-" + getId();
	}

	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

}
