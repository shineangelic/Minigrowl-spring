package it.angelic.growlroom.service;

import java.util.Collection;

import it.angelic.growlroom.model.Command;

public interface CommandsService {

	public abstract Command createOrUpdateCommand(Command product);

	public abstract Collection<Command> getUnexecutedCommands();
	public abstract boolean removeExecutedCommand(Long queueCommandId, Command executed);
	//returns queue len
	public abstract Long sendCommand(Command toExecurte);

	public abstract Collection<Command> getSupportedCommands();

	public abstract Long sendFullRefreshCommand();
 
}