package com.chaos.badoo.searcher;

import com.chaos.badoo.searcher.badoo.BadooApi;
import com.chaos.badoo.searcher.badoo.UserDto;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class BadooApiTest {

    private static final String X_SESSION_HEADER = "s1:401:FggKTZHs856oCTQ6kHmgqHU2FfzBSnguJ7dcqCxt";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private BadooApi badooApi = new BadooApi(X_SESSION_HEADER, OBJECT_MAPPER);

    @Test
    public void testGet5Users() throws Exception {
        List<UserDto> users = badooApi.getUsersFromNearBy(0, 5);
        Assert.assertEquals(5, users.size());
    }


}
