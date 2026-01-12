package gr.deddie.pfr.utilities;

import javax.json.bind.serializer.DeserializationContext;
import javax.json.bind.serializer.JsonbDeserializer;
import javax.json.stream.JsonParser;
import java.lang.reflect.Type;
import java.util.Date;

public class UnixTimestampDeSerializer implements JsonbDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext, Type type) {
        Date date = new Date ();
        date.setTime(jsonParser.getLong());
        return date;
    }
}
