package com.scala.oop.commands

import com.scala.oop.filesystem.State

class Cat(filename: String) extends Command {
  override def apply(state: State): State = {
    val wd = state.wd
    val entry = wd.findEntry(filename)

    if (entry == null || !entry.isFile)
      state.setMessage(s"$filename: No such file!")
    else
      state.setMessage(entry.asFile.contents)
  }
}
