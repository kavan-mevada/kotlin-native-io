package io

import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import platform.posix.*

/**
 *  File Class Declaration
 * **/

data class File (internal val filePath: Path) {
    constructor(path: String) : this(Path(path))
    constructor(parent: Path, child: String) : this(parent.resolve(child))
    constructor(parent: File, child: String) : this(parent.systemPath.resolve(child))

    val path = filePath
    val absolutePath = filePath.absolutePath
    val systemPath = Path(filePath.absolutePath)

    val name: String
        get() = filePath.fileName

    val extension: String
        get() = name.substringAfterLast('.', "")

    val parent: String
        get() = filePath.parent

    val exists
        get() = access(filePath.absolutePath, F_OK) != -1

    val isReadable
        get() = access(filePath.absolutePath, R_OK) != -1

    val isWritable
        get() = access(filePath.absolutePath, W_OK) != -1

    fun child(name: String) = File(this, name)



    // Useful POSIX Functions
    val size get() =
        nativeHeap.alloc<stat>().let {
            if (stat(absolutePath, it.ptr) == 0) it.st_size.convert<Long>()-1 else 0L
        }

    val validate get() = run {
        if (!exists)
            throw FileNotFoundException(absolutePath, "No such file or directory")
        else if (!isReadable)
            throw FileNotFoundException(absolutePath, "Access is denied")
    }
}