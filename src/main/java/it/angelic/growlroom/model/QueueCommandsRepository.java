package it.angelic.growlroom.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QueueCommandsRepository extends JpaRepository<QueueCommands, Long> {
	 
	//List<Command> findByActuatorId(String commandId);
	Command findById(long id);
}