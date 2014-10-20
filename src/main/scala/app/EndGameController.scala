package app

import org.flagship.console.screen.{Screen, ConsoleKey}
import org.flagship.game.Controller
import state.StateMachine

// Created: 10/17/2014

class EndGameController extends Controller {
  override def render(screen: Screen): Unit = ???

  override def keyPressed(key: ConsoleKey): Unit = ???

  override def update(elapsed: Int): Unit = ???
}
