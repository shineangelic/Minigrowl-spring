package it.angelic.growlroom.service;

import java.util.Collection;
import java.util.Date;

import it.angelic.growlroom.model.Actuator;

public interface ActuatorsService {

	public abstract Actuator createOrUpdateActuator(Actuator product, String checkId);

	public abstract Collection<Actuator> getActuators();

	public Date getLastContact();
}