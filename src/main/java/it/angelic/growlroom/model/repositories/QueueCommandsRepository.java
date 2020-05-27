package it.angelic.growlroom.model.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.angelic.growlroom.model.Command;
import it.angelic.growlroom.model.QueueCommands;

public interface QueueCommandsRepository extends JpaRepository<QueueCommands, Long> {
	 
	//List<Command> findByActuatorId(String commandId);
	Command findById(long id);

	@Query(value = "Select * from Queue_Commands "
			+ " inner join Command cmd on Queue_Commands.to_Execute = cmd.id_on_queue "
			+ " inner join Actuator act on act.id = cmd.target_Actuator "
			+ " where act.board_id = :boardId", nativeQuery = true)
	List<QueueCommands> findByBoardId(@Param("boardId") Integer boardId);
}