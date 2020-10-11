package it.angelic.growlroom.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import it.angelic.growlroom.model.Actuator;
import it.angelic.growlroom.model.Board;
import it.angelic.growlroom.model.Sensor;
import it.angelic.growlroom.model.repositories.BoardsRepository;

public class BoardsServiceImpl implements BoardsService {

	@Autowired
	private BoardsRepository boardsRepository;

	@Override
	public Board findOrCreateBoard(String boardId) {
		Board tboard;
		try {
			tboard = boardsRepository.findByBoardId(Long.valueOf(boardId));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("boardId ERR: " + e.getMessage());
		}
		if (tboard == null) {
			tboard = new Board(Long.valueOf(boardId));
			tboard.setBoardSensors(new ArrayList<>());
			tboard = boardsRepository.save(tboard);
		}
		return tboard;
	}

	@Override
	public void assign(Board owner, Sensor updated) {
		if (!owner.getBoardSensors().contains(updated)) {
			owner.getBoardSensors().add(updated);
			boardsRepository.save(owner);
		}

	}

	@Override
	public void assign(Board owner, Actuator dispositivo) {
		if (!owner.getBoardActuators().contains(dispositivo)) {
			owner.getBoardActuators().add(dispositivo);
			boardsRepository.save(owner);
		}

	}
}