package io

/**
 *  IO Exceptions
 * **/
class FileNotFoundException(fileName: String, reason: String): Exception("FileNotFoundException: $fileName ($reason)")