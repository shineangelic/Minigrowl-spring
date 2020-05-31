package it.angelic.growlroom.service;

import java.util.Collection;

import it.angelic.growlroom.model.Sensor;

public interface SensorsService {

	public Sensor createOrUpdateSensor(Sensor product, String checkId);

	public Sensor createOrUpdateBoardSensor(Sensor product, String boardId, String checkId);

	public Collection<Sensor> getSensors();
	public Collection<Sensor> getBoardSensors(Integer boardId);

	public Sensor getSensorByPid(Integer sensorPid) throws SensorNotFoundException;

	Sensor getSensorByBoardIdAndPid(Long boardId, Integer sensorPid) throws SensorNotFoundException;


}