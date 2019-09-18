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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpLogging;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.*;

@Service
@Slf4j
public class BadooApi {

    private final String xSessionHeader;
    private final ObjectMapper objectMapper;

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
            LOGGER.info("Fetching users from near by. Offset: {}, page size: {}", offset, pageSize);
            String url = "https://badoo.com/webapi.phtml?SERVER_GET_USER_LIST_WITH_SETTINGS";

            HttpLogging.forLogName(RestTemplate.class);
            RestTemplate restTemplate = new RestTemplate();

            MultiValueMap<String, String> headersMap = new HttpHeaders();
            headersMap.put("X-Session-id", Arrays.asList(xSessionHeader));

            JsonNode requestBody = createRequestBody(offset, pageSize);
            RequestEntity<JsonNode> httpEntity = new RequestEntity<>(requestBody, headersMap, HttpMethod.POST, new URI(url));

            JsonNode responseBody = restTemplate.exchange(url, HttpMethod.POST, httpEntity, JsonNode.class).getBody();

            JsonNode sectionNode = responseBody.get("body").get(0).get("client_user_list").get("section").get(0);
            if (!sectionNode.has("users")) {
                return Collections.emptyList();
            }

            return objectMapper.convertValue(sectionNode.get("users"), new TypeReference<List<UserDto>>() {});
        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to get users from near by. Offset: %s, page size: %s", offset, pageSize), e);
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
