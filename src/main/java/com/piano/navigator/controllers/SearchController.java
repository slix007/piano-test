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

    //TODO use DI
    private static StackExchangeService stackExchangeService = StackExchangeService.getInstance();

    public TemplateViewRoute index = (request, response) -> {

        Map<String, Object> map = getResponse(request);

        return new ModelAndView(map, "search-result.mustache");
    };

    private Map<String, Object> getResponse(Request request) {
        Map<String, Object> map = new HashMap<>();

        try {
            String site = request.queryParams("site");
            logger.info("Site:" + site);
            String tagged = request.queryParams("tagged");
            String nottagged = request.queryParams("nottagged");
            String intitle = request.queryParams("intitle");

            map = stackExchangeService.searchForQuestions(site, tagged, nottagged, intitle);

        } catch (URISyntaxException | IOException e) {
            logger.error("Unexpected exception", e);
        }
        return map;
    }
}
