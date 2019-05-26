package ua.com.nc.service;

public interface SerializationService {

    String serializeWithDateFormat(Object element);

    String serializationWithDateTimeFormat(Object element);

    Object deserialize(String json, Class cls);
}
