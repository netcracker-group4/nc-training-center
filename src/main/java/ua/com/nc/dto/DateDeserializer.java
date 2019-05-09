package ua.com.nc.dto;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.sql.Timestamp;

public class DateDeserializer implements JsonDeserializer<Timestamp> {

    @Override
    public Timestamp deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
        //date must be in "yyyy-MM-dd HH:mm:ss" format
        String date = element.getAsString();
        return Timestamp.valueOf(date);
    }
}