package it.angelic.growlroom.model;

import org.springframework.data.repository.CrudRepository;

public interface CommandsRepository extends CrudRepository<Command, Long> {

	//List<Command> findByActuatorId(String commandId);

	Command findById(long id);
}