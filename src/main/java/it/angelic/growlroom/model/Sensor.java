package it.angelic.growlroom.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "board_id", "pid" }) })
public class Sensor {
	@Id
	@SequenceGenerator(name = "sensor_id_seq", sequenceName = "sensor_sensor_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sensor_id_seq")
	@Column(columnDefinition="serial")
	private Long sensorId;

	@JsonProperty("id")
	private Integer pid;
	
	private SensorEnum typ;
	
	@JsonProperty("val")
	private String reading;
	
	private UnitEnum uinit;
	@Column(nullable=false)
	private Date timeStamp;
	@Column(nullable=false)
	private Date timeStampCreated;
	
	private boolean err;

	@JsonProperty("bid")
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
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

	public String getReading() {
		return reading;
	}

	public void setReading(String reading) {
		this.reading = reading;
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
		return this.getClass().getSimpleName() + ":" + getSensorId() +" PID:"+ getPid();
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public Long getSensorId() {
		return sensorId;
	}

	public void setSensorId(Long sensorId) {
		this.sensorId = sensorId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sensorId == null) ? 0 : sensorId.hashCode());
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
		if (sensorId == null) {
			if (other.sensorId != null)
				return false;
		} else if (!sensorId.equals(other.sensorId))
			return false;
		return true;
	}

	public Date getTimeStampCreated() {
		return timeStampCreated;
	}

	public void setTimeStampCreated(Date timeStampCreated) {
		this.timeStampCreated = timeStampCreated;
	}

}
