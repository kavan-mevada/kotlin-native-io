
# Kotlin Native File IO

## Path Object

An Path object that may be used to locate a file in a file system. It will typically represent a system dependent file path.

**Constructor and Description**

`Path (file: String)` Paths may be used with the `Files` files, directories, and other types of files.
<div class="sample" markdown="1" theme="idea" data-highlight-only>

```kotlin
val path = Path("/mnt/sotragle/test.txt")
// a Path object representing the absolute path
println(file.absolutePath)

// gives file name only
println(file.fileName)

// gives parent path of file
println(file.parent)
```
</div>

## File Object

A File object is created by passing in a String that represents the name of a file, or a String or another File object. 

**Constructor and Description**

`File(parent: File, child: String)` Creates a new File instance from a parent abstract pathname and a child pathname string.
`File(pathname: String)` Creates a new File instance by converting the given pathname string into an abstract pathname.
`File(parent: String, child: String)` Creates a new File instance from a parent pathname string and a child pathname string.

**Implementation**
<div class="sample" markdown="1" theme="idea" data-highlight-only>

```kotlin
 //pass the filename or directory name to File object
 val f = File(fname)
 
 //apply File class methods on File object
 println( "File name :" + f.name )
 println( "Path: " + f.path )
 println( "Absolute path:" + f.absolutePath )
 println( "Parent:" + f.parent )
 println( "Exists :" + f.exists )
 
 if(f.exists) {
   println( "Is writeable:" + f.isWritable )
   println( "Is readable" + f.isReadable )
}
```
</div>


## Streams

Library is useful to read data from a file in the form of sequence of bytes. FileInputStream is meant for reading streams of raw bytes such as image data.

**Constructor and Description**

```InputStream (file: File)``` Creates an input file stream to read from the specified File object.
```InputStream (name: String)``` Creates an input file stream to read from a file with the specified name.

**Implementation**
<div class="sample" markdown="1" theme="idea" data-highlight-only>

```kotlin
 //attach the file to FileInputStream
 val streamIn = InputStream("fileIn.txt")
 val streamOut = OutputStream("fileOut.txt")
	
 //illustrating getFD() method
 println(streamIn.getFD())
	
 //illustrating available method
 println( "Number of remaining bytes:" + streamIn.available())

 //illustrating skip method
 streamIn.skip(4)

 println( "FileContents :" )
 //read characters from FileInputStream and write them
 while (streamIn.available()) {

 	//read 1 byte
 	val ch = streamIn.read()
	println(ch.toChar())
	
	//read bytes in chunks
	val bytes = streamIn.read(3)
	
	//read bytes to buffer
	val buffer = ByteArray(3)
	val bytes = streamIn.read(buffer)
	println(bytes.toKString())
	
	//write to another file
	streamOut.write(bytes)
 }

 //close the file
 streamIn.close()
 streamOut.close()
```
</div>
