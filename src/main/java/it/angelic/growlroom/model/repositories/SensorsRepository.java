package it.angelic.growlroom.model.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.angelic.growlroom.model.Sensor;
import it.angelic.growlroom.model.SensorEnum;

public interface SensorsRepository extends CrudRepository<Sensor, Long> {

	Sensor findByTyp(SensorEnum id);
	
	@Query(value = "SELECT a FROM Sensor a WHERE a.board.boardId = :bid")
	List<Sensor> findByBoardId(@Param("bid") Long  boardId);
	
	Optional<Sensor> findByPid(Integer id);
	
	@Query(value = "SELECT max(timeStamp) FROM Sensor")
	public Date getLastContact();

	@Query(value = "SELECT u FROM Sensor u WHERE u.board.boardId = :boardId AND u.pid = :pid")
	Sensor findByBoardIdAndPid(@Param("boardId") Long boardId, @Param("pid") Integer pid);


}