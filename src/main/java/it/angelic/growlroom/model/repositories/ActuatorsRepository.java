package it.angelic.growlroom.model.repositories;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.angelic.growlroom.model.Actuator;

public interface ActuatorsRepository extends JpaRepository<Actuator, Long> {

	Actuator findById(int id);

	@Query(value = "SELECT max(timeStamp) FROM Actuator")
	public Date getLastContact();
}