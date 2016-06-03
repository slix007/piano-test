package com.piano.navigator.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.TemplateViewRoute;

import java.util.HashMap;

/**
 * Created by Sergey on 6/2/16.
 */
public class HomeController {
    private static Logger logger = LoggerFactory.getLogger(HomeController.class);

    public TemplateViewRoute index = (request, response) -> {
        return new ModelAndView(new HashMap<>(), "home.mustache");
    };
}
