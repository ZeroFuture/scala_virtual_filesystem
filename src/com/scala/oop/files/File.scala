package com.scala.oop.files

import com.scala.oop.filesystem.FileSystemException

class File(override val parentPath: String, override val name: String, val contents: String)
  extends Entry(parentPath, name) {
  override def asDirectory: Directory = throw new FileSystemException(s"File $name cannot be converted to a directory")

  override def getType: String = "File"

  override def asFile: File = this

  override def isDirectory: Boolean = false

  override def isFile: Boolean = true

  def setContents(newContents: String): File = new File(parentPath, name, newContents)

  def appendContents(newContents: String): File = new File(parentPath, name, contents + "\n" + newContents)
}

object File {
  def empty(parentPath: String, name: String): File =
    new File(parentPath, name, "")
}
