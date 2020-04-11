package it.angelic.growlroom.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import it.angelic.growlroom.model.Actuator;

public interface ActuatorsRepository extends JpaRepository<Actuator, Long> {
 

	Actuator findById(int id);
}