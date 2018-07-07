package com.ljcr.filesystems

import com.ljcr.api.ImmutableProperty
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList

class FilesystemAdapterTest {

    val root = Paths.get("/tmp/ljcr")

    @Before
    fun setup() {
        root.toFile().mkdirs();
    }

    @After()
    fun teardown() {
        root.toFile().deleteRecursively()
    }

    @Test
    fun simpleValuesAreOk() {
        val tempFile = File.createTempFile("ljcr", "", root.toFile())
        FileOutputStream(tempFile).use {
            it.write("someText".toByteArray(StandardCharsets.UTF_8))
        }
        Assert.assertThat(Files.size(tempFile.toPath()), equalTo(8L))

        val tempFile2 = File.createTempFile("ljcr", "", root.toFile())

        val ws = FilesystemAdapter.createWs(root)

        Assert.assertThat(ws.rootNode.name, equalTo(""))
        Assert.assertThat(ws.rootNode.isObjectNode, equalTo(true))
        Assert.assertThat(ws.rootNode.typeDefinition.identifier, equalTo("directory"))

        val files = ws.rootNode.items.toList()
        Assert.assertThat(files.map { f -> f.name }, hasItem(tempFile.name))
        Assert.assertThat(files.map { f -> f.name }, hasItem(tempFile2.name))

        val item = ws.rootNode.getItem(tempFile.name)
        Assert.assertThat(item, CoreMatchers.notNullValue())
        Assert.assertThat(item.name, equalTo(tempFile.name))
        Assert.assertThat(item.typeDefinition.identifier, equalTo("file"))

        Assert.assertThat((item.getItem("fileContent") as ImmutableProperty).value.string, equalTo("someText"))
        Assert.assertThat((item.getItem("size") as ImmutableProperty).value.long, equalTo(8L))
        Assert.assertThat((item.getItem("isRegularFile") as ImmutableProperty).value.boolean, equalTo(true))
        Assert.assertThat((item.getItem("creationTime") as ImmutableProperty).value.dateTime, notNullValue())
    }

}