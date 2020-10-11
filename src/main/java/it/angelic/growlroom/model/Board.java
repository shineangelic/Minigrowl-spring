package it.angelic.growlroom.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Board implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1142251055437613043L;

	@Column(nullable=false)
	private Date timeStampCreated;
	
	
	public Board() {
		super();
		boardActuators = new ArrayList<Actuator>();
		boardSensors = new ArrayList<Sensor>();
	}

	public Board(Long boardId) {
		this.boardId = boardId;
		boardActuators = new ArrayList<Actuator>();
		boardSensors = new ArrayList<Sensor>();
	}

	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@SequenceGenerator(name = "board_id_seq", sequenceName = "board_id_board_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "board_id_seq")
	@Column(columnDefinition="serial")
	private Long boardId;

	@OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	List<Sensor> boardSensors;

	@OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	List<Actuator> boardActuators;
	
	public Date getTimeStampCreated() {
		return timeStampCreated;
	}

	public void setTimeStampCreated(Date timeStampCreated) {
		this.timeStampCreated = timeStampCreated;
	}

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
