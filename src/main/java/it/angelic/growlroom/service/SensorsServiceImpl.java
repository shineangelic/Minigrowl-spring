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

import it.angelic.growlroom.controllers.MongoSensorController;
import it.angelic.growlroom.model.Sensor;
import it.angelic.growlroom.model.SensorLog;
import it.angelic.growlroom.model.UnitEnum;
import it.angelic.growlroom.model.repositories.SensorsRepository;
@Service
public class SensorsServiceImpl implements SensorsService {

	@Autowired
	private SensorsRepository sensorRepository;

	@Autowired
	private MongoSensorController mongoSensorController;

	private final SimpMessagingTemplate simpMessagingTemplate;
	
	Logger logger = LoggerFactory.getLogger(SensorsServiceImpl.class);

	public SensorsServiceImpl(SimpMessagingTemplate simpMessagingTemplate) {
		this.simpMessagingTemplate = simpMessagingTemplate;
	}

	@Override
	public Sensor createOrUpdateSensor(Sensor sensing, String checkId) {
		float dbs = -1f;
		try {
			dbs =Float.valueOf(getSensorById(sensing.getId()).getVal());
		} catch (SensorNotFoundException e) {
			logger.warn("Sensore non Trovato? " + sensing.getId());
		}

		Sensor updated = createSensorImpl(sensing, checkId);
		//mando a mongo e al front-end sse cambiato
		if ( dbs!= Float.valueOf(sensing.getVal())) {
			try {
				mongoSensorController.logSensor(new SensorLog(updated));
			} catch (Exception e) {
				logger.warn("MongoDB exc: "+ e.getMessage());
			}
			
			// avvisa i sottoscrittori dei sensori
			this.simpMessagingTemplate.convertAndSend("/topic/sensors", updated);
		}
		return updated;
	}

	public Sensor createSensorImpl(Sensor sensing, String checkId) {
		if (!Integer.valueOf(checkId).equals(sensing.getId()))
			throw new IllegalArgumentException("PID Mismatch: " + checkId + " vs" + sensing.getId());

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
	public Sensor getSensorById(Integer sensorPid) throws SensorNotFoundException {
		Optional<Sensor> opt = sensorRepository.findById(sensorPid);
		if (opt.isPresent())
			return opt.get();

		throw new SensorNotFoundException();
	}

}
