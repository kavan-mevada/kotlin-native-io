package io.streams

import io.File
import kotlinx.cinterop.*
import platform.posix.*

open class OutputStream {

    private lateinit var file : File

    constructor(path: String, append: Boolean = false) {
        file = File(path).apply { validate }
    }

    constructor(file: File, append: Boolean = false): this(file.absolutePath, append) {
        this.file = file
    }


    private var fd: Int = open(file.absolutePath, O_RDWR  or O_CREAT)
    private var contentSize = nativeHeap.alloc<stat>().apply { stat(file.absolutePath, this.ptr) }.st_size -1


    private fun writeBytes(bytes: ByteArray, offset: Long = 0L): ssize_t {
        if (offset>0) skip(offset)
        return bytes.usePinned {
            write(fd, it.addressOf(0), bytes.size.convert())
        }
    }

    fun write(bytes: ByteArray) = writeBytes(bytes)

    fun write(byte: Byte) = writeBytes(byteArrayOf(byte))

    fun skip(n: Long): Long = lseek(fd, n.convert(), SEEK_SET)

    fun close() = close(fd)

    fun getFD() = fd
}
