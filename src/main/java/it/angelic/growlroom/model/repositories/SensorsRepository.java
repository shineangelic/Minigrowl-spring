package it.angelic.growlroom.model.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.angelic.growlroom.model.Sensor;
import it.angelic.growlroom.model.SensorEnum;

public interface SensorsRepository extends CrudRepository<Sensor, Long> {

	Sensor findByTyp(SensorEnum id);

	Optional<Sensor> findById(Integer id);
}