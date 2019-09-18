package com.chaos.badoo.searcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.logback.LogbackLoggingSystem;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UserListCodeToFieldParser {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void main(String[] args) throws Exception {
        LogbackLoggingSystem.get(UserListCodeToFieldParser.class.getClassLoader()).setLogLevel(LogbackLoggingSystem.ROOT_LOGGER_NAME, LogLevel.ERROR);
        List<String> fieldNamesToIgnore = Arrays.asList("user_id", "$gpb", "projection", "access_level");

        String xSessionHeader = "s1:401:FggKTZHs856oCTQ6kHmgqHU2FfzBSnguJ7dcqCxt";
        String url = "https://badoo.com/webapi.phtml?SERVER_GET_USER_LIST_WITH_SETTINGS";

        int projectionNumberStart = 0;
        int projectionNumberEnd = 2000;

        HttpLogging.forLogName(RestTemplate.class);
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> headersMap = new HttpHeaders();
        headersMap.put("X-Session-id", Arrays.asList(xSessionHeader));

        for (int i = projectionNumberStart; i <= projectionNumberEnd; i++) {
            try {
                JsonNode requestBody = createRequestBody(i);
                RequestEntity<JsonNode> httpEntity = new RequestEntity<>(requestBody, headersMap, HttpMethod.POST, new URI(url));

                JsonNode responseBody = restTemplate.exchange(url, HttpMethod.POST, httpEntity, JsonNode.class).getBody();
                if (responseBody.toString().contains("Unknown command server_unknown_action")) {
                    continue;
                }

                JsonNode userNode = responseBody.get("body").get(0).get("client_user_list").get("section").get(0).get("users").get(0);
                Map<String, Object> fieldValues = new LinkedHashMap<>();
                userNode.fieldNames().forEachRemaining(fieldName -> {
                    if (!fieldNamesToIgnore.contains(fieldName)) {
                        fieldValues.put(fieldName, userNode.get(fieldName));
                    }
                });

                if (fieldValues.isEmpty()) {
                    continue;
                }

                System.out.println("CODE: " + i);
                fieldValues.entrySet().forEach(entry -> {
                    System.out.println(String.format("%s: %s", entry.getKey(), entry.getValue()));
                });

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Thread.sleep(500);
            }
        }

    }

    public static JsonNode createRequestBody(int projectionNumber) {
        ObjectNode requestBody = OBJECT_MAPPER.createObjectNode();
        requestBody.put("version", 1);
        requestBody.put("message_type", 416);
        requestBody.put("message_id", 15);

        ObjectNode body = requestBody.putArray("body").addObject();
        body.put("message_type", 245);

        ObjectNode userList = body.putObject("server_get_user_list");
        userList.put("folder_id", 25);
        userList.putObject("user_field_filter").putArray("projection").add(projectionNumber);
        userList.put("offset", 0);
        userList.put("preferred_count", 1);

        return requestBody;
    }

}
