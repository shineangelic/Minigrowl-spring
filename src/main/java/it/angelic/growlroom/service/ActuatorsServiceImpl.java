package it.angelic.growlroom.service;

import java.util.Collection;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.angelic.growlroom.model.Actuator;
import it.angelic.growlroom.model.Board;
import it.angelic.growlroom.model.Command;
import it.angelic.growlroom.model.UnitEnum;
import it.angelic.growlroom.model.mongo.ActuatorLog;
import it.angelic.growlroom.model.repositories.ActuatorsRepository;

@Service
public class ActuatorsServiceImpl implements ActuatorsService {

	@Autowired
	private ActuatorsRepository actuatorsRepository;

	@Autowired
	private MongoLogService mongoLogService;
	
	@Autowired
	private BoardsService boardsService;
	
	Logger logger = LoggerFactory.getLogger(ActuatorsServiceImpl.class);

	@Override
	public Actuator createOrUpdateBoardActuator(Actuator dispositivo, String boardId, String id) {
		if (!Integer.valueOf(id).equals(dispositivo.getPid()))
			throw new IllegalArgumentException("PID Mismatch: " + id + " vs " + dispositivo.getPid());

		Actuator previous = actuatorsRepository.findByBoardIdAndPid(Long.valueOf(boardId), dispositivo.getPid());
		Actuator updated = null;
		Board tboard= boardsService.findOrCreateBoard( boardId );
		if (previous == null) {
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
			dispositivo.setTimeStampCreated(new Date());
			dispositivo.setBoard(tboard);

			for (Command com : dispositivo.getSupportedCommands()) {
				com.setTargetActuator(dispositivo);
				// commandsRepository.save(com);
			}
			
			updated = actuatorsRepository.save(dispositivo);
			logger.warn("Created new ACTUATOR id:" + updated.getActuatorId());
		} else {
			dispositivo.setBoard(tboard);
			previous.setReading(dispositivo.getReading());
			previous.setMode(dispositivo.getMode());
			previous.setTimeStamp(new Date());
			previous.setErrorPresent(dispositivo.isErrorPresent());
			updated = actuatorsRepository.save(previous);
		}
		
		boardsService.assign(tboard, updated);

		// mongo log
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

	@Override
	public Collection<Actuator> getBoardActuators(Long t2) {
		return actuatorsRepository.findByBoardId(t2);
	}

}
