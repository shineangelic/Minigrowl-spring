package it.angelic.growlroom.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@Entity 
public class Sensor {
	@Id
	private Integer id;
	private SensorEnum typ;
	private String val;
	private UnitEnum uinit;
	private Date timeStamp;
	private boolean err;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "board_id", nullable = false)
	private Board board;

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
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "-" + getId();
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
