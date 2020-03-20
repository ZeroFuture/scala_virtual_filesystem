package com.scala.oop.filesystem

import java.util.Scanner

import com.scala.oop.commands.Command
import com.scala.oop.files.Directory

object FileSystem extends App {

  val root = Directory.ROOT

//  io.Source.stdin.getLines().foldLeft(State(root, root))((currentState, newLine) => {
//    currentState.show()
//    Command.from(newLine)(currentState)
//  })

  val scanner = new Scanner(System.in)

  var state = State(root, root)

  while (true) {
    state.show()
    state = Command.from(scanner.nextLine())(state)
  }
}
