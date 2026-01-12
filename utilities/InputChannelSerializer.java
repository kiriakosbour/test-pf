package gr.deddie.pfr.utilities;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import gr.deddie.pfr.model.Failure;
import gr.deddie.pfr.model.PowerOutage;

import java.io.IOException;

public class InputChannelSerializer extends JsonSerializer<Failure.InputChannel> {
    @Override
    public void serialize(Failure.InputChannel inputChannel, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("value");
        jsonGenerator.writeString(inputChannel.toString());
        jsonGenerator.writeFieldName("description");
        jsonGenerator.writeString(inputChannel.getValue());
        jsonGenerator.writeEndObject();
    }
}
