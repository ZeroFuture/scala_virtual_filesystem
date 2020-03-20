package com.scala.oop.commands

import com.scala.oop.files.{Directory, Entry}
import com.scala.oop.filesystem.State

abstract class CreateEntry(name: String) extends Command {
  override def apply(state: State): State = {
    val wd = state.wd
    if (wd.hasEntry(name))
      state.setMessage(s"Entry $name already exists!")
    else if (name.contains(Directory.SEPARATOR))
      state.setMessage(s"Entry $name must not contain separators!")
    else if (checkIllegal(name))
      state.setMessage(s"Entry $name is illegal!")
    else
      doCreateEntry(state, name)
  }

  def doCreateEntry(state: State, dirName: String): State = {
    def updateDir(currentDir: Directory, path: List[String], newEntry: Entry): Directory = {
      if (path.isEmpty)
        currentDir.addEntry(newEntry)
      else {
        val oldEntry = currentDir.findEntry(path.head).asDirectory
        currentDir.replaceEntry(oldEntry.name, updateDir(oldEntry, path.tail, newEntry))
      }
    }

    val wd = state.wd
    val allDirsInPath = wd.getAllDirsInPath
    val newEntry: Entry = createNewEntry(state)
    val newRoot = updateDir(state.root, allDirsInPath, newEntry)
    val newWd = newRoot.findDescendant(allDirsInPath)

    State(newRoot, newWd)
  }

  def checkIllegal(dirName: String): Boolean =
    dirName.contains(".")

  def createNewEntry(state: State): Entry
}
