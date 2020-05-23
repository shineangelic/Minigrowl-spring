package it.angelic.growlroom.service;

import java.util.Collection;

import it.angelic.growlroom.model.Sensor;

public interface SensorsService {

	public Sensor createOrUpdateSensor(Sensor product, String checkId);

	public Collection<Sensor> getSensors();

	public Sensor getSensorById(Integer sensorPid) throws SensorNotFoundException;
}