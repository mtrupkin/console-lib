package org.flagship.console.app

/**
 * User: mtrupkin
 * Date: 11/29/13
 */


import org.flagship.console.terminal.{GUIConsole, SwingTerminal}
import org.flagship.console.Size


// TODO: fix right grab for vertical layout
object GameApp extends App {
  val size = Size(120, 42)
  val term = new SwingTerminal(size, "Brace for Impact")
  val window = new MainWindow(size)

  val gui = new GUIConsole(term, window)

  gui.doEventLoop()
}