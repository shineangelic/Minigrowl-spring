package it.angelic.growlroom.model;

import org.springframework.data.repository.CrudRepository;

public interface ActuatorsRepository extends CrudRepository<Actuator, Long> {
 

	Sensor findById(long id);
}