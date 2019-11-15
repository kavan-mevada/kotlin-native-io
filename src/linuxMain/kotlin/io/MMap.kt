package io

import kotlinx.cinterop.*
import platform.posix.*


final class MMap(path: String) {
    val file = File(path)
    val fd = open(file.absolutePath, O_RDONLY)
    val size = nativeHeap.alloc<stat>().apply { fstat(fd, ptr) }.st_size.convert<Int>() - 1
    val mapped = mmap(null, size.convert(), PROT_READ, MAP_PRIVATE, fd, 0)

    fun read(chunk: Int, off: Int = 0, func: (bytes: ByteArray) -> Unit) {
        for (index in off until off+size step chunk) {
            val sourceArray = mapped?.reinterpret<ByteVar>()?.pointed?.ptr

            val dyChuck = if (index+chunk>off+size) (off+size % chunk) else chunk

            val dest = ByteArray(dyChuck)
            for (j in 0 until dyChuck.convert()) {
                dest[j] = sourceArray?.get(j+index) ?: 0
            }
            func(dest)
        }
    }
}