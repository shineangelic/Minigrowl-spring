package it.angelic.growlroom.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Board implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1142251055437613043L;

	public Board() {
		super();

	}

	public Board(Long boardId) {
		super();
		this.boardId = boardId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long boardId;

	@OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	List<Sensor> boardSensors;

	@OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	List<Actuator> boardActuators;

	public Long getBoardId() {
		return boardId;
	}

	public void setBoardId(Long boardId) {
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
