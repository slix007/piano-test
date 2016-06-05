package com.piano.navigator.controllers;

import com.piano.navigator.StackExchangeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Request;
import spark.TemplateViewRoute;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sergey on 6/2/16.
 */
public class HomeController {
    private static Logger logger = LoggerFactory.getLogger(HomeController.class);

    //TODO use DI
    private static StackExchangeService stackExchangeService = StackExchangeService.getInstance();

    public TemplateViewRoute index = (request, response) -> {
        Map<String, Object> map = getResponse(request);
        return new ModelAndView(map, "home.mustache");
    };

    private Map<String, Object> getResponse(Request request) {
        Map<String, Object> jsonAsMap = new HashMap<>();
        try {
            jsonAsMap = stackExchangeService.getSites();
        } catch (URISyntaxException | IOException e) {
            logger.error("Unexpected exception",e);
        }
        return jsonAsMap;
    }
}
