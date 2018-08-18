package com.shaposhnyk.jackson1x;

import com.ljcr.api.ImmutableNode;
import com.ljcr.api.Repository;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.jackson1x.JacksonAdapter;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.*;

public class JacksonAdapterTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Test
    public void simpleValuesAreOk() throws IOException {
        JsonNode json = om.readTree("{"
                + "\"myFieldS\": \"myValue\","
                + "\"myFieldL\": 1234567890,"
                + "\"myFieldB\": true,"
                + "\"myFieldN\": null"
                + "}");
        Repository ws = JacksonAdapter.createWs("Test", json);
        ImmutableNode rootNode = ws.getRootNode();
        Assert.assertThat(rootNode.getName(), equalTo(""));
        Assert.assertThat(rootNode.isObjectNode(), equalTo(true));

        Assert.assertThat(rootNode.getItem("myFieldS"), notNullValue());
        Assert.assertThat(rootNode.getItem("myFieldS").getValue(), equalTo("myValue"));

        Assert.assertThat(rootNode.getItem("myFieldL"), notNullValue());
        Assert.assertThat(rootNode.getItem("myFieldL").asLong(), equalTo(1234567890L));

        Assert.assertThat(rootNode.getItem("myFieldB"), notNullValue());
        Assert.assertThat(rootNode.getItem("myFieldB").getValue(), equalTo(true));

        ImmutableNode nullNode = rootNode.getItem("myFieldN");
        Assert.assertThat(nullNode, notNullValue());
        Assert.assertThat(nullNode.getValue(), nullValue());
        Assert.assertThat(nullNode.asString(), nullValue());
    }

    @Test
    public void simpleValuesTypesAreOk() throws IOException {
        JsonNode json = om.readTree("{\"myObject\": {\"myProp\":1, \"myProp2\":\"some\"} }");
        Repository ws = JacksonAdapter.createWs("Test", json);
        ImmutableNode rootNode = ws.getRootNode();


        Assert.assertThat(rootNode.getName(), equalTo(""));
        Assert.assertThat(rootNode.isArrayNode(), equalTo(false));
        Assert.assertThat(rootNode.isObjectNode(), equalTo(true));
        Assert.assertThat(rootNode.getTypeDefinition().getIdentifier(), equalTo("object"));

        // assert that defined field is only one - ANY
        Assert.assertThat(rootNode.getTypeDefinition().getDeclaredPropertyDefinitions(),
                everyItem(equalTo(StandardTypes.UNKNOWN_PROPERTY)));

        // field defs
        Assert.assertThat(rootNode.getItems().map(it -> it.getName())
                .collect(toList()), equalTo(
                Arrays.asList("myObject")));

        // field defs
        Assert.assertThat(rootNode.getItems().map(it -> it.getTypeDefinition().getIdentifier())
                .collect(toList()), equalTo(
                Arrays.asList("object")));

        Assert.assertThat(rootNode.getItem("myObject"), notNullValue());
        Assert.assertThat(rootNode.getItem("myObject").getTypeDefinition().getIdentifier(), equalTo("object"));
        Assert.assertThat(rootNode.getItem("myObject").getItems().collect(toList()).size(), equalTo(2));
    }

}