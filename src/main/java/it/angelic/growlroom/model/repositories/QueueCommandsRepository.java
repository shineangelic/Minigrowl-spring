package it.angelic.growlroom.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import it.angelic.growlroom.model.Command;
import it.angelic.growlroom.model.QueueCommands;

public interface QueueCommandsRepository extends JpaRepository<QueueCommands, Long> {
	 
	//List<Command> findByActuatorId(String commandId);
	Command findById(long id);
}