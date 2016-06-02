package com.piano.navigator;

import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Created by slix on 6/1/16.
 */
public class DtoToVoConverter {


    private static final String FIELD_CREATION_DATE = "creation_date";
    private static final String FIELD_ITEMS = "items";


    public static Map<String, Object> convertResponse(String json) {
        //TODO use jackson to manipulate with response JSON(to map json into objects)
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            Items newPost = mapper.readValue(json, Items.class);
//
//        } catch (JsonParseException e){
//            // Hey, you did not send a valid request!
//        }

        Map map = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create().fromJson(json, Map.class);

        List<Map<String, Object>> itemList = (List<Map<String, Object>>)map.get(FIELD_ITEMS);

        for (Map<String, Object> item : itemList) {
            Double doubleVal = (Double)item.get(FIELD_CREATION_DATE);
            Long creationDate = doubleVal.longValue();
            LocalDateTime localDate = LocalDateTime.ofEpochSecond(creationDate, 0, ZoneOffset.UTC);
            String formatedDate = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            item.replace(FIELD_CREATION_DATE, formatedDate);
        }

        return map;
    }
}
