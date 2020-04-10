package it.angelic.growlroom.service;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.angelic.growlroom.model.Actuator;
import it.angelic.growlroom.model.ActuatorEnum;
import it.angelic.growlroom.model.ActuatorsRepository;
import it.angelic.growlroom.model.Command;
import it.angelic.growlroom.model.CommandsRepository;

@Service
public class ActuatorsServiceImpl implements ActuatorsService {

	@Autowired
	private CommandsRepository commandsRepository;
	
	@Autowired
	private ActuatorsRepository actuatorsRepository;

	@Override
	public Actuator createOrUpdateActuator(Actuator dispositivo, String id) {
		if (!Integer.valueOf(id).equals(dispositivo.getId()))
			throw new IllegalArgumentException("PID Mismatch: " + id + " vs" + dispositivo.getId());

		switch(dispositivo.getType()) {
		case FAN:
			dispositivo.setType(ActuatorEnum.LIGHT);
		case LIGHT:
			dispositivo.setType(ActuatorEnum.LIGHT);
		}
		
		for (Command com : dispositivo.getSupportedCommands()) {
			com.setTargetActuatorId(Integer.valueOf(id));
			commandsRepository.save(com);
		}

		dispositivo.setTimeStamp(new Date());
		return actuatorsRepository.save(dispositivo);
	}

	@Override
	public Collection<Actuator> getActuators() {
		return actuatorsRepository.findAll();
	}

 

}
