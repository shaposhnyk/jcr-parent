package com.ljcr.filesystems

import com.ljcr.api.*
import com.ljcr.api.definitions.PropertyDefinition
import com.ljcr.api.definitions.StandardTypes
import com.ljcr.api.definitions.TypeDefinition
import com.ljcr.api.exceptions.UnsupportedRepositoryOperationException
import java.io.InputStream
import java.math.BigDecimal
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.stream.Stream


class FilesystemAdapter {

    companion object {
        val anyTypes = listOf(StandardTypes.UNKNOWN_PROPERTY)

        val objectType = object : TypeDefinition {
            override fun getIdentifier() = "file"

            override fun getDeclaredPropertyDefinitions(): MutableCollection<PropertyDefinition> {
                return anyTypes.toMutableList()
            }
        }
        val arrayType = object : TypeDefinition {
            override fun getIdentifier() = "directory"

            override fun getDeclaredPropertyDefinitions(): MutableCollection<PropertyDefinition> {
                return anyTypes.toMutableList()
            }
        }

        fun of(root: Path, p: Path): ImmutableNode {
            if (!p.isAbsolute) {
                return of(root, Paths.get("/").resolve(p))
            }

            return if (root.resolve(p).toFile().isDirectory) FsFolder(root, p)
            else FsFile(root, p)
        }

        fun createWs(root: Path): Repository {
            val jsonItem = of(root, Paths.get("/"));

            return object : Repository {
                override fun getName() = "json"

                override fun getRootNode() = jsonItem
            }
        }

        fun typeOf(json: Path): TypeDefinition {
            return StandardTypes.ANYTYPE;
        }
    }

}

class FsFolder(val root: Path, val p: Path) : ImmutableNodeObject {
    override fun getValue(): Any? = null

    fun getKey(): Path = p

    override fun getName() = if (p.nameCount == 0) "" else p.fileName.toString()

    override fun getItem(fieldName: String): ImmutableNode? {
        return FilesystemAdapter.of(root, p.resolve(fieldName))
    }

    override fun getItem(field: PropertyDefinition) = getItem(field.identifier)

    override fun getElements(): Stream<ImmutableNode> {
        val realPath = if (p.nameCount == 0) root
        else root.resolve(p.relativize(Paths.get("/")))
        val list = Files.newDirectoryStream(realPath)
                .map { FilesystemAdapter.of(root, root.relativize(it)) }
        return list // to be fixed. use real stream
                .stream()
    }

    override fun <T : Any?> accept(visitor: ImmutableItemVisitor<T>): T? {
        return visitor.visit(this)
    }

    override fun getTypeDefinition(): TypeDefinition = FilesystemAdapter.arrayType
}

class FsFile(val root: Path, val p: Path) : ImmutableNodeObject {
    override fun getValue(): Any? = null

    fun getKey(): Path = p

    override fun getName() = if (p.nameCount == 0) "" else p.fileName.toString()

    override fun getItem(fieldName: String): ImmutableNode? {
        val realPath = root.resolve(Paths.get("/").relativize(p))
        return if ("fileContent" == fieldName) GenericProperty("fileContent", p, FileContentNode(realPath))
        else GenericProperty(fieldName, p, Files.getAttribute(realPath, fieldName))
    }

    override fun getItem(field: PropertyDefinition) = getItem(field.identifier)

    override fun getElements(): Stream<ImmutableNode> {
        return listOf(getItem("fileContent"))
                .filterNotNull()
                .stream()
    }

    override fun <T : Any?> accept(visitor: ImmutableItemVisitor<T>): T? {
        return visitor.visit(this)
    }

    override fun getTypeDefinition(): TypeDefinition = FilesystemAdapter.objectType

}

data class GenericProperty(val fieldName: String, val p: Path, val objValue: Any) : ImmutableNode {
    override fun isObject(): Boolean = false;

    override fun isCollection(): Boolean = false;

    override fun isScalarValue(): Boolean = true;

    fun getKey(): Path = p.resolve(fieldName)

    override fun getName(): String = fieldName

    fun getPath(): Path = p

    override fun getValue(): Any = objValue

    override fun getItem(fieldName: String): ImmutableNode? = null

    override fun getItem(fieldName: PropertyDefinition) = getItem(fieldName.identifier)

    override fun getElements(): Stream<ImmutableNode> = Stream.empty()

    override fun getTypeDefinition(): TypeDefinition = StandardTypes.ANYTYPE

    override fun <T : Any?> accept(visitor: ImmutableItemVisitor<T>): T? {
        return visitor.visit(this)
    }
}

data class FileContentNode(val p: Path) : ImmutableNodeScalar {
    override fun getTypeDefinition(): TypeDefinition = StandardTypes.BINARY;

    override fun asLong(): Long = throw UnsupportedRepositoryOperationException()

    override fun asDouble(): Double = throw UnsupportedRepositoryOperationException()

    override fun asDecimal(): BigDecimal? = throw UnsupportedRepositoryOperationException()

    override fun asDate(): LocalDate? = throw UnsupportedRepositoryOperationException()

    override fun asDateTime(): LocalDateTime? = throw UnsupportedRepositoryOperationException()

    override fun asBoolean(): Boolean = throw UnsupportedRepositoryOperationException()

    override fun asObjectNode(): ImmutableNodeObject = throw UnsupportedRepositoryOperationException()

    override fun asArrayNode(): ImmutableNodeCollection = throw UnsupportedRepositoryOperationException()

    override fun getValue(): Any = p

    fun newInputStream(): InputStream {
        return Files.newInputStream(p)
    }

    override fun asString(): String {
        return java.util.Scanner(newInputStream())
                .useDelimiter("\\A")
                .next()
    }
}
