package com.ljcr.jackson2x

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.CoreMatchers.*
import org.junit.Assert
import org.junit.Test
import com.ljcr.api.definitions.StandardTypes
import kotlin.streams.toList

class JacksonAdapterTest {

    val om = ObjectMapper()

    @Test
    fun simpleValuesAreOk() {
        val json = om.readTree("""{
            |"myFieldS": "myValue",
            |"myFieldL": 1234567890,
            |"myFieldB": true,
            |"myFieldN": null
            |}""".trimMargin())
        val ws = JsonAdapter.createWs(json)

        Assert.assertThat(ws.rootNode.name, equalTo(""))
        Assert.assertThat(ws.rootNode.isObjectNode, equalTo(true))

        Assert.assertThat(ws.rootNode.getItem("myFieldS"), notNullValue())
        Assert.assertThat(ws.rootNode.getItem("myFieldS").asProperty().string, equalTo("myValue"))

        Assert.assertThat(ws.rootNode.getItem("myFieldL"), notNullValue())
        Assert.assertThat(ws.rootNode.getItem("myFieldL").asProperty().long, equalTo(1234567890L))

        Assert.assertThat(ws.rootNode.getItem("myFieldB"), notNullValue())
        Assert.assertThat(ws.rootNode.getItem("myFieldB").asProperty().boolean, equalTo(true))

        Assert.assertThat(ws.rootNode.getItem("myFieldN"), notNullValue())
        Assert.assertThat(ws.rootNode.getItem("myFieldN").asProperty().string, nullValue())
        Assert.assertThat(ws.rootNode.getItem("myFieldN")
                .asProperty()
                .decimal, nullValue())
    }

    @Test
    fun simpleValuesTypesAreOk() {
        val json = om.readTree("""{
            |"myFieldS": "myValue",
            |"myFieldL": 1234567890,
            |"myFieldB": true,
            |"myFieldN": null,
            |"myObject": {"myProp": 1, "myProp2": "some"}
            |}""".trimMargin())
        val ws = JsonAdapter.createWs(json)

        Assert.assertThat(ws.rootNode.name, equalTo(""))
        Assert.assertThat(ws.rootNode.isArrayNode, equalTo(false))
        Assert.assertThat(ws.rootNode.isObjectNode, equalTo(true))
        Assert.assertThat(ws.rootNode.typeDefinition.identifier, equalTo("object"))

        // assert that defined field is only one - ANY
        Assert.assertThat(ws.rootNode.typeDefinition.propertyDefinitions, everyItem(equalTo(StandardTypes.ANYTYPE)))

        // field defs
        Assert.assertThat(ws.rootNode.items
                .map { it.name }
                .toList(), equalTo(listOf("myFieldS", "myFieldL", "myFieldB", "myFieldN", "myObject")))

        // field defs
        Assert.assertThat(ws.rootNode.items
                .map { it.typeDefinition.identifier }
                .toList(), equalTo(listOf("String", "Long", "Boolean", "undefined", "object")))

        Assert.assertThat(ws.rootNode.getItem("myFieldS"), notNullValue())
        Assert.assertThat(ws.rootNode.getItem("myFieldS").typeDefinition, equalTo(StandardTypes.STRING))
        Assert.assertThat(ws.rootNode.getItem("myFieldS").asProperty().typeDefinition, equalTo(StandardTypes.STRING))

        Assert.assertThat(ws.rootNode.getItem("myFieldL"), notNullValue())
        Assert.assertThat(ws.rootNode.getItem("myFieldL").typeDefinition, equalTo(StandardTypes.LONG))
        Assert.assertThat(ws.rootNode.getItem("myFieldL").asProperty().typeDefinition, equalTo(StandardTypes.LONG))

        Assert.assertThat(ws.rootNode.getItem("myFieldB"), notNullValue())
        Assert.assertThat(ws.rootNode.getItem("myFieldB").typeDefinition, equalTo(StandardTypes.BOOLEAN))
        Assert.assertThat(ws.rootNode.getItem("myFieldB").asProperty().typeDefinition, equalTo(StandardTypes.BOOLEAN))

        Assert.assertThat(ws.rootNode.getItem("myFieldN"), notNullValue())
        Assert.assertThat(ws.rootNode.getItem("myFieldN").typeDefinition, equalTo(StandardTypes.UNDEFINED))
        Assert.assertThat(ws.rootNode.getItem("myFieldN").asProperty().typeDefinition, equalTo(StandardTypes.UNDEFINED))

        Assert.assertThat(ws.rootNode.getItem("myObject"), notNullValue())
        Assert.assertThat(ws.rootNode.getItem("myObject").typeDefinition.identifier, equalTo("object"))
        Assert.assertThat(ws.rootNode.getItem("myObject").items.toList().size, equalTo(2))
    }

    @Test
    fun arraysSimpleCases() {
        val json = om.readTree("""{
            |"myFieldS": ["myValue1", "myValue2"],
            |"myFieldL": [123, 456],
            |"myFieldB": [true, false],
            |"myFieldE": [],
            |"myFieldM": ["myValue1", 123, true, [1,2,3], null],
            |"myFieldN": null
            |}""".trimMargin())
        val ws = JsonAdapter.createWs(json)

        Assert.assertThat(ws.rootNode.name, equalTo(""))
        Assert.assertThat(ws.rootNode.isArrayNode, equalTo(false))
        Assert.assertThat(ws.rootNode.isObjectNode, equalTo(true))

        val itemS = ws.rootNode.getItem("myFieldS")
        Assert.assertThat(itemS.isArrayNode, equalTo(true))
        Assert.assertThat(itemS.typeDefinition.identifier, equalTo("array"))

        Assert.assertThat(itemS.asArray().items
                .map { it.asProperty().string }
                .toList(), equalTo(listOf("myValue1", "myValue2")))

        val itemSItems = itemS.asArray().items.toList();
        Assert.assertThat(itemSItems[0].typeDefinition, equalTo(StandardTypes.STRING))

        val itemL = ws.rootNode.getItem("myFieldL")
        Assert.assertThat(itemL.isArrayNode, equalTo(true))
        Assert.assertThat(itemL.asArray().items
                .map { it.asProperty().long }
                .toList(), equalTo(listOf(123L, 456L)))
        Assert.assertThat(itemL.asArray().items
                .map { it.asProperty().string }
                .toList(), equalTo(listOf("123", "456")))

        val itemLItems = itemL.asArray().items.toList();
        Assert.assertThat(itemLItems[0].typeDefinition, equalTo(StandardTypes.LONG))

    }

}