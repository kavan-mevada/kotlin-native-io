package io.streams

import io.File
import kotlinx.cinterop.*
import platform.posix.*


open class InputStream {

    private lateinit var file : File

    constructor(path: String) {
        file = File(path).apply { validate }
    }

    constructor(file: File): this(file.absolutePath) {
        this.file = file
    }


    private var fd = open(file.absolutePath, O_RDONLY)
    private var contentSize = nativeHeap.alloc<stat>().apply { stat(file.absolutePath, this.ptr) }.st_size -1

    private var bytesRead = 0L


    private fun readBytes(bytes: ByteArray): ssize_t {
        return bytes.usePinned {
            platform.posix.read(fd, it.addressOf(0), bytes.size.convert())
        }
    }

    fun read(buffer: ByteArray): ByteArray {
        bytesRead += readBytes(buffer)
        //if (ret == 0L) break; /* EOF */
        //if (ret == -1L) { break; /* Handle error */ }
        return buffer
    }

    fun read(chunk: Int): ByteArray {
        val dbuff = if (contentSize-bytesRead > chunk) chunk else (contentSize-bytesRead).convert()
        return read(ByteArray(dbuff))
    }

    fun read() = read(1)[0]

    fun skip(n: Long): Long = lseek(fd, n.convert(), SEEK_SET)

    fun available() = bytesRead < contentSize

    fun getFD() = fd

    fun close() = close(fd)

    //val dbuff = if (contentSize-bytesRead > buffer.size) buffer.size.toInt() else (contentSize-bytesRead)
}