package ua.com.nc.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Service;
import ua.com.nc.service.SerializationService;

@Service
public class SerializationServiceImpl implements SerializationService {

    private Gson gson;

    public SerializationServiceImpl(){
        gson = new GsonBuilder().setDateFormat("dd.MM.yyyy").serializeNulls().create();
    }


    @Override
    public String serializeWithDateFormat(Object element) {
        return gson.toJson(element);
    }

    @Override
    public String serializationWithDateTimeFormat(Object element) {
        gson = new GsonBuilder().setDateFormat("yyyy.MM.dd.HH.mm.ss").serializeNulls().create();
        return gson.toJson(element);
    }

    @Override
    public Object deserialize(String json, Class cls) {
        return gson.fromJson(json, cls);
    }
}
