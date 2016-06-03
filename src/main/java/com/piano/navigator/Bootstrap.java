package com.piano.navigator;

import com.piano.navigator.controllers.HomeController;
import com.piano.navigator.controllers.SearchController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.staticFiles;


/**
 * Created by Sergey on 5/28/16.
 */
public class Bootstrap {

    private static Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    public static void main(String[] args) {
        staticFiles.location("/public");

        port(getHerokuAssignedPort());
        get("/", new HomeController().index, new MustacheTemplateEngine());

        get("/search-result", new SearchController().index, new MustacheTemplateEngine());
    }

    private static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }

}
