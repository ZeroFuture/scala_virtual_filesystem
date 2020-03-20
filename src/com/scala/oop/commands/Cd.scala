package com.scala.oop.commands

import com.scala.oop.files.{Directory, Entry}
import com.scala.oop.filesystem.State

import scala.annotation.tailrec

class Cd(path: String) extends Command {
  override def apply(state: State): State = {
    val root = state.root
    val wd = state.wd

    val absolutePath =
      if (path.startsWith(Directory.SEPARATOR)) path
      else if (wd.isRoot) wd.path + path
      else wd.path + Directory.SEPARATOR + path

    val destDirectory = doFindEntry(root, absolutePath)

    if (destDirectory == null || !destDirectory.isDirectory)
      state.setMessage(s"$path: No such directory!")
    else
      State(root, destDirectory.asDirectory)
  }

  def doFindEntry(root: Directory, path: String): Entry = {
    @tailrec
    def findEntryHelper(currentDirectory: Directory, tokens: List[String]): Entry = {
      if (tokens.isEmpty || tokens.head.isEmpty) currentDirectory
      else if (tokens.tail.isEmpty) currentDirectory.findEntry(tokens.head)
      else {
        val nextDir = currentDirectory.findEntry(tokens.head)
        if (nextDir == null || !nextDir.isDirectory) null
        else findEntryHelper(nextDir.asDirectory, tokens.tail)
      }
    }

    @tailrec
    def collapseTokens(tokens: List[String], result: List[String]): List[String] = {
      if (tokens.isEmpty) result
      else if (tokens.head.equals(".")) collapseTokens(tokens.tail, result)
      else if (tokens.head.equals("..")) {
        if (result.isEmpty) null
        else collapseTokens(tokens.tail, result.init)
      }
      else collapseTokens(tokens.tail, result :+ tokens.head)
    }

    val tokens: List[String] = path.substring(1).split(Directory.SEPARATOR).toList
    val collapsedTokens: List[String] = collapseTokens(tokens, List())

    if (collapsedTokens == null) null
    else findEntryHelper(root, collapsedTokens)
  }
}
