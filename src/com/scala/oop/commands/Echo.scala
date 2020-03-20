package com.scala.oop.commands

import com.scala.oop.files.{Directory, File}
import com.scala.oop.filesystem.State

import scala.annotation.tailrec

class Echo(args: Array[String]) extends Command {
  override def apply(state: State): State = {
    if (args.isEmpty) state
    else if (args.length == 1) state.setMessage(args(0))
    else {
      val APPEND = ">>"
      val WRITE = ">"
      val operator = args(args.length - 2)
      val filename = args(args.length - 1)
      val contents = extractContent(args, args.length - 2)

      if (operator.equals(APPEND))
        doEcho(state, contents, filename, append = true)
      else if (operator.equals(WRITE))
        doEcho(state, contents, filename, append = false)
      else
        state.setMessage(extractContent(args, args.length))
    }
  }

  def getRootAfterEcho(currentDirectory: Directory, tokens: List[String], contents: String, append: Boolean): Directory = {
    if (tokens.isEmpty) currentDirectory
    else if (tokens.tail.isEmpty) {
      val entry = currentDirectory.findEntry(tokens.head)
      if (entry == null) currentDirectory.addEntry(new File(currentDirectory.path, tokens.head, contents))
      else if (entry.isDirectory) currentDirectory
      else {
        if (append) currentDirectory.replaceEntry(tokens.head, entry.asFile.appendContents(contents))
        else currentDirectory.replaceEntry(tokens.head, entry.asFile.setContents(contents))
      }
    }
    else {
      val nextDir = currentDirectory.findEntry(tokens.head).asDirectory
      val newNextDir = getRootAfterEcho(nextDir, tokens.tail, contents, append)

      if (newNextDir == nextDir) currentDirectory
      else currentDirectory.replaceEntry(tokens.head, newNextDir)
    }
  }

  def doEcho(state: State, contents: String, filename: String, append: Boolean): State = {
    if (filename.contains(Directory.SEPARATOR))
      state.setMessage(s"Echo: filename $filename must not contain separators")
    else {
      val newRoot: Directory = getRootAfterEcho(state.root, state.wd.getAllDirsInPath :+ filename, contents, append)
      if (newRoot == state.root) state.setMessage(filename + ": No such file")
      else State(newRoot, newRoot.findDescendant(state.wd.getAllDirsInPath))
    }
  }

  def extractContent(args: Array[String], topIdx: Int): String = {
    @tailrec
    def extractContentHelper(currentIdx: Int, accumulator: String): String = {
      if (currentIdx >= topIdx) accumulator
      else extractContentHelper(currentIdx + 1, accumulator + " " + args(currentIdx))
    }

    extractContentHelper(0, "")
  }
}
