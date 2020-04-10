package it.angelic.growlroom.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.angelic.growlroom.model.Actuator;
import it.angelic.growlroom.model.ActuatorsRepository;
import it.angelic.growlroom.model.Command;
import it.angelic.growlroom.model.CommandsRepository;
import it.angelic.growlroom.model.QueueCommands;
import it.angelic.growlroom.model.QueueCommandsRepository;
import it.angelic.growlroom.model.SensorsRepository;

@Service
public class CommandsServiceImpl implements CommandsService {

	@Autowired
	ActuatorsRepository actuatorsRepository;

	@Autowired
	CommandsRepository commandRepository;

	@Autowired
	SensorsRepository sensorsRepository;

	@Autowired
	QueueCommandsRepository queueCommands;

	/**
	 * Creates a new command to be executed
	 */
	@Override
	public Command createOrUpdateCommand(Command product) {
		return commandRepository.save(product);
	}

	@Override
	public Collection<Command> getUnexecutedCommands() {
		ArrayList<Command> ret = new ArrayList<>();
		List<QueueCommands> pes = queueCommands.findAll();
		for (QueueCommands queueCommands : pes) {
			Command actualCmd = queueCommands.getToExecute();
			actualCmd.setIdOnQueue(queueCommands.getIdQueueCommand());
			ret.add(actualCmd);
		}
		return ret;
	}

	@Override
	public Long sendCommand(Command toExecurte) {

		int targetActuator = toExecurte.getTargetActuator();
		Actuator checkSensor = actuatorsRepository.findById(targetActuator);
		
		if (checkSensor==null||!checkSensor.getSupportedCommands().contains(toExecurte))
			throw new IllegalArgumentException("UNSUPPORTED COMMAND: "+ toExecurte.toString());

		QueueCommands arg0 = new QueueCommands(toExecurte);
		queueCommands.save(arg0);
		return queueCommands.count();
	}

	@Override
	public boolean removeExecutedCommand(Long queueCommandId) {
		queueCommands.deleteById(queueCommandId);
		return !queueCommands.existsById(queueCommandId);
	}

	@Override
	public Collection<Command> getSupportedCommands() {
		Collection<Command> ret = new HashSet<>();
		Iterable<Actuator> it = actuatorsRepository.findAll();
		for (Actuator sensor : it) {
			ret.addAll(sensor.getSupportedCommands());
		}
		return ret;
	}


}