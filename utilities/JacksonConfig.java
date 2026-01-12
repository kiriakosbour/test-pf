package gr.deddie.pfr.utilities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import javax.ws.rs.ext.ContextResolver;

public class JacksonConfig implements ContextResolver<ObjectMapper> {

    @Override
    public ObjectMapper getContext(final Class<?> type) {
        final ObjectMapper mapper = new ObjectMapper();

        SimpleModule module = new SimpleModule("ConvertEmptyToNullDeserializer");
        // set deserializer in order to convert empty strings to null
        module.addDeserializer(String.class, new EmptyToNullDeserializer());
        // set serializer in order to convert empty strings to null
        module.addSerializer(String.class, new EmptyToNullSerializer());

        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.registerModule(module);

        return mapper;
    }

}
