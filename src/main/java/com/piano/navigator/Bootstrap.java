package com.piano.navigator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.staticFiles;


/**
 * Created by slix on 5/28/16.
 */
public class Bootstrap {

    private static Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    private static StackExchangeService stackExchangeService;

    public static void main(String[] args) throws IOException, URISyntaxException {
        staticFiles.location("/public");

        stackExchangeService = new StackExchangeService();
        Map<String, Object> map = stackExchangeService.getJsonAsMap();


        logger.error(map.toString());
//        logger.error(json);

        ModelAndView modelAndView = new ModelAndView(map, "navigator.mustache");

        port(getHerokuAssignedPort());
        get("/hello", (req, res) -> modelAndView, new MustacheTemplateEngine());
    }

    private static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}
