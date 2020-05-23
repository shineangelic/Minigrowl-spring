package it.angelic.growlroom.service;

import java.util.Collection;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.angelic.growlroom.model.Actuator;
import it.angelic.growlroom.model.Command;
import it.angelic.growlroom.model.CommandsRepository;
import it.angelic.growlroom.model.UnitEnum;
import it.angelic.growlroom.model.mongo.ActuatorLog;
import it.angelic.growlroom.model.repositories.ActuatorsRepository;

@Service
public class ActuatorsServiceImpl implements ActuatorsService {

	@Autowired
	private CommandsRepository commandsRepository;

	@Autowired
	private ActuatorsRepository actuatorsRepository;
	
	@Autowired
	private MongoLogService mongoLogService;
	
	Logger logger = LoggerFactory.getLogger(ActuatorsServiceImpl.class);

	@Override
	public Actuator createOrUpdateActuator(Actuator dispositivo, String id) {
		if (!Integer.valueOf(id).equals(dispositivo.getId()))
			throw new IllegalArgumentException("PID Mismatch: " + id + " vs" + dispositivo.getId());

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
			com.setTargetActuatorId(Integer.valueOf(id));
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
