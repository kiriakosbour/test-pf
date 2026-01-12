package gr.deddie.pfr.utilities;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import gr.deddie.pfr.model.PowerOutage;

import java.io.IOException;

public class PowerOutageCauseSerializer extends JsonSerializer<PowerOutage.PowerOutageCause> {
    @Override
    public void serialize(PowerOutage.PowerOutageCause powerOutageCause, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("value");
        jsonGenerator.writeString(powerOutageCause.toString());
        jsonGenerator.writeFieldName("description");
        jsonGenerator.writeString(powerOutageCause.getValue());
        jsonGenerator.writeEndObject();
    }
}
