package it.angelic.growlroom.service;

import java.util.Collection;

import it.angelic.growlroom.model.Sensor;

public interface SensorsService {
 
	public Sensor createOrUpdateBoardSensor(Sensor product, String boardId, String checkId);
 
	public Collection<Sensor> getBoardSensors(Integer boardId);
 
	Sensor getSensorByBoardIdAndPid(Long boardId, Integer sensorPid) throws SensorNotFoundException;

}