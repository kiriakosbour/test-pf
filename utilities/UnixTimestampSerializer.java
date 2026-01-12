package gr.deddie.pfr.utilities;

import javax.json.bind.serializer.JsonbSerializer;
import javax.json.bind.serializer.SerializationContext;
import javax.json.stream.JsonGenerator;
import java.util.Date;

public class UnixTimestampSerializer implements JsonbSerializer<Date> {

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializationContext serializationContext) {
        jsonGenerator.write(date != null ? date.getTime() : null);
    }
}
