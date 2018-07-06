package com.shaposhnyk.jackson.adapter;

import com.fasterxml.jackson.databind.JsonNode
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.stream.Stream
import java.util.stream.StreamSupport
import javax.jcr.api.*
import javax.jcr.api.definitions.PropertyDefinition


interface X {

    fun createWs(json: JsonNode): Workspace {
        val jsonItem = JsonImmutableItem("/", Paths.get("/"), json);

        return object : Workspace {
            override fun getName() = "json"

            override fun getRootNode() = jsonItem
        }
    }

}

class JsonImmutableItem(val jsonName: String, val jsonPath: Path, val json: JsonNode) : ImmutableProperty {
    override fun getValue(): ImmutableValue {
        return JsonImmutableValue(json)
    }

    override fun getName(): String = jsonName

    override fun getPath(): Path = jsonPath

    override fun getItem(fieldName: String): ImmutableItem {
        return JsonImmutableItem(fieldName, jsonPath.resolve(jsonName), json.get(fieldName));
    }

    override fun getItems(): Stream<ImmutableItem> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isSame(otherItem: ImmutableItem?): Boolean {
        return this.equals(otherItem);
    }

    override fun accept(visitor: ImmutableItemVisitor) {
        if (json.isArray) {
            visitor.visit(JsonImmutableArrayNode(this))
        } else if (json.isObject) {
            visitor.visit(JsonImmutableObjectNode(this))
        } else {
            visitor.visit(this)
        }
    }

    override fun getDefinition(): PropertyDefinition? {
        return null;
    }

}

class JsonImmutableObjectNode(val item: JsonImmutableItem) : ImmutableObjectNode, ImmutableItem by item {
    override fun getIdentifier(): String {
        val ref = item.json.get("reference")
        return if (ref != null) ref.asText() else ""
    }

    override fun getItems(): Stream<ImmutableItem> {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(item.json.fields(), Spliterator.ORDERED),
                false)
                .map { f -> JsonImmutableItem(f.key, item.jsonPath.resolve(f.key), f.value) }
    }

    override fun getItemNames(): Stream<String> {
        val sourceIterator = item.json.fieldNames().iterator();
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(sourceIterator, Spliterator.ORDERED),
                false)
    }
}

class JsonImmutableArrayNode(val item: JsonImmutableItem) : ImmutableArrayNode, ImmutableItem by item {

}

class JsonImmutableValue(val json: JsonNode) : ImmutableValue {
    override fun getString() = json.asText()

    override fun getBoolean(): Boolean = json.asBoolean()

    override fun getLong() = json.asLong()

    override fun getDouble(): Double = json.asDouble()
}
