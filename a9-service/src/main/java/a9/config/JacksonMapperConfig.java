package a9.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import static com.fasterxml.jackson.databind.DeserializationFeature.*;

/**
 * @author tzq0301
 * @version 1.0
 */
@SpringBootConfiguration
public class JacksonMapperConfig {

    @Bean
    public ToStringSerializer toStringSerializer() {
        return new ToStringSerializer();
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer(
            ToStringSerializer toStringSerializer) {
        ObjectMapper objectMapper = new ObjectMapper()
                .configure(ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                .configure(ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
                .configure(FAIL_ON_INVALID_SUBTYPE, false)
                .configure(FAIL_ON_TRAILING_TOKENS, false);

        return builder -> builder
                .serializerByType(ObjectId.class, toStringSerializer)
                .configure(objectMapper);
    }

}
