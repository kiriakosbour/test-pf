package gr.deddie.pfr.utilities;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import gr.deddie.pfr.model.Failure;

import java.io.IOException;

public class GeneralFailureGroupSerializer extends JsonSerializer<Failure.GeneralFailureGroup> {

    @Override
    public void serialize(Failure.GeneralFailureGroup group, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("value");
        jsonGenerator.writeString(group.toString());
        jsonGenerator.writeFieldName("description");
        jsonGenerator.writeString(group.getValue());
        jsonGenerator.writeEndObject();
    }
}
