package it.angelic.growlroom.service;

import it.angelic.growlroom.model.Actuator;
import it.angelic.growlroom.model.Board;
import it.angelic.growlroom.model.Sensor;

public interface BoardsService {

	Board findOrCreateBoard(String boardId);
 
	void assign(Board owner,Sensor in);
	
	void assign(Board owner,Actuator in);
 
}