package it.angelic.growlroom.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ActuatorsRepository extends JpaRepository<Actuator, Long> {
 

	Actuator findById(int id);
}