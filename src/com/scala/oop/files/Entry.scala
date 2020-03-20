package com.scala.oop.files

abstract class Entry(val parentPath: String, val name: String) {
  def path: String = {
    if (parentPath.equals(Directory.ROOT_PATH)) s"$parentPath$name"
    else s"$parentPath${Directory.SEPARATOR}$name"
  }

  def asDirectory: Directory

  def asFile: File

  def isDirectory: Boolean

  def isFile: Boolean

  def getType: String
}
