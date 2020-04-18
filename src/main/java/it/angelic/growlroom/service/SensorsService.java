package it.angelic.growlroom.service;

import java.util.Collection;

import it.angelic.growlroom.model.Sensor;

public interface SensorsService {

	public abstract Sensor createOrUpdateSensor(Sensor product, String checkId);

	public abstract Collection<Sensor> getSensors();
	
	public abstract Sensor getSensorById(Integer sensorPid) throws SensorNotFoundException;
}