package com.shaposhnyk.jackson1x;


import com.ljcr.api.Repository;
import com.ljcr.jackson1x.JacksonAdapter;
import com.ljcr.tests.UserRepositorySupport;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class JsonUserRepositoryTest extends UserRepositorySupport {


    @Override
    public Repository createWs() {
        try {
            JsonNode json = new ObjectMapper().readTree(getClass().getResourceAsStream("/user.json"));
            return JacksonAdapter.createWs("User", json);
        } catch (IOException x) {
            throw new IllegalStateException(x);
        }
    }
}