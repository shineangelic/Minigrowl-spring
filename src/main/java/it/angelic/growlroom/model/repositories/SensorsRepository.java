package it.angelic.growlroom.model.repositories;

import org.springframework.data.repository.CrudRepository;

import it.angelic.growlroom.model.Sensor;
import it.angelic.growlroom.model.SensorEnum;

public interface SensorsRepository extends CrudRepository<Sensor, Long> {

	Sensor findByTyp(SensorEnum id);

	Sensor findById(long id);
}