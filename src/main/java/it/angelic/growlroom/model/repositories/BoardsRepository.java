package it.angelic.growlroom.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import it.angelic.growlroom.model.Board;

public interface BoardsRepository extends JpaRepository<Board, Long> {

	Board findByBoardId(Long id);
 
}