package com.scala.oop.commands

import com.scala.oop.files.{Entry, File}
import com.scala.oop.filesystem.State

class Touch(name: String) extends CreateEntry(name) {
  override def createNewEntry(state: State): Entry =
    File.empty(state.wd.path, name)
}
