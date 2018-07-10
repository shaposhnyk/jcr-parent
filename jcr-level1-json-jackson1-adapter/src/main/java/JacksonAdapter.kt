package com.ljcr.jackson1x

import com.ljcr.api.*
import com.ljcr.api.definitions.PropertyDefinition
import com.ljcr.api.definitions.StandardTypes
import com.ljcr.api.definitions.TypeDefinition
import com.ljcr.api.exceptions.UnsupportedRepositoryOperationException
import org.codehaus.jackson.JsonNode
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
                override fun getName() = "jNode"

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
) : @JvmDefault ImmutableValue by jsonValue, ImmutableNode {
    override fun getValue(): Any? = jsonValue.value

    override fun asString(): String? {
        return jsonValue.asString()
    }

    override fun asDecimal(): BigDecimal? {
        return jsonValue.asDecimal()
    }

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

data class JsonImmutableObjectNode(val iNode: JsonImmutableNode) : ImmutableNode by iNode, ImmutableObjectNode {
    override fun getValue(): Any? = null

    override fun getReference(): String {
        val ref = iNode.jsonValue.jNode.get("reference")
        return if (ref != null) ref.asText() else ""
    }

    override fun getItem(fieldName: String): ImmutableNode? {
        return JsonAdapter.of(iNode.jsonPath.resolve(fieldName), iNode.jsonValue.jNode.get(fieldName))
    }

    override fun getItems(): Stream<ImmutableNode> {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(iNode.jsonValue.jNode.fields, Spliterator.ORDERED), false)
                .map { f -> JsonAdapter.of(iNode.jsonPath.resolve(f.key), f.value) }
    }

    override fun getFieldNames(): Stream<String> {
        val sourceIterator = iNode.jsonValue.jNode.fieldNames.iterator();
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(sourceIterator, Spliterator.ORDERED),
                false)
    }
}

data class JsonImmutableArrayNode(val iNode: JsonImmutableNode) : @JvmDefault ImmutableNode by iNode, ImmutableArrayNode {
    override fun getValue(): Collection<ImmutableNode> {
        return items.toList()
    }

    override fun getItems(): Stream<ImmutableNode> {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(iNode.jsonValue.jNode.elements, Spliterator.ORDERED), false)
                .map { f -> JsonAdapter.of(iNode.jsonPath.resolve("0"), f) }
    }
}

open class JsonImmutableValue(open val jNode: JsonNode) : ImmutableValue {
    override fun asDate(): LocalDate? = null

    override fun asDateTime(): LocalDateTime? = null

    override fun asBinaryValue(): ImmutableBinaryValue = throw UnsupportedRepositoryOperationException()

    override fun asObjectNode(): ImmutableObjectNode = throw UnsupportedRepositoryOperationException()

    override fun asArrayNode(): ImmutableArrayNode = throw UnsupportedRepositoryOperationException()

    override fun getValue(): Any? = jNode

    override fun asString() = jNode.asText()

    override fun asBoolean(): Boolean = jNode.asBoolean()

    override fun asLong() = jNode.asLong()

    override fun asDouble(): Double = jNode.asDouble()

    override fun asDecimal(): BigDecimal? = jNode.decimalValue
}

class JsonImmutableNull(override val jNode: JsonNode) : @JvmDefault JsonImmutableValue(jNode) {
    override fun asString(): String? = null

    override fun asDecimal(): BigDecimal? = null

    override fun asDate() = null

    override fun asDateTime() = null

    override fun asBoolean() = throw UnsupportedRepositoryOperationException()

    override fun asLong() = throw UnsupportedRepositoryOperationException()

    override fun asDouble() = throw UnsupportedRepositoryOperationException()
}
