package org.flagship.game

import org.flagship.console.screen.{ConsoleKey, Screen}
import state.{StateMachine, State}

// Created: 9/17/2014

trait Controller {
  def render(screen: Screen): Unit
  def keyPressed(key: ConsoleKey): Unit
  def update(machine: GameEngine, elapsed: Int): Unit = {}
}
