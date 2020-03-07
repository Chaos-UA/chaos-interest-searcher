package com.chaos.badoo.searcher.badoo;

import com.chaos.badoo.searcher.common.PageableIterator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.chaos.badoo.searcher.common.PageableResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Service
@Slf4j
public class BadooApi {

    private final String xSessionHeader;
    private final ObjectMapper objectMapper;

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public BadooApi(@Value("${x-session-header}") String xSessionHeader, ObjectMapper objectMapper) {
        this.xSessionHeader = xSessionHeader;
        this.objectMapper = objectMapper;
    }

    public Iterator<PageableResult<UserDto>> getAllUsersFromNearByPages(int pageSize) {
        return new PageableIterator<>() {
            @Override
            protected PageableResult<UserDto> fetchNextPage(String lastEvaluatedKey) {
                int offset = lastEvaluatedKey == null ? 0 : Integer.parseInt(lastEvaluatedKey);
                var pageableResult = new PageableResult<UserDto>();
                pageableResult.setItems(getUsersFromNearBy(offset, pageSize));
                pageableResult.setLastEvaluatedKey(String.valueOf(offset + pageSize));
                return pageableResult;
            }
        };
    }

    public List<UserDto> getUsersFromNearBy(int offset, int pageSize) {
        try {

            String url = "https://badoo.com/webapi.phtml?SERVER_GET_USER_LIST_WITH_SETTINGS";

            MultiValueMap<String, String> headersMap = new HttpHeaders();
            headersMap.put("X-Session-id", Arrays.asList(xSessionHeader));

            JsonNode requestBody = createRequestBody(offset, pageSize);
            RequestEntity<JsonNode> httpEntity = new RequestEntity<>(requestBody, headersMap, HttpMethod.POST, new URI(url));

            JsonNode responseBody = restTemplate.exchange(url, HttpMethod.POST, httpEntity, JsonNode.class).getBody();

            JsonNode sectionNode = responseBody.get("body").get(0).get("client_user_list").get("section").get(0);
            LOGGER.info(
                    "Fetched users from near by. Offset: {}, page size: {}, total count: {}",
                    offset, pageSize, sectionNode.get("total_count")
            );
            if (!sectionNode.has("users")) {
                return Collections.emptyList();
            }

            return objectMapper.convertValue(sectionNode.get("users"), new TypeReference<List<UserDto>>() {});
        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to get users from near by. Offset: %s, page size: %s", offset, pageSize), e);
        }
    }

    public void updateSearchSettings(SearchSettingsDto searchSettingsDto) {
        LOGGER.info("Updating search settings to: {}", searchSettingsDto);

        ObjectNode settingsNode = objectMapper.createObjectNode();
        settingsNode.put("version", 1);
        settingsNode.put("message_type", 420);
        settingsNode.put("message_id", 53);
        ObjectNode bodyObject = settingsNode.putArray("body").addObject();
        settingsNode.put("is_background", false);


        bodyObject.put("message_type", 503);
        ObjectNode searchSettings = bodyObject.putObject("server_save_search_settings");
        searchSettings.put("context_type", 2);

        ObjectNode settings = searchSettings.putObject("settings");
        settings.put("$gpb", "badoo.bma.SearchSettingsValues");
        settings.putObject("age")
                .put("start", searchSettingsDto.getAgeStart())
                .put("end", searchSettingsDto.getAgeEnd());
      //  settings.putObject("distance")
      //          .put("fixed_end", searchSettingsDto.getDistance());
            //    .put("fixed_end", "a_62_291_4247_100_Km");

        String url = "https://badoo.com/webapi.phtml?SERVER_SAVE_SEARCH_SETTINGS_AND_GET_USER_LIST";
        MultiValueMap<String, String> headersMap = new HttpHeaders();
        headersMap.put("X-Session-id", Arrays.asList(xSessionHeader));
        try {
            RequestEntity<JsonNode> httpEntity = new RequestEntity<>(settingsNode, headersMap, HttpMethod.POST, new URI(url));
            ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, JsonNode.class);
            if (!response.getStatusCode().equals(HttpStatus.OK) || response.getBody().get("message_type").intValue() != 504) {
                throw new RuntimeException(String.format("Invalid response: %s. Body: %s", response.getStatusCode(), response.getBody()));
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException("Failed to update search settings: " + searchSettingsDto, e);
        }
    }

    private JsonNode createRequestBody(int offset, int pageSize) {
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("version", 1);
        requestBody.put("message_type", 416);
        requestBody.put("message_id", 15);

        ObjectNode body = requestBody.putArray("body").addObject();
        body.put("message_type", 245);

        ObjectNode userList = body.putObject("server_get_user_list");
        userList.put("folder_id", 25);

        ObjectNode userFieldFilter = userList.putObject("user_field_filter");
        ArrayNode requestAlbums = userFieldFilter.putArray("request_albums");
        for (int i = 1; i <= 13; i++) {
            requestAlbums.addObject().put("album_type", 2);
        }

        ArrayNode projectionArray = userFieldFilter.putArray("projection");
        for (UserField userField : UserField.values()) {
            projectionArray.add(userField.getCode());
        }

        userList.put("offset", offset);
        userList.put("preferred_count", pageSize);

        return requestBody;
    }
}
