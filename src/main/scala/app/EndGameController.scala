package app

import org.flagship.console.app.GameEngine
import org.flagship.console.screen.{Screen, ConsoleKey}

// Created: 10/17/2014

class EndGameController extends GameEngine.Controller {
  def update(elapsedTime: Int): Unit = ???
  def render(screen: Screen): Unit = ???
  def keyPressed(key: ConsoleKey): Unit = ???
}
