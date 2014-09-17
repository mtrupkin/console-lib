package org.flagship.console.app

/**
 * User: mtrupkin
 * Date: 11/29/13
 */


import org.flagship.console.terminal.SwingTerminal
import org.flagship.console.{Point, Size}
import org.flagship.game.{GameEngine, Game}
import org.flagship.console.screen.{ConsoleKey, Screen}
import model.World


// TODO: fix right grab for vertical layout
object GameApp extends App with GameEngine {
  val size = Size(120, 42)
  val screen = Screen(size)
  val terminal = new SwingTerminal(size, "App")
  val world = new World()
  val window = new MainWindow(size, world)

  gameLoop()

  override def update() = {
    world.update(100)
  }

  override def render(screen: Screen) = window.render(screen)

  def keyPressed(key: ConsoleKey) {
    import scala.swing.event.Key._

    val k = key.keyValue
    k match {
      case W | Up => world.player.move(Point.Up)
      case A | Left => world.player.move(Point.Left)
      case S | Down => world.player.move(Point.Down)
      case D | Right => world.player.move(Point.Right)
      case Enter => ???
      case Escape => ???
      case _ =>
      case _ =>
    }
  }
}