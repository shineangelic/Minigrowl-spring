package it.angelic.growlroom.model;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class IntActuatorSerializer extends JsonSerializer<Integer> {

	@Override
	public void serialize(Integer tmpInt, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException, JsonProcessingException {
		jsonGenerator.writeObject(tmpInt.toString());
	}
}