package it.angelic.growlroom.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	Logger logger = LoggerFactory.getLogger(CommandsServiceImpl.class);

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
			actualCmd.setTargetActuator(queueCommands.getToExecute().getTargetActuator());
			ret.add(actualCmd);
			break;// only 1 per volta
		}
		return ret;
	}
	
	 

	@Override
	public Long sendCommand(Command toExecurte) {

		//mmmm
		Long targetActuator = toExecurte.getTargetActuator().getActuatorId();
		Optional<Actuator> checkAct = actuatorsRepository.findById(targetActuator);

		if (!checkAct.isPresent()) 
			throw new IllegalArgumentException("ACTUATOR COMMAND NOT FOUND: " + toExecurte.getTargetActuator().getActuatorId());
		if( checkAct.get().containsCommand(toExecurte) == null)
			throw new IllegalArgumentException("UNSUPPORTED COMMAND: " + toExecurte.toString());

		QueueCommands arg0 = new QueueCommands(checkAct.get().containsCommand(toExecurte));
		queueCommands.save(arg0);
		long ret = queueCommands.count();
		logger.warn("Saving command on queue for execution. Queue lenght: " + ret);
		return ret;
	}

	@Override
	public boolean removeExecutedCommand(String boardId, Long queueCommandId, Command executed) {
		queueCommands.deleteById(queueCommandId);
		return !queueCommands.existsById(queueCommandId);
	}



}
