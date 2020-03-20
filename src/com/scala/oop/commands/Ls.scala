package com.scala.oop.commands

import com.scala.oop.files.Entry
import com.scala.oop.filesystem.State

class Ls extends Command {
  override def apply(state: State): State = {
    val contents = state.wd.contents
    val output = formatContents(contents)
    state.setMessage(output)
  }

  def formatContents(contents: List[Entry]): String = {
    if (contents.isEmpty) ""
    else s"${contents.head.name}[${contents.head.getType}]\n${formatContents(contents.tail)}"
  }
}
