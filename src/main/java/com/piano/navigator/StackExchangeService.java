package com.piano.navigator;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Created by slix on 5/30/16.
 */
public class StackExchangeService {


    private final String FIELD_CREATION_DATE = "creation_date";
    private final String FIELD_ITEMS = "items";

    public Map<String, Object> getJsonAsMap() throws URISyntaxException, IOException {

        Map<String, Object> map = getTheMap();
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

    private Map<String, Object> getTheMap() throws URISyntaxException, IOException {
        String json = getHardcodedResponse();

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
        return map;
    }

    private String getHardcodedResponse() throws URISyntaxException, IOException {
        URI uri = StackExchangeService.class.getResource("/public/data.json").toURI();
        return new String(Files.readAllBytes(Paths.get(uri)));
    }
}
