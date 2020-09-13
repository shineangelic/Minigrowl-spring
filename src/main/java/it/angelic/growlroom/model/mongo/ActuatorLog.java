package it.angelic.growlroom.model.mongo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.angelic.growlroom.model.Actuator;
import it.angelic.growlroom.model.ActuatorEnum;
import lombok.Data;

@Data
@Document(collection = "actuatorsV2")
public class ActuatorLog implements Serializable {

	private static final long serialVersionUID = 816445541126448L;
	@Transient
	public static final String SEQUENCE_NAME_ACTUATORS = "actuatorsv2_sequence";

	@Id
	private Long logId;

	private Long nextLogId;
	private Integer pid;
	private ActuatorEnum typ;
	@JsonProperty("val")
	private String reading;
	private short mode;
	private Date timeStamp;
	private boolean err;
	@JsonProperty("bid")
	private Long boardId;
	private Long actuatorId;

	public ActuatorLog() {
		super();
		err = false;
	}

	public ActuatorLog(Actuator updated) {
		pid = updated.getPid();
		actuatorId = updated.getActuatorId();
		reading = updated.getReading();
		timeStamp = updated.getTimeStamp();
		mode = updated.getMode();
		err = updated.isErrorPresent();
		typ = updated.getTyp();
		boardId = updated.getBoard().getBoardId();
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer id) {
		this.pid = id;
	}

	public ActuatorEnum getTyp() {
		return typ;
	}

	public void setTyp(ActuatorEnum type) {
		this.typ = type;
	}

	public String getReading() {
		return reading;
	}

	public void setReading(String reading) {
		this.reading = reading;
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

	public short getMode() {
		return mode;
	}

	public void setMode(short mode) {
		this.mode = mode;
	}

	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public Long getNextLogId() {
		return nextLogId;
	}

	public void setNextLogId(Long long1) {
		this.nextLogId = long1;
	}

	public Long getBoardId() {
		return boardId;
	}

	public void setBoardId(Long boardId) {
		this.boardId = boardId;
	}

	public Long getActuatorId() {
		return actuatorId;
	}

	public void setActuatorId(Long actuatorId) {
		this.actuatorId = actuatorId;
	}

}
