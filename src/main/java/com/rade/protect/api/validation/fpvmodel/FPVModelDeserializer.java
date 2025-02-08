package com.rade.protect.api.validation.fpvmodel;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.rade.protect.model.entity.FPVDrone;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.regex.Pattern;

@Slf4j
public class FPVModelDeserializer extends StdDeserializer<FPVDrone.FPVModel> {

    private static final long serialVersionUID = -1166032307856492833L;

    private static final Pattern ENUM_PATTERN = Pattern.compile("KAMIKAZE|BOMBER|PPO");

    public FPVModelDeserializer() {
        this(null);
    }

    protected FPVModelDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public FPVDrone.FPVModel deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText().toUpperCase();
        if (!ENUM_PATTERN.matcher(value).matches()) {
            throw JsonMappingException.from(p, "Invalid FPV Model! Must be KAMIKAZE, BOMBER, or PPO");
        }
        return FPVDrone.FPVModel.valueOf(value);
    }


}
