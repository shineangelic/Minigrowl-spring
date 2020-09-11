package it.angelic.growlroom.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import it.angelic.growlroom.model.Board;
import it.angelic.growlroom.model.Sensor;
import it.angelic.growlroom.model.UnitEnum;
import it.angelic.growlroom.model.mongo.SensorLog;
import it.angelic.growlroom.model.repositories.BoardsRepository;
import it.angelic.growlroom.model.repositories.SensorsRepository;

@Service
public class SensorsServiceImpl implements SensorsService {

	@Autowired
	private SensorsRepository sensorRepository;

	@Autowired
	private MongoLogService mongoLogService;

	@Autowired
	private BoardsRepository boardsRepository;

	private final SimpMessagingTemplate simpMessagingTemplate;

	Logger logger = LoggerFactory.getLogger(SensorsServiceImpl.class);

	public SensorsServiceImpl(SimpMessagingTemplate simpMessagingTemplate) {
		this.simpMessagingTemplate = simpMessagingTemplate;
	}

	@Override
	public Sensor createOrUpdateBoardSensor(Sensor sensing, String boardId, String checkId) {
		if (!Integer.valueOf(checkId).equals(sensing.getPid()))
			throw new IllegalArgumentException("PID Mismatch: " + checkId + " vs" + sensing.getPid());

		Sensor previous = sensorRepository.findByBoardIdAndPid(Long.valueOf(boardId), sensing.getPid());
		Sensor updated;
		if (previous == null) {
			Board tboard = findOrCreateBoard(boardId);

			switch (sensing.getTyp()) {
			case BAROMETER:
				sensing.setUinit(UnitEnum.MILLIBAR);
				break;
			case TEMPERATURE:
				sensing.setUinit(UnitEnum.CELSIUS);
				break;
			case HUMIDITY:
				sensing.setUinit(UnitEnum.PERCENT);
				break;
			case LIGHT:
				sensing.setUinit(UnitEnum.LUMEN);
				break;
			case WATER_RESERVE:
				sensing.setUinit(UnitEnum.LITER);
				break;
			default:
				break;
			}

			sensing.setBoard(tboard);
			sensing.setTimeStamp(new Date());
			updated = sensorRepository.save(sensing);
			if (!tboard.getBoardSensors().contains(updated)) {
				tboard.getBoardSensors().add(updated);
				boardsRepository.save(tboard);
			}
			logger.warn("Created new SENSOR id:" + updated.getSensorId());
		} else {// update its readings

			if (sensing.getTyp().equals(previous.getTyp())) {//BUGHUNT

				previous.setReading(sensing.getReading());
				previous.setTimeStamp(new Date());
				previous.setErr(sensing.isErr());
				updated = sensorRepository.save(previous);

				// mongo logging
				if (!updated.isErr()) {
					try {
						SensorLog grimpl = new SensorLog(updated);
						mongoLogService.logSensor(grimpl);
					} catch (Exception e) {
						logger.error("MongoDB exc: " + e.getMessage());
					}
				} else {
					logger.warn("NON-logging erratic sensor: " + updated.getSensorId());
				}
			}else {
				logger.error("GRAVE sensor id mismatch sensing: " +sensing.toString() + " previous: " + previous.toString());
				updated = previous;
			}
		}
		// avvisa i sottoscrittori dei sensori
		this.simpMessagingTemplate.convertAndSend("/topic/sensors", updated);

		return updated;
	}

	private Board findOrCreateBoard(String boardId) {
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
	public Sensor getSensorByBoardIdAndPid(Long boardId, Integer sensorPid) throws SensorNotFoundException {
		Sensor opt = sensorRepository.findByBoardIdAndPid(boardId, sensorPid);
		if (opt != null)
			return opt;

		throw new SensorNotFoundException();
	}

	@Override
	public Collection<Sensor> getBoardSensors(Integer boardId) {

		return sensorRepository.findByBoardId(boardId.longValue());
	}

}
