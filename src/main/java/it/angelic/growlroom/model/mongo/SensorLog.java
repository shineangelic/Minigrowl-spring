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
@Document(collection = "sensorsV2")
public class SensorLog {

	@Transient
	public static final String SEQUENCE_NAME = "sensorsv2_sequence";


	@Id
	private Long logId;

	private Integer pid;
	private SensorEnum typ;
	private Float val;
	private UnitEnum uinit;
	private Date timeStamp;
	private boolean err;
	private Long boardId;
	private Long sensorId;
	
	

	public SensorLog() {
		super();
	}

	public SensorLog(Sensor updated) {
		pid = updated.getPid();
		typ = updated.getTyp();
		sensorId = updated.getSensorId();
		val = Float.valueOf(updated.getVal());
		timeStamp = updated.getTimeStamp();
		err = updated.isErr();
		boardId = updated.getBoard().getBoardId();
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer id) {
		this.pid = id;
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
		return this.getClass().getSimpleName() + "-" + getPid();
	}

	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public Long getBoardId() {
		return boardId;
	}

	public void setBoardId(Long boardId) {
		this.boardId = boardId;
	}

	public Long getSensorId() {
		return sensorId;
	}

	public void setSensorId(Long sensorId) {
		this.sensorId = sensorId;
	}

}
