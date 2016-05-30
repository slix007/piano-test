package com.piano.navigator;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Created by slix on 5/30/16.
 */
public class StackExchangeService {


    public Map<String, Object> getJsonAsMap() throws URISyntaxException, IOException {

        return getHardcoded();
    }

    private Map<String, Object> getHardcoded() throws URISyntaxException, IOException {
        URI uri = StackExchangeService.class.getResource("/public/data.json").toURI();
        String json = new String(Files.readAllBytes(Paths.get(uri)));

//        Map<String,Object> map = new Gson().fromJson(json, Map.class);

        Map map = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create().fromJson(json, Map.class);
        return map;
    }
}
