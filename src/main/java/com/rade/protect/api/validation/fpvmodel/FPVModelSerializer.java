package com.rade.protect.api.validation.fpvmodel;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.rade.protect.model.entity.FPVDrone;

import java.io.IOException;

public class FPVModelSerializer extends StdSerializer<FPVDrone.FPVModel> {

    private static final long serialVersionUID = 1376504304439963619L;

    public FPVModelSerializer() {
        super(FPVDrone.FPVModel.class);
    }

    public FPVModelSerializer(Class<FPVDrone.FPVModel> t) {
        super(t);
    }

    @Override
    public void serialize(FPVDrone.FPVModel fpvModel, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
        generator.writeString(fpvModel.name());
    }
}
