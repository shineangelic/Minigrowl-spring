package it.angelic.growlroom.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.angelic.growlroom.model.Sensor;
import it.angelic.growlroom.model.SensorsRepository;
import it.angelic.growlroom.model.UnitEnum;

@Service
public class SensorsServiceImpl implements SensorsService {
	@Autowired
	private SensorsRepository sensorRepository;

	@Override
	public Sensor createOrUpdateSensor(Sensor sensing, String checkId) {
		if (!Integer.valueOf(checkId).equals(sensing.getId()))
			throw new IllegalArgumentException("PID Mismatch: " + checkId + " vs" + sensing.getId());
		
		switch (sensing.getTyp()) {
		case BAROMETER:
			sensing.setUinit(UnitEnum.MILLIBAR);
			break;
		case TEMPERATURE:
			sensing.setUinit(UnitEnum.CELSIUS);
			break;
		case HUMIDITY:
			sensing.setUinit(UnitEnum.PERCENT);
			break;
		case LIGHT:
			sensing.setUinit(UnitEnum.LUMEN);
			break;
		case WATER_RESERVE:
			sensing.setUinit(UnitEnum.LITER);
			break;
		default:
			break;
		}

		sensing.setTimeStamp(new Date());
		return sensorRepository.save(sensing);
	}

	@Override
	public Collection<Sensor> getSensors() {
		List<Sensor> ret = new ArrayList<>();
		Iterable<Sensor> res = sensorRepository.findAll();
		for (Sensor sensor : res) {
			ret.add(sensor);
		}

		return ret;
	}

}
