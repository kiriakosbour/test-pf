package gr.deddie.pfr.utilities;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import gr.deddie.pfr.model.Failure;

import java.io.IOException;

/**
 * Created by M.Masikos on 22/5/2017.
 */
public class FailureStatusSerializer extends JsonSerializer<Failure.FailureStatus> {

    @Override
    public void serialize(Failure.FailureStatus status, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("value");
        jsonGenerator.writeString(status.toString());
        jsonGenerator.writeFieldName("description");
        jsonGenerator.writeString(status.getValue());
        jsonGenerator.writeEndObject();
    }
}
