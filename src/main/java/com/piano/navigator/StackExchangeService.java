package com.piano.navigator;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
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


    /**
     *
     * @param tagged not null
     * @param nottagged
     * @param intitle
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    public Map<String, Object> getJsonAsMap(String tagged, String nottagged, String intitle) throws URISyntaxException, IOException {

//        String json = getHardcodedResponse();
        String json = requestToExternalService(tagged, nottagged, intitle);
        logger.info(json);
        Map<String, Object> map = DtoToVoConverter.convertResponse(json);

        return map;
    }

    private String getHardcodedResponse() throws URISyntaxException, IOException {
        URI uri = StackExchangeService.class.getResource("/public/data.json").toURI();
        return new String(Files.readAllBytes(Paths.get(uri)));
    }

    private String requestToExternalService(String tagged, String nottagged, String intitle) throws URISyntaxException, IOException {
        URIBuilder uriBuilder = new URIBuilder("http://api.stackexchange.com/2.2/search");
        uriBuilder.addParameter("site", "stackoverflow");
        if (tagged != null) uriBuilder.addParameter("tagged", tagged);
        if (nottagged != null) uriBuilder.addParameter("nottagged", nottagged);
        if (intitle != null) uriBuilder.addParameter("intitle", intitle);

        HttpGet httpGet = new HttpGet(uriBuilder.build());

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
