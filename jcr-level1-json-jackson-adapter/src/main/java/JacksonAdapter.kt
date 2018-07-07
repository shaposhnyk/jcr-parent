package com.shaposhnyk.jackson.adapter

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.stream.Stream
import java.util.stream.StreamSupport
import javax.jcr.api.*
import javax.jcr.api.definitions.StandardPropertyTypes
import javax.jcr.api.definitions.TypeDefinition
import javax.jcr.api.exceptions.UnsupportedRepositoryOperationException


class JsonWorkspaces {

    companion object {

        val objectType = TypeDefinition { "object" };
        val arrayType = TypeDefinition { "array" };

        fun of(p: Path, json: JsonNode): ImmutableItem {
            val item = JsonImmutableItem(p, json)
            if (json.isObject) {
                return JsonImmutableObjectNode(item)
            } else if (json.isArray) {
                return JsonImmutableArrayNode(item)
            }
            return item;
        }

        fun createWs(json: JsonNode): Workspace {
            val jsonItem = of(Paths.get("/"), json);

            return object : Workspace {
                override fun getName() = "json"

                override fun getRootNode() = jsonItem
            }
        }

        fun typeOf(json: JsonNode): TypeDefinition {
            if (json.isTextual) {
                return StandardPropertyTypes.STRING
            } else if (json.isBoolean) {
                return StandardPropertyTypes.BOOLEAN
            } else if (json.isLong || json.isIntegralNumber) {
                return StandardPropertyTypes.LONG
            } else if (json.isBigDecimal) {
                return StandardPropertyTypes.DECIMAL
            } else if (json.isDouble || json.isFloat) {
                return StandardPropertyTypes.DOUBLE
            } else if (json.isBinary) {
                return StandardPropertyTypes.BINARY
            }

            return StandardPropertyTypes.UNDEFINED
        }
    }

}

class JsonImmutableItem(val jsonPath: Path, val json: JsonNode) : ImmutableProperty {
    private val fieldName = if (jsonPath.nameCount == 0) "" else jsonPath.fileName.toString()

    override fun getName(): String = fieldName;

    override fun getPath(): Path = jsonPath.parent

    override fun getValue(): ImmutableValue {
        return if (json.isNull) JsonImmutableNull()
        else JsonImmutableValue(json)
    }

    override fun getItem(fieldName: String): ImmutableItem {
        return JsonWorkspaces.of(jsonPath.resolve(fieldName), json.get(fieldName));
    }

    override fun getItems(): Stream<ImmutableItem> {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(json.fields(), Spliterator.ORDERED), false)
                .map { JsonWorkspaces.of(jsonPath.resolve(it.key), it.value) }
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

    override fun getTypeDefinition(): TypeDefinition = JsonWorkspaces.typeOf(json)
}

class JsonImmutableObjectNode(val item: JsonImmutableItem) : ImmutableObjectNode, ImmutableItem by item {
    override fun getIdentifier(): String {
        val ref = item.json.get("reference")
        return if (ref != null) ref.asText() else ""
    }

    override fun isObjectNode(): Boolean = true

    override fun getTypeDefinition(): TypeDefinition = JsonWorkspaces.objectType

    override fun getItems(): Stream<ImmutableItem> {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(item.json.fields(), Spliterator.ORDERED), false)
                .map { f -> JsonImmutableItem(item.jsonPath.resolve(f.key), f.value) }
    }

    override fun getItemNames(): Stream<String> {
        val sourceIterator = item.json.fieldNames().iterator();
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(sourceIterator, Spliterator.ORDERED),
                false)
    }
}

class JsonImmutableArrayNode(val item: JsonImmutableItem) : ImmutableArrayNode, ImmutableItem by item {

    override fun isArrayNode(): Boolean = true

    override fun getTypeDefinition(): TypeDefinition = JsonWorkspaces.arrayType

    override fun getItems(): Stream<ImmutableItem> {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize((item.json as ArrayNode).elements(), Spliterator.ORDERED), false)
                .map { f -> JsonImmutableItem(item.jsonPath.resolve("0"), f) }
    }
}

class JsonImmutableValue(val json: JsonNode) : ImmutableValue {
    override fun getString() = json.asText()

    override fun getBoolean(): Boolean = json.asBoolean()

    override fun getLong() = json.asLong()

    override fun getDouble(): Double = json.asDouble()

    override fun getTypeDefinition(): TypeDefinition = JsonWorkspaces.typeOf(json)
}

class JsonImmutableNull : ImmutableValue {
    override fun getString() = null

    override fun getBoolean(): Boolean = throw UnsupportedRepositoryOperationException()

    override fun getLong() = throw UnsupportedRepositoryOperationException()

    override fun getDouble(): Double = throw UnsupportedRepositoryOperationException()

    override fun getTypeDefinition(): TypeDefinition = StandardPropertyTypes.UNDEFINED
}
