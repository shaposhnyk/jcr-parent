package com.ljcr.jackson2x

import com.fasterxml.jackson.databind.JsonNode
import com.ljcr.api.*
import com.ljcr.api.definitions.PropertyDefinition
import com.ljcr.api.definitions.StandardTypes
import com.ljcr.api.definitions.TypeDefinition
import com.ljcr.api.exceptions.UnsupportedRepositoryOperationException
import java.math.BigDecimal
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Stream
import java.util.stream.StreamSupport
import kotlin.streams.toList


class JsonAdapter {

    companion object {
        val anyTypes = listOf(StandardTypes.ANYTYPE)

        val objectType = object : TypeDefinition {
            override fun getIdentifier() = "object"

            override fun getDeclaredPropertyDefinitions(): MutableCollection<PropertyDefinition> {
                return anyTypes.toMutableList()
            }
        }
        val arrayType = object : TypeDefinition {
            override fun getIdentifier() = "array"

            override fun getDeclaredPropertyDefinitions(): MutableCollection<PropertyDefinition> {
                return anyTypes.toMutableList()
            }
        }

        fun of(p: Path, json: JsonNode): ImmutableNode {
            if (json.isObject) {
                val item = JsonImmutableNode(p, JsonImmutableValue(json)) { objectType }
                return JsonImmutableObjectNode(item)
            } else if (json.isArray) {
                val item = JsonImmutableNode(p, JsonImmutableValue(json)) { arrayType }
                return JsonImmutableArrayNode(item)
            } else if (json.isNull) {
                return JsonImmutableNode(p, JsonImmutableNull(json)) { StandardTypes.UNDEFINED }
            }
            return JsonImmutableNode(p, JsonImmutableValue(json)) { typeOf(json) }
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
                return StandardTypes.STRING
            } else if (json.isBoolean) {
                return StandardTypes.BOOLEAN
            } else if (json.isLong || json.isIntegralNumber) {
                return StandardTypes.LONG
            } else if (json.isBigDecimal) {
                return StandardTypes.DECIMAL
            } else if (json.isDouble || json.isFloatingPointNumber) {
                return StandardTypes.DOUBLE
            } else if (json.isBinary) {
                return StandardTypes.BINARY
            }

            return StandardTypes.UNDEFINED
        }
    }

}

data class JsonImmutableNode(
        val jsonPath: Path,
        val jsonValue: JsonImmutableValue,
        val jsonType: () -> TypeDefinition
) : ImmutableValue by jsonValue, ImmutableNode {
    override fun getKey(): Path = jsonPath

    override fun getItem(fieldName: String): ImmutableNode? {
        return null
    }

    override fun getItems(): Stream<ImmutableNode> = Stream.empty()

    override fun accept(visitor: ImmutableItemVisitor) {
        visitor.visit(this)
    }

    override fun getTypeDefinition(): TypeDefinition = jsonType()
}

data class JsonImmutableObjectNode(val item: JsonImmutableNode) : ImmutableNode by item, ImmutableObjectNode {
    override fun getValue(): Any? = null

    override fun getReference(): String {
        val ref = item.jsonValue.json.get("reference")
        return if (ref != null) ref.asText() else ""
    }

    override fun getItem(fieldName: String): ImmutableNode? {
        return JsonAdapter.of(item.jsonPath.resolve(fieldName), item.jsonValue.json.get(fieldName))
    }

    override fun getItems(): Stream<ImmutableNode> {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(item.jsonValue.json.fields(), Spliterator.ORDERED), false)
                .map { f -> JsonAdapter.of(item.jsonPath.resolve(f.key), f.value) }
    }

    override fun getFieldNames(): Stream<String> {
        val sourceIterator = item.jsonValue.json.fieldNames().iterator();
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(sourceIterator, Spliterator.ORDERED),
                false)
    }
}

data class JsonImmutableArrayNode(val item: JsonImmutableNode) : @JvmDefault ImmutableNode by item, ImmutableArrayNode {
    override fun getValue(): Collection<ImmutableNode> {
        return items.toList()
    }

    override fun getItems(): Stream<ImmutableNode> {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(item.jsonValue.json.elements(), Spliterator.ORDERED), false)
                .map { f -> JsonAdapter.of(item.jsonPath.resolve("0"), f) }
    }
}

open class JsonImmutableValue(open val json: JsonNode) : ImmutableValue {
    override fun asDecimal(): BigDecimal? = json.decimalValue()

    override fun asDate(): LocalDate? = null

    override fun asDateTime(): LocalDateTime? = null

    override fun asBinaryValue(): ImmutableBinaryValue = throw UnsupportedRepositoryOperationException()

    override fun asObjectNode(): ImmutableObjectNode = throw UnsupportedRepositoryOperationException()

    override fun asArrayNode(): ImmutableArrayNode = throw UnsupportedRepositoryOperationException()

    override fun getValue(): Any? = json

    override fun asString() = json.asText()

    override fun asBoolean(): Boolean = json.asBoolean()

    override fun asLong() = json.asLong()

    override fun asDouble(): Double = json.asDouble()
}

class JsonImmutableNull(override val json: JsonNode) : JsonImmutableValue(json) {
    override fun asString() = null

    override fun asDecimal() = null

    override fun asDate() = null

    override fun asDateTime() = null

    override fun asBoolean() = throw UnsupportedRepositoryOperationException()

    override fun asLong() = throw UnsupportedRepositoryOperationException()

    override fun asDouble() = throw UnsupportedRepositoryOperationException()
}
