package it.angelic.growlroom.model.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.angelic.growlroom.model.Actuator;

public interface ActuatorsRepository extends JpaRepository<Actuator, Long> {

	Actuator findByPid(int id);

	@Query(value = "SELECT max(timeStamp) FROM Actuator")
	public Date getLastContact();

	@Query(value = "SELECT a FROM Actuator a WHERE a.board.boardId = :valueOf")
	List<Actuator> findByBoardId(Integer valueOf);
	
	@Query(value = "SELECT u FROM Actuator u WHERE u.board.boardId = :boardId AND u.pid = :pid")
	Actuator findByBoardIdAndPid(@Param("boardId") Long boardId, @Param("pid") Integer pid);
}