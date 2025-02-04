package com.rade.protect.api.validation.fpvreportids;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Long.parseLong;

public class FPVReportIdsDeserializer extends StdDeserializer<List<Long>> {

    public FPVReportIdsDeserializer() {
        super(List.class);
    }

    @Override
    public List<Long> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonMappingException {
        List<?> rawIds = p.readValueAs(List.class); // Читаємо JSON як List<?>

        return rawIds.stream()
                .map(obj -> {
                    if (obj instanceof Number) {
                        return ((Number) obj).longValue(); // Якщо число → конвертуємо
                    } else if (obj instanceof String) {
                        try {
                            return parseLong((String) obj);
                        } catch (NumberFormatException e) {
                            throw new NumberFormatException("Invalid ID format: '" + obj + "' is not a valid number.");
                        }
                    } else {
                        throw new NumberFormatException("Invalid ID format: '" + obj + "' is not a valid number.");
                    }
                })
                .collect(Collectors.toList());
    }
}
