package it.angelic.growlroom.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.angelic.growlroom.model.Actuator;
import it.angelic.growlroom.model.Command;
import it.angelic.growlroom.model.CommandsRepository;
import it.angelic.growlroom.model.QueueCommands;
import it.angelic.growlroom.model.repositories.ActuatorsRepository;
import it.angelic.growlroom.model.repositories.QueueCommandsRepository;
import it.angelic.growlroom.model.repositories.SensorsRepository;

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
	public Collection<Command> getUnexecutedCommands(String boardId) {
		ArrayList<Command> ret = new ArrayList<>();
		
		//List<Actuator> act = actuatorsRepository.findByBoardId(Integer.valueOf(boardId));
		
		List<QueueCommands> pes = queueCommands.findByBoardId(Integer.valueOf(boardId));
		for (QueueCommands queueCommands : pes) {
			Command actualCmd = queueCommands.getToExecute();
			actualCmd.setIdOnQueue(queueCommands.getIdQueueCommand());
			ret.add(actualCmd);
			break;// only 1 per volta
		}
		return ret;
	}
	
	@Override
	public Collection<Command> getUnexecutedCommands() {
		ArrayList<Command> ret = new ArrayList<>();
		List<QueueCommands> pes = queueCommands.findAll();
		for (QueueCommands queueCommands : pes) {
			Command actualCmd = queueCommands.getToExecute();
			actualCmd.setIdOnQueue(queueCommands.getIdQueueCommand());
			ret.add(actualCmd);
			break;// only 1 per volta
		}
		return ret;
	}

	@Override
	public Long sendCommand(Command toExecurte) {

		//mmmm
		Long targetActuator = toExecurte.getTargetActuator().getActuatorId();
		Optional<Actuator> checkSensor = actuatorsRepository.findById(targetActuator);

		if (!checkSensor.isPresent() || checkSensor.get().containsCommand(toExecurte) == null)
			throw new IllegalArgumentException("UNSUPPORTED COMMAND: " + toExecurte.toString());

		QueueCommands arg0 = new QueueCommands(checkSensor.get().containsCommand(toExecurte));
		queueCommands.save(arg0);
		return queueCommands.count();
	}

	@Override
	public Long sendFullRefreshCommand() {
		Command forceRefresh = new Command();
		forceRefresh.setParameter("-3");
		forceRefresh.setName("Refresh");
		forceRefresh.setTargetActuator(null);
		QueueCommands arg0 = new QueueCommands(forceRefresh);
		queueCommands.save(arg0);
		return queueCommands.count();
	}

	@Override
	public boolean removeExecutedCommand(Long queueCommandId, Command check) {
		queueCommands.deleteById(queueCommandId);
		return !queueCommands.existsById(queueCommandId);
	}
	
	@Override
	public boolean removeExecutedCommand(String boardId, Long queueCommandId, Command executed) {
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
