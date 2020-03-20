package com.scala.oop.commands

import com.scala.oop.files.{Directory, Entry}
import com.scala.oop.filesystem.State

class Mkdir(name: String) extends CreateEntry(name) {
  override def createNewEntry(state: State): Entry =
    Directory.empty(state.wd.path, name)
}
