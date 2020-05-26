package it.angelic.growlroom.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Board {
	public Board() {
		super();

	}

	public Board(int boardId) {
		super();
		this.boardId = boardId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int boardId;

	@OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	List<Sensor> boardSensors;

	@OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	List<Actuator> boardActuators;

	public int getBoardId() {
		return boardId;
	}

	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}

	public List<Sensor> getBoardSensors() {
		return boardSensors;
	}

	public void setBoardSensors(List<Sensor> boardSensors) {
		this.boardSensors = boardSensors;
	}

	public List<Actuator> getBoardActuators() {
		return boardActuators;
	}

	public void setBoardActuators(List<Actuator> boardActuators) {
		this.boardActuators = boardActuators;
	}

}
