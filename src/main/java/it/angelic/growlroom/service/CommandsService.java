package it.angelic.growlroom.service;

import java.util.Collection;

import it.angelic.growlroom.model.Command;

public interface CommandsService {

	public abstract Command createOrUpdateCommand(Command product);
  
	//returns queue len
	public abstract Long sendCommand(Command toExecurte);
  
	public abstract Collection<Command> getUnexecutedCommands(String boardId);
	
	public abstract boolean removeExecutedCommand(String boardId, Long valueOf, Command executed);
 
}