package gr.deddie.pfr.utilities;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import gr.deddie.pfr.model.ExyphretoumeniPerioxi;
import gr.deddie.pfr.model.PowerOutage;

import java.io.IOException;

public class ExyphretoymeniPerioxiStatusSerializer extends JsonSerializer<ExyphretoumeniPerioxi.ExyphretoymeniPerioxiStatus> {
    @Override
    public void serialize(ExyphretoumeniPerioxi.ExyphretoymeniPerioxiStatus exyphretoymeniPerioxiStatus, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("value");
        jsonGenerator.writeString(exyphretoymeniPerioxiStatus.toString());
        jsonGenerator.writeFieldName("description");
        jsonGenerator.writeString(exyphretoymeniPerioxiStatus.getValue());
        jsonGenerator.writeEndObject();
    }
}
