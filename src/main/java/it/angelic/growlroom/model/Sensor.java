package it.angelic.growlroom.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "board_id", "pid" }) })
public class Sensor {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long sensorId;

	@JsonProperty("id")
	private Integer pid;
	private SensorEnum typ;
	private String val;
	private UnitEnum uinit;
	private Date timeStamp;
	private boolean err;

	@JsonProperty("bid")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "board_id", nullable = false)
	private Board board;

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

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "-" + getPid();
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pid == null) ? 0 : pid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sensor other = (Sensor) obj;
		if (pid == null) {
			if (other.pid != null)
				return false;
		} else if (!pid.equals(other.pid))
			return false;
		return true;
	}

	public Long getSensorId() {
		return sensorId;
	}

	public void setSensorId(Long sensorId) {
		this.sensorId = sensorId;
	}

}
