package com.scala.oop.commands

import com.scala.oop.files.Directory
import com.scala.oop.filesystem.State

class Rm(name: String) extends Command {
  override def apply(state: State): State = {
    val wd = state.wd
    val absolutePath =
      if (name.startsWith(Directory.SEPARATOR)) name
      else if (wd.isRoot) wd.path + name
      else wd.path + Directory.SEPARATOR + name

    if (Directory.ROOT_PATH.equals(absolutePath))
      state.setMessage("Root can not be removed!")
    else
      doRm(state, absolutePath)
  }

  def doRm(state: State, path: String): State = {
    def rmHelper(currentDirectory: Directory, tokens: List[String]): Directory = {
      if (tokens.isEmpty) currentDirectory
      else if (tokens.tail.isEmpty) currentDirectory.removeEntry(tokens.head)
      else {
        val nextDir = currentDirectory.findEntry(tokens.head)
        if (nextDir == null || !nextDir.isDirectory) currentDirectory
        else {
          val newNextDir = rmHelper(nextDir.asDirectory, tokens.tail)
          if (newNextDir == nextDir) currentDirectory
          else currentDirectory.replaceEntry(tokens.head, newNextDir)
        }
      }
    }

    val tokens: List[String] = path.substring(1).split(Directory.SEPARATOR).toList
    val newRoot: Directory = rmHelper(state.root, tokens)

    if (newRoot == state.root) state.setMessage(s"$path: No such file or directory")
    else State(newRoot, newRoot.findDescendant(state.wd.path.substring(1)))
  }
}
