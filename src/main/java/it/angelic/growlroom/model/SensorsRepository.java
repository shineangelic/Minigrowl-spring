package it.angelic.growlroom.model;

import org.springframework.data.repository.CrudRepository;

public interface SensorsRepository extends CrudRepository<Sensor, Long> {

	Sensor findByTyp(SensorEnum id);

	Sensor findById(long id);
}