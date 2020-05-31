package it.angelic.growlroom.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.angelic.growlroom.model.Actuator;
import it.angelic.growlroom.model.Board;
import it.angelic.growlroom.model.Command;
import it.angelic.growlroom.model.CommandsRepository;
import it.angelic.growlroom.model.UnitEnum;
import it.angelic.growlroom.model.mongo.ActuatorLog;
import it.angelic.growlroom.model.repositories.ActuatorsRepository;
import it.angelic.growlroom.model.repositories.BoardsRepository;

@Service
public class ActuatorsServiceImpl implements ActuatorsService {

	@Autowired
	private CommandsRepository commandsRepository;

	@Autowired
	private ActuatorsRepository actuatorsRepository;

	@Autowired
	private BoardsRepository boardsRepository;

	@Autowired
	private MongoLogService mongoLogService;

	Logger logger = LoggerFactory.getLogger(ActuatorsServiceImpl.class);

	@Override
	public Actuator createOrUpdateBoardActuator(Actuator dispositivo, String boardId, String id) {
		if (!Integer.valueOf(id).equals(dispositivo.getPid()))
			throw new IllegalArgumentException("PID Mismatch: " + id + " vs " + dispositivo.getPid());
		 

		Actuator previous = actuatorsRepository.findByBoardIdAndPid(Long.valueOf(boardId), dispositivo.getPid());
		Actuator updated = null;
		if (previous == null) {
			Board tboard;
			try {
				tboard = boardsRepository.findByBoardId(Long.valueOf(boardId));
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("boardId ERR: " + e.getMessage());
			}
			if (tboard == null) {
				tboard = new Board(Long.valueOf(boardId));
				tboard.setBoardActuators(new ArrayList<>());
				tboard = boardsRepository.save(tboard);
			}

			switch (dispositivo.getTyp()) {
			case FAN:
			case OUTTAKE:
			case LIGHT:
				dispositivo.setUinit(UnitEnum.TURNED_ON);
				break;
			case HUMIDIFIER:
				// reserve tank?
				dispositivo.setUinit(UnitEnum.LITER);
				break;
			case HVAC:
				dispositivo.setUinit(UnitEnum.CELSIUS);
				break;
			}

			dispositivo.setTimeStamp(new Date());
			dispositivo.setBoard(tboard);

			for (Command com : dispositivo.getSupportedCommands()) {
				com.setTargetActuator(dispositivo);
				// commandsRepository.save(com);
			}
			updated = actuatorsRepository.save(dispositivo);
			if (!tboard.getBoardActuators().contains(dispositivo)) {
				tboard.getBoardActuators().add(dispositivo);
				tboard = boardsRepository.save(tboard);
			}
			if (!updated.isErrorPresent()) {

				

				try {
					mongoLogService.logActuator(new ActuatorLog(updated));
				} catch (Exception e) {
					logger.warn("MongoDB exc: " + e.getMessage());
				}
			}
		} else {
			previous.setReading(dispositivo.getReading());
			previous.setTimeStamp(new Date());
			previous.setErrorPresent(dispositivo.isErrorPresent());
			updated = actuatorsRepository.save(previous);
		}

		return updated;
	}

	@Override
	public Actuator createOrUpdateActuator(Actuator dispositivo, String id) {
		if (!Integer.valueOf(id).equals(dispositivo.getPid()))
			throw new IllegalArgumentException("PID Mismatch: " + id + " vs" + dispositivo.getPid());

		switch (dispositivo.getTyp()) {
		case FAN:
		case OUTTAKE:
		case LIGHT:
			dispositivo.setUinit(UnitEnum.TURNED_ON);
			break;
		case HUMIDIFIER:
			// reserve tank?
			dispositivo.setUinit(UnitEnum.LITER);
			break;
		case HVAC:
			dispositivo.setUinit(UnitEnum.CELSIUS);
			break;
		}

		for (Command com : dispositivo.getSupportedCommands()) {
			com.setTargetActuator(dispositivo);
			commandsRepository.save(com);
		}

		dispositivo.setTimeStamp(new Date());
		Actuator updated = actuatorsRepository.save(dispositivo);
		if (!updated.isErrorPresent()) {
			try {
				mongoLogService.logActuator(new ActuatorLog(updated));
			} catch (Exception e) {
				logger.warn("MongoDB exc: " + e.getMessage());
			}
		}

		return updated;
	}

	@Override
	public Collection<Actuator> getActuators() {
		return actuatorsRepository.findAll();
	}

}
