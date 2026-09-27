package com.theatre.catalogueservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Provides a Jackson 2 {@link ObjectMapper} bean.
 *
 * <p>Spring Boot 4.1 auto-configures a Jackson 3 mapper ({@code tools.jackson})
 * rather than a Jackson 2 {@code com.fasterxml.jackson} {@link ObjectMapper}.
 * {@code ProductionService} serializes/deserializes the cast-and-crew JSON column
 * using the Jackson 2 API, so we expose that mapper explicitly here.
 *
 * <p>{@link JavaTimeModule} is registered so {@code java.time} types (used on the
 * cast/crew and production models) serialize correctly.
 */
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule());
    }
}
