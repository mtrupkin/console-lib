package org.flagship.game

import org.flagship.console.screen.{ConsoleKey, Screen}

// Created: 9/17/2014

trait Controller {
  def update(elapsed: Int)
  def render(screen: Screen)

  def keyPressed(key: ConsoleKey)
}
