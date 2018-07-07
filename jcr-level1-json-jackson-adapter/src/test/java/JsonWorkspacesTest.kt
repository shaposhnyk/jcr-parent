package com.shaposhnyk.jackson.adapter

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.CoreMatchers.*
import org.junit.Assert
import org.junit.Test
import javax.jcr.api.definitions.StandardPropertyTypes
import kotlin.streams.toList

class JsonWorkspacesTest {

    val om = ObjectMapper()

    @Test
    fun simpleCases() {
        val json = om.readTree("""{
            |"myFieldS": "myValue",
            |"myFieldL": 1234567890,
            |"myFieldB": true,
            |"myFieldN": null
            |}""".trimMargin())
        val ws = JsonWorkspaces.createWs(json)

        Assert.assertThat(ws.rootNode.name, equalTo(""))
        Assert.assertThat(ws.rootNode.isArrayNode, equalTo(false))
        Assert.assertThat(ws.rootNode.isObjectNode, equalTo(true))
        Assert.assertThat(ws.rootNode.typeDefinition.identifier, equalTo("object"))

        Assert.assertThat(ws.rootNode.getItem("myFieldS"), notNullValue())
        Assert.assertThat(ws.rootNode.getItem("myFieldS").asProperty().string, equalTo("myValue"))
        Assert.assertThat(ws.rootNode.getItem("myFieldS").typeDefinition, equalTo(StandardPropertyTypes.STRING))
        Assert.assertThat(ws.rootNode.getItem("myFieldS").asProperty().typeDefinition, equalTo(StandardPropertyTypes.STRING))

        Assert.assertThat(ws.rootNode.getItem("myFieldL"), notNullValue())
        Assert.assertThat(ws.rootNode.getItem("myFieldL").asProperty().long, equalTo(1234567890L))
        Assert.assertThat(ws.rootNode.getItem("myFieldL").typeDefinition, equalTo(StandardPropertyTypes.LONG))
        Assert.assertThat(ws.rootNode.getItem("myFieldL").asProperty().typeDefinition, equalTo(StandardPropertyTypes.LONG))

        Assert.assertThat(ws.rootNode.getItem("myFieldB"), notNullValue())
        Assert.assertThat(ws.rootNode.getItem("myFieldB").asProperty().boolean, equalTo(true))
        Assert.assertThat(ws.rootNode.getItem("myFieldB").typeDefinition, equalTo(StandardPropertyTypes.BOOLEAN))
        Assert.assertThat(ws.rootNode.getItem("myFieldB").asProperty().typeDefinition, equalTo(StandardPropertyTypes.BOOLEAN))

        Assert.assertThat(ws.rootNode.getItem("myFieldN"), notNullValue())
        Assert.assertThat(ws.rootNode.getItem("myFieldN").asProperty().string, nullValue())
        Assert.assertThat(ws.rootNode.getItem("myFieldN").typeDefinition, equalTo(StandardPropertyTypes.UNDEFINED))
        Assert.assertThat(ws.rootNode.getItem("myFieldN").asProperty().typeDefinition, equalTo(StandardPropertyTypes.UNDEFINED))
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
        val ws = JsonWorkspaces.createWs(json)

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
        Assert.assertThat(itemSItems[0].typeDefinition, equalTo(StandardPropertyTypes.STRING))

        val itemL = ws.rootNode.getItem("myFieldL")
        Assert.assertThat(itemL.isArrayNode, equalTo(true))
        Assert.assertThat(itemL.asArray().items
                .map { it.asProperty().long }
                .toList(), equalTo(listOf(123L, 456L)))
        Assert.assertThat(itemL.asArray().items
                .map { it.asProperty().string }
                .toList(), equalTo(listOf("123", "456")))

        val itemLItems = itemL.asArray().items.toList();
        Assert.assertThat(itemLItems[0].typeDefinition, equalTo(StandardPropertyTypes.LONG))

    }

}