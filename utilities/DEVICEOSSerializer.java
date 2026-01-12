package gr.deddie.pfr.utilities;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import gr.deddie.pfr.model.MobileAppAnnouncement;

import java.io.IOException;

/**
 * Created by M.Masikos on 2/12/2020.
 */
public class DEVICEOSSerializer extends JsonSerializer<MobileAppAnnouncement.DeviceOS> {

    @Override
    public void serialize(MobileAppAnnouncement.DeviceOS deviceOS, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("value");
        jsonGenerator.writeString(deviceOS.toString());
        jsonGenerator.writeFieldName("description");
        jsonGenerator.writeString(deviceOS.getValue());
        jsonGenerator.writeEndObject();
    }
}
