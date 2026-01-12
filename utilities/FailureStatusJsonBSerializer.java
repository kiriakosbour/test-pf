package gr.deddie.pfr.utilities;

import gr.deddie.pfr.model.Failure;

import javax.json.bind.serializer.JsonbSerializer;
import javax.json.bind.serializer.SerializationContext;

/**
 * Created by M.Masikos on 31/3/2022.
 */
public class FailureStatusJsonBSerializer implements JsonbSerializer<Failure.FailureStatus> {

    @Override
    public void serialize(Failure.FailureStatus failureStatus, javax.json.stream.JsonGenerator jsonGenerator, SerializationContext serializationContext) {
        jsonGenerator.writeStartObject();
        jsonGenerator.write("value", failureStatus.toString());
        jsonGenerator.write("description", failureStatus.getValue());
        jsonGenerator.writeEnd();
    }
}
