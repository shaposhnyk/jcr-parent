package com.ljcr.filesystems

import com.ljcr.api.*
import com.ljcr.api.definitions.PropertyDefinition
import com.ljcr.api.definitions.StandardTypes
import com.ljcr.api.definitions.TypeDefinition
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream


class FilesystemAdapter {

    companion object {
        val anyTypes = listOf(StandardTypes.ANYTYPE)

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

        fun of(root: Path, p: Path): ImmutableItem {
            if (!p.isAbsolute) {
                return of(root, Paths.get("/").resolve(p))
            }

            return if (root.resolve(p).toFile().isDirectory) FsFolder(root, p)
            else FsFile(root, p)
        }

        fun createWs(root: Path): Workspace {
            val jsonItem = of(root, Paths.get("/"));

            return object : Workspace {
                override fun getName() = "json"

                override fun getRootNode() = jsonItem
            }
        }

        fun typeOf(json: Path): TypeDefinition {
            return StandardTypes.UNDEFINED
        }
    }

}

class FsFolder(val root: Path, val p: Path) : ImmutableObjectNode {
    override fun getIdentifier(): String = p.toString()

    override fun getName(): String {
        return if (p.nameCount == 0) ""
        else p.fileName.toString()
    }

    override fun getPath(): Path = p.parent

    override fun getItem(fieldName: String?): ImmutableItem {
        return FilesystemAdapter.of(root, p.resolve(fieldName))
    }

    override fun getItems(): Stream<ImmutableItem> {
        val realPath = if (p.nameCount == 0) root
        else root.resolve(p.relativize(Paths.get("/")))
        val list = Files.newDirectoryStream(realPath)
                .map { FilesystemAdapter.of(root, root.relativize(it)) }
        return list // to be fixed. use real stream
                .stream()
    }

    override fun isSame(otherItem: ImmutableItem?): Boolean {
        return this == otherItem
    }

    override fun accept(visitor: ImmutableItemVisitor) {
        visitor.visit(this)
    }

    override fun getTypeDefinition(): TypeDefinition = FilesystemAdapter.arrayType
}

class FsFile(val root: Path, val p: Path) : ImmutableObjectNode {
    override fun getName(): String = p.fileName.toString() // root object (/) cannot be a file

    override fun getPath(): Path = p.parent

    override fun getItem(fieldName: String): ImmutableItem {
        val realPath = root.resolve(Paths.get("/").relativize(p))
        return if ("fileContent" == fieldName) GenericProperty("fileContent", p, FileContentValue(realPath))
        else GenericProperty(fieldName, p, Files.getAttribute(realPath, fieldName))
    }

    override fun getItems(): Stream<ImmutableItem> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getIdentifier(): String = p.toString()

    override fun isSame(otherItem: ImmutableItem?): Boolean {
        return this == otherItem
    }

    override fun accept(visitor: ImmutableItemVisitor) {
        visitor.visit(this)
    }

    override fun getTypeDefinition(): TypeDefinition = FilesystemAdapter.objectType

}

class GenericProperty(val fieldName: String, val p: Path, val value: Any) : ImmutableProperty {
    override fun getName() = fieldName

    override fun getItem(fieldName: String?) = null;

    override fun getValue(): ImmutableValue = if (value is FileContentValue) value else GenericValue(value)

    override fun getPath(): Path = p

    override fun isSame(otherItem: ImmutableItem?): Boolean {
        return this == otherItem
    }

    override fun accept(visitor: ImmutableItemVisitor) {
        visitor.visit(this)
    }

}

data class FileContentValue(val p: Path) : ImmutableValue, ImmutableBinaryValue {
    override fun dispose() {
        TODO("not implementwed") //To change body of created functions use File | Settings | File Templates.
    }

    override fun size(): Long = Files.size(p)

    override fun getStream(): InputStream {
        return Files.newInputStream(p)
    }

    override fun getString(): String {
        return java.util.Scanner(stream)
                .useDelimiter("\\A")
                .next()
    }
}

data class GenericValue(val value: Any) : ImmutableValue {
    override fun getString(): String = value.toString()
}

data class GenericStringValue(val value: String) : ImmutableValue {
    override fun getString(): String = value
}

data class GenericLongValue(val value: Long) : ImmutableValue {
    override fun getString(): String = value.toString()

    override fun getLong(): Long = value
}
