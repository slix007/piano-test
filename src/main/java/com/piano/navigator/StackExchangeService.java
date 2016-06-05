package com.piano.navigator;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Sergey on 5/30/16.
 */
public class StackExchangeService {

    private static Logger logger = LoggerFactory.getLogger(StackExchangeService.class);
    private final String DEFAULT_PAGE = "1";
    private final String DEFAULT_PAGE_SIZE = "20";
    //    private final String DEFAULT_SITE = "stackoverflow";
    private final String BASE_URI_SEARCH = "http://api.stackexchange.com/2.2/search";
    private final String BASE_URI_SITES = "http://api.stackexchange.com/2.2/sites";

    private static StackExchangeService instance = new StackExchangeService();

    private StackExchangeService() {
    }

    public static StackExchangeService getInstance() {
        if (instance == null) {
            instance = new StackExchangeService();
        }
        return instance;
    }

    public Map<String, Object> getSites() throws URISyntaxException, IOException {
        String json = requestExternalResource(new URI(BASE_URI_SITES));
        logger.info(json);
        Map<String, Object> map = DtoToVoConverter.convertSitesResponse(json);
        return map;
    }

    /**
     * Search for questions on a site.
     *
     * @param site      not null
     * @param tagged    not null
     * @param nottagged
     * @param intitle
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    public Map<String, Object> searchForQuestions(String site, String tagged, String nottagged, String intitle) throws URISyntaxException, IOException {

        String json = doSearch(site, tagged, nottagged, intitle);
        logger.info(json);
        Map<String, Object> map = DtoToVoConverter.convertResponse(json);

        // Add request info to response. To let it request more/
        map.put("page", DEFAULT_PAGE);
        map.put("pagesize", DEFAULT_PAGE_SIZE);
        map.put("site", site);
        map.put("tagged", tagged);
        map.put("nottagged", nottagged);
        map.put("intitle", intitle);
        return map;
    }

    private String doSearch(String site, String tagged, String nottagged, String intitle) throws URISyntaxException, IOException {
        URIBuilder uriBuilder = new URIBuilder(BASE_URI_SEARCH);
        uriBuilder.addParameter("site", site);
        if (tagged != null) uriBuilder.addParameter("tagged", tagged);
        if (nottagged != null) uriBuilder.addParameter("nottagged", nottagged);
        if (intitle != null) uriBuilder.addParameter("intitle", intitle);
        uriBuilder.addParameter("page", DEFAULT_PAGE);
        uriBuilder.addParameter("pagesize", DEFAULT_PAGE_SIZE);

        URI uri = uriBuilder.build();
        logger.info("Request url: " + uri);
        return requestExternalResource(uri);
    }

    private String requestExternalResource(URI uri) throws IOException {
        HttpGet httpGet = new HttpGet(uri);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

        String result;
        try {
            logger.info("Response status is {}", httpResponse.getStatusLine());
            HttpEntity entity1 = httpResponse.getEntity();

            // do something useful with the response body
            InputStream inputStream = entity1.getContent();
            result = new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining());

            // and ensure it is fully consumed
            EntityUtils.consume(entity1);
        } finally {
            httpResponse.close();
        }

        return result;
    }
}
