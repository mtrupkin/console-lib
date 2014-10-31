package app


import org.flagship.console.screen.{Screen, ConsoleKey}
import org.flagship.game.ViewStateMachine

// Created: 10/17/2014

class EndGameController extends ViewStateMachine.Controller {
  def update(elapsedTime: Int): Unit = ???
  def render(screen: Screen): Unit = ???
  def keyPressed(key: ConsoleKey): Unit = ???
}
