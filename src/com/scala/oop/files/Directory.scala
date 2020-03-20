package com.scala.oop.files

import com.scala.oop.filesystem.FileSystemException

import scala.annotation.tailrec

class Directory(override val parentPath: String, override val name: String, val contents: List[Entry])
  extends Entry(parentPath, name) {

  override def asDirectory: Directory = this

  override def getType: String = "Directory"

  override def asFile: File = throw new FileSystemException(s"Directory $name cannot be converted to a File!")

  override def isDirectory: Boolean = true

  override def isFile: Boolean = false

  def hasEntry(entryName: String): Boolean =
    findEntry(entryName) != null

  def getAllDirsInPath: List[String] =
    path.substring(1).split(Directory.SEPARATOR).toList.filter(!_.isEmpty)

  def findDescendant(pathList: List[String]): Directory = {
    if (pathList.isEmpty) this
    else findEntry(pathList.head).asDirectory.findDescendant(pathList.tail)
  }

  def findDescendant(relativePath: String): Directory = {
    if (relativePath.isEmpty) this
    else findDescendant(relativePath.split(Directory.SEPARATOR).toList)
  }

  def addEntry(newEntry: Entry): Directory =
    new Directory(parentPath, name, contents :+ newEntry)

  def removeEntry(entryName: String): Directory = {
    if (!hasEntry(entryName)) this
    else new Directory(parentPath, name, contents.filter(!_.name.equals(entryName)))
  }

  def findEntry(entryName: String): Entry = {
    @tailrec
    def findEntryHelper(name: String, contents: List[Entry]): Entry = {
      if (contents.isEmpty) null
      else if (name.equals(contents.head.name)) contents.head
      else findEntryHelper(name, contents.tail)
    }

    findEntryHelper(entryName, contents)
  }

  def replaceEntry(entryName: String, newEntry: Entry): Directory =
    new Directory(parentPath, name, contents.filter(!_.name.equals(entryName)) :+ newEntry)

  def isRoot: Boolean = parentPath.isEmpty
}

object Directory {
  val SEPARATOR = "/"
  val ROOT_PATH = "/"

  def ROOT: Directory = Directory.empty("", "")

  def empty(parentPath: String, name: String): Directory =
    new Directory(parentPath, name, List())
}
