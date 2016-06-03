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
public class SearchController {
    private static Logger logger = LoggerFactory.getLogger(SearchController.class);


    private static StackExchangeService stackExchangeService = new StackExchangeService();

    public TemplateViewRoute index = (request, response) -> {

        Map<String, Object> map = getResponse(request);


//        logger.error(map.toString());

        return new ModelAndView(map, "search-result.mustache");
    };

    private Map<String, Object> getResponse(Request request) {
        Map<String, Object> jsonAsMap = new HashMap<>();

        try {
            String tagged = request.queryParams("tagged");
            String nottagged = request.queryParams("nottagged");
            String intitle = request.queryParams("intitle");

            jsonAsMap = stackExchangeService.getJsonAsMap(tagged, nottagged, intitle);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("",e);
        }
        return jsonAsMap;
    }
}
