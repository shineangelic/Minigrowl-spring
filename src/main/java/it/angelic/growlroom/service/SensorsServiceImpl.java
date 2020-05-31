package it.angelic.growlroom.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
	public Sensor createOrUpdateSensor(Sensor sensing, String checkId) {
		float dbs = -1f;
		try {
			dbs = Float.valueOf(getSensorByPid(sensing.getPid()).getVal());
		} catch (SensorNotFoundException e) {
			logger.warn("Sensore non Trovato? " + sensing.getPid());
		}

		Sensor updated = createSensorImpl(sensing, checkId);
		if (!updated.isErr()) {
			try {
				mongoLogService.logSensor(new SensorLog(updated));
			} catch (Exception e) {
				logger.warn("MongoDB exc: " + e.getMessage());
			}
		}
		// avvisa i sottoscrittori dei sensori
		this.simpMessagingTemplate.convertAndSend("/topic/sensors", updated);
		// }
		return updated;
	}

	@Override
	public Sensor createOrUpdateBoardSensor(Sensor sensing, String boardId, String checkId) {
		if (!Integer.valueOf(checkId).equals(sensing.getPid()))
			throw new IllegalArgumentException("PID Mismatch: " + checkId + " vs" + sensing.getPid());
		Sensor previous = sensorRepository.findByBoardIdAndPid(Long.valueOf(boardId), sensing.getPid());
		Sensor updated;
		if (previous == null) {
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
			if (!tboard.getBoardSensors().contains(sensing)) {
				tboard.getBoardSensors().add(sensing);
				boardsRepository.save(tboard);
			}
			if (!updated.isErr()) {

				try {
					mongoLogService.logSensor(new SensorLog(updated));
				} catch (Exception e) {
					logger.warn("MongoDB exc: " + e.getMessage());
				}
			}
			return updated;
		} else {
			previous.setVal(sensing.getVal());
			previous.setTimeStamp(new Date());
			previous.setErr(sensing.isErr());
			return sensorRepository.save(previous);
		}
	}

	public Sensor createSensorImpl(Sensor sensing, String checkId) {
		if (!Integer.valueOf(checkId).equals(sensing.getPid()))
			throw new IllegalArgumentException("PID Mismatch: " + checkId + " vs" + sensing.getPid());

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

		sensing.setTimeStamp(new Date());
		return sensorRepository.save(sensing);
	}

	@Override
	public Collection<Sensor> getSensors() {
		List<Sensor> ret = new ArrayList<>();
		Iterable<Sensor> res = sensorRepository.findAll();
		for (Sensor sensor : res) {
			ret.add(sensor);
		}

		return ret;
	}

	@Override
	public Sensor getSensorByBoardIdAndPid(Long boardId, Integer sensorPid) throws SensorNotFoundException {
		Sensor opt = sensorRepository.findByBoardIdAndPid(boardId, sensorPid);
		if (opt != null)
			return opt;

		throw new SensorNotFoundException();
	}

	@Override
	public Sensor getSensorByPid(Integer sensorPid) throws SensorNotFoundException {
		Optional<Sensor> opt = sensorRepository.findByPid(sensorPid);
		if (opt.isPresent())
			return opt.get();

		throw new SensorNotFoundException();
	}

	@Override
	public Collection<Sensor> getBoardSensors(Integer boardId) {
		
		return sensorRepository.findByBoardId(boardId.longValue());
	}

}
