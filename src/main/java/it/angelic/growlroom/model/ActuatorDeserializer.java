package it.angelic.growlroom.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import it.angelic.growlroom.model.repositories.ActuatorsRepository;

public class ActuatorDeserializer extends JsonDeserializer implements Serializable {

	private static final long serialVersionUID = -9012464195937554378L;

	@Autowired
	ActuatorsRepository actuatorsRepository;

	@Override
	public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		JsonNode node = jsonParser.getCodec().readTree(jsonParser);
		Long userId = node.asLong();
		System.out.println(node);
		System.out.println(node.asLong());
		Optional<Actuator> act = actuatorsRepository.findById(userId);
		System.out.println("appuser: " + act.toString());
		return act.get();
	}
}