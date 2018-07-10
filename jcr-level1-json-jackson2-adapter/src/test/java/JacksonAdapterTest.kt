package com.ljcr.jackson2x

import com.fasterxml.jackson.databind.ObjectMapper
import com.ljcr.api.definitions.StandardTypes
import org.hamcrest.CoreMatchers.*
import org.junit.Assert
import org.junit.Test
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
        Assert.assertThat(ws.rootNode.getItem("myFieldS")?.asString(), equalTo("myValue"))

        Assert.assertThat(ws.rootNode.getItem("myFieldL"), notNullValue())
        Assert.assertThat(ws.rootNode.getItem("myFieldL")?.asLong(), equalTo(1234567890L))

        Assert.assertThat(ws.rootNode.getItem("myFieldB"), notNullValue())
        Assert.assertThat(ws.rootNode.getItem("myFieldB")?.asBoolean(), equalTo(true))

        val nullNode = ws.rootNode.getItem("myFieldN")
        Assert.assertThat(nullNode, notNullValue())
        Assert.assertThat(nullNode?.asString(), nullValue())
        Assert.assertThat(nullNode?.asDecimal(), nullValue())
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
        Assert.assertThat(ws.rootNode.getItem("myFieldS")?.typeDefinition, equalTo(StandardTypes.STRING))

        Assert.assertThat(ws.rootNode.getItem("myFieldL"), notNullValue())
        Assert.assertThat(ws.rootNode.getItem("myFieldL")?.typeDefinition, equalTo(StandardTypes.LONG))

        Assert.assertThat(ws.rootNode.getItem("myFieldB"), notNullValue())
        Assert.assertThat(ws.rootNode.getItem("myFieldB")?.typeDefinition, equalTo(StandardTypes.BOOLEAN))

        Assert.assertThat(ws.rootNode.getItem("myFieldN"), notNullValue())
        Assert.assertThat(ws.rootNode.getItem("myFieldN")?.typeDefinition, equalTo(StandardTypes.UNDEFINED))

        Assert.assertThat(ws.rootNode.getItem("myObject"), notNullValue())
        Assert.assertThat(ws.rootNode.getItem("myObject")!!.typeDefinition.identifier, equalTo("object"))
        Assert.assertThat(ws.rootNode.getItem("myObject")!!.items.toList().size, equalTo(2))
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

        val itemS = ws.rootNode.getItem("myFieldS")!!
        Assert.assertThat(itemS.isArrayNode, equalTo(true))
        Assert.assertThat(itemS.typeDefinition.identifier, equalTo("array"))

        Assert.assertThat(itemS!!.asArrayNode().items
                .map { it.asString() }
                .toList() as List<String>, equalTo(listOf("myValue1", "myValue2")))

        val itemSItems = itemS.asArrayNode().items.toList();
        Assert.assertThat(itemSItems[0].typeDefinition, equalTo(StandardTypes.STRING))

        val itemL = ws.rootNode.getItem("myFieldL")!!
        Assert.assertThat(itemL.isArrayNode, equalTo(true))
        Assert.assertThat(itemL.asArrayNode().items
                .map { it.asLong() }
                .toList(), equalTo(listOf(123L, 456L)))
        Assert.assertThat(itemL!!.asArrayNode().items
                .map { it.asString() }
                .toList() as List<String>, equalTo(listOf("123", "456")))

        val itemLItems = itemL.asArrayNode().items.toList();
        Assert.assertThat(itemLItems[0].typeDefinition, equalTo(StandardTypes.LONG))

    }

}